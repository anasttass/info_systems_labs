
package ru.stud.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.stud.dto.ErrorCode;
import ru.stud.dto.HumanBeingForm;
import ru.stud.dto.HumanBeingResponse;

import ru.stud.event.HumanBeingChangedEvent;

import ru.stud.exception.ObjectDeletionException;
import ru.stud.exception.ResourceNotFoundException;

import ru.stud.model.Car;
import ru.stud.model.Coordinates;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.model.User;
import ru.stud.model.WeaponType;

import ru.stud.repository.CarRepository;
import ru.stud.repository.CoordinatesRepository;
import ru.stud.repository.HumanBeingRepository;
import ru.stud.repository.UserRepository;

import ru.stud.util.HumanBeingMapper;

import java.util.List;

@Service
public class HumanBeingService {

    private final HumanBeingRepository humanBeingRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final HumanBeingMapper humanBeingMapper;

    public HumanBeingService(
            HumanBeingRepository humanBeingRepository,
            CoordinatesRepository coordinatesRepository,
            CarRepository carRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher,
            HumanBeingMapper humanBeingMapper
    ) {
        this.humanBeingRepository = humanBeingRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.humanBeingMapper = humanBeingMapper;
    }

    // ============================================
    // ПОЛУЧЕНИЕ ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ
    // ============================================

    private Authentication getAuthentication() {
        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(
                authentication.getPrincipal()
        )) {
            throw new AccessDeniedException(
                    "Пользователь не авторизован"
            );
        }

        return authentication;
    }

    private String getCurrentLogin() {
        return getAuthentication().getName();
    }

    private User getCurrentUser() {
        return userRepository
                .findByLogin(getCurrentLogin())
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "Текущий пользователь не найден"
                        )
                );
    }

    private boolean isAdmin() {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(
                                authority.getAuthority()
                        )
                );
    }

    // ============================================
    // ПРОВЕРКА ПРАВ
    // ============================================

    private void checkCanModify(HumanBeing hero) {
        // Администратор может изменять любые объекты.
        if (isAdmin()) {
            return;
        }

        User owner = hero.getOwner();

        if (owner == null ||
                !owner.getLogin().equals(getCurrentLogin())) {
            throw new AccessDeniedException(
                    "Вы можете изменять или удалять " +
                            "только собственные объекты"
            );
        }
    }

    // ============================================
    // ПОЛУЧЕНИЕ HUMAN BEINGS
    // ============================================

    @Transactional(readOnly = true)
    public List<HumanBeingResponse> getAll() {
        return humanBeingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<HumanBeingResponse> getAll(
            Pageable pageable,
            Boolean realHero,
            Boolean hasToothpick,
            Mood mood,
            WeaponType weaponType
    ) {
        return humanBeingRepository
                .findAllWithFilters(
                        realHero,
                        hasToothpick,
                        mood,
                        weaponType,
                        pageable
                )
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public HumanBeingResponse getById(Long id) {
        HumanBeing hero = getEntityById(id);
        return toResponse(hero);
    }

    private HumanBeing getEntityById(Long id) {
        return humanBeingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.HUMAN_NOT_FOUND,
                                "Герой с ID " + id + " не найден"
                        )
                );
    }

    // ============================================
    // СОЗДАНИЕ HUMAN BEING
    // ============================================

    @Transactional
    public HumanBeingForm create(HumanBeingForm form) {
        User currentUser = getCurrentUser();

        HumanBeing hero = new HumanBeing();

        // Владелец определяется по JWT,
        // а не по данным, присланным frontend.
        hero.setOwner(currentUser);

        fillHumanBeing(hero, form, null);

        HumanBeing savedHero =
                humanBeingRepository.save(hero);

        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );

        return toForm(savedHero);
    }

    // ============================================
    // ОБНОВЛЕНИЕ HUMAN BEING
    // ============================================

    @Transactional
    public HumanBeingForm update(
            Long id,
            HumanBeingForm form
    ) {
        HumanBeing hero = getEntityById(id);

        // USER может изменять только свои объекты.
        // ADMIN может изменять любые.
        checkCanModify(hero);

        fillHumanBeing(hero, form, id);

        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );

        return toForm(hero);
    }

    // ============================================
    // ЗАПОЛНЕНИЕ ДАННЫХ HUMAN BEING
    // ============================================

    private void fillHumanBeing(
            HumanBeing hero,
            HumanBeingForm form,
            Long currentHumanId
    ) {
        Coordinates coordinates =
                coordinatesRepository
                        .findById(form.getCoordinatesId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.COORDINATES_NOT_FOUND,
                                        "Координаты с ID " +
                                                form.getCoordinatesId() +
                                                " не найдены"
                                )
                        );

        Car car = null;

        if (form.getCarId() != null) {
            car = carRepository
                    .findById(form.getCarId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    ErrorCode.CAR_NOT_FOUND,
                                    "Автомобиль с ID " +
                                            form.getCarId() +
                                            " не найден"
                            )
                    );

            boolean carAlreadyUsed;

            if (currentHumanId == null) {
                carAlreadyUsed =
                        humanBeingRepository
                                .existsByCarId(
                                        form.getCarId()
                                );
            } else {
                carAlreadyUsed =
                        humanBeingRepository
                                .existsByCarIdAndIdNot(
                                        form.getCarId(),
                                        currentHumanId
                                );
            }

            if (carAlreadyUsed) {
                throw new IllegalStateException(
                        "Автомобиль с ID " +
                                form.getCarId() +
                                " уже принадлежит другому HumanBeing"
                );
            }
        }

        hero.setName(form.getName());
        hero.setCoordinates(coordinates);
        hero.setRealHero(form.getRealHero());
        hero.setHasToothpick(form.getHasToothpick());
        hero.setCar(car);
        hero.setMood(form.getMood());
        hero.setImpactSpeed(form.getImpactSpeed());
        hero.setSoundtrackName(form.getSoundtrackName());
        hero.setMinutesOfWaiting(form.getMinutesOfWaiting());
        hero.setWeaponType(form.getWeaponType());
    }

    // ============================================
    // УДАЛЕНИЕ HUMAN BEING
    // ============================================

    @Transactional
    public void delete(Long id) {
        HumanBeing hero = getEntityById(id);

        // Проверяем владельца до удаления.
        checkCanModify(hero);

        try {
            humanBeingRepository.delete(hero);
            humanBeingRepository.flush();

            eventPublisher.publishEvent(
                    new HumanBeingChangedEvent()
            );

        } catch (DataIntegrityViolationException e) {
            throw new ObjectDeletionException(
                    ErrorCode.HUMAN_DELETE_FORBIDDEN,
                    "Невозможно удалить героя с ID " + id +
                            ", поскольку с ним связаны другие объекты"
            );
        }
    }

    // ============================================
    // DTO
    // ============================================

    private HumanBeingForm toForm(HumanBeing hero) {
        return humanBeingMapper.toForm(hero);
    }

    private HumanBeingResponse toResponse(
            HumanBeing humanBeing
    ) {
        return humanBeingMapper.toResponse(humanBeing);
    }
}
