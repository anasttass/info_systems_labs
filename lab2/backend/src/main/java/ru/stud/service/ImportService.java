package ru.stud.service;

import com.fasterxml.jackson.databind.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.stud.dto.ImportHistoryResponse;
import ru.stud.dto.ImportHumanForm;
import ru.stud.model.*;
import ru.stud.repository.*;

import java.util.*;

@Service
public class ImportService {
    private final ObjectMapper mapper;
    private final Validator validator;
    private final ImportTransactionService transaction;
    private final LoadingRecordRepository records;
    private final UserRepository users;

    public ImportService(ObjectMapper mapper, Validator validator, ImportTransactionService transaction,
                         LoadingRecordRepository records, UserRepository users) {
        this.mapper = mapper;
        this.validator = validator;
        this.transaction = transaction;
        this.records = records;
        this.users = users;
    }

    public ImportHistoryResponse importFile(MultipartFile file, Jwt jwt) {
        User user = currentUser(jwt);
        LoadingRecord record = new LoadingRecord();
        record.setCreator(user);
        record.setStatus(RecordStatus.PENDING);
        record = records.saveAndFlush(record);
        try {
            if (file.isEmpty()) throw new IllegalArgumentException("Файл пуст");
            JsonNode root = mapper.readTree(file.getInputStream());
            if (root == null || !root.isArray() || root.isEmpty())
                throw new IllegalArgumentException("Ожидается непустой JSON-массив");
            List<ImportHumanForm> items = new ArrayList<>();
            int index = 0;
            for (JsonNode node : root) {
                if (!node.isObject()) throw new IllegalArgumentException("Элемент " + index + " должен быть объектом");
                ImportHumanForm item = mapper.treeToValue(node, ImportHumanForm.class);
                Set<ConstraintViolation<ImportHumanForm>> violations = validator.validate(item);
                if (!violations.isEmpty()) {
                    var violation = violations.iterator().next();
                    throw new IllegalArgumentException("Элемент " + index + ": " + violation.getPropertyPath() + " — " + violation.getMessage());
                }
                items.add(item);
                index++;
            }
            int count = transaction.importAll(items, user);
            record.setStatus(RecordStatus.SUCCEED);
            record.setNumber_of_entities(count);
            return response(records.save(record));
        } catch (Exception ex) {
            record.setStatus(RecordStatus.FAILED);
            record.setNumber_of_entities(null);
            records.save(record);
            throw new ImportFailedException("Импорт #" + record.getId() + " отклонён: " + ex.getMessage());
        }
    }

    public List<ImportHistoryResponse> history(Jwt jwt) {
        User user = currentUser(jwt);
        List<LoadingRecord> list = user.getRole() == UserRoles.ADMIN
                ? records.findAllByOrderByIdDesc()
                : records.findByCreatorIdOrderByIdDesc(user.getId());
        return list.stream().map(this::response).toList();
    }

    private User currentUser(Jwt jwt) {
        Long id = Long.valueOf(jwt.getSubject());
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    private ImportHistoryResponse response(LoadingRecord r) {
        return new ImportHistoryResponse(r.getId(), r.getStatus(), r.getCreator().getLogin(), r.getNumber_of_entities());
    }

    public static class ImportFailedException extends RuntimeException {
        public ImportFailedException(String message) { super(message); }
    }
}
