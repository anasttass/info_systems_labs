package ru.stud.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.stud.dto.CoordinatesForm;
import ru.stud.dto.HumanBeingResponse;
import ru.stud.util.CoordinatesMapper;

import static org.junit.jupiter.api.Assertions.*;

class ModelContractTest {
    private final ObjectMapper json = new ObjectMapper().findAndRegisterModules();

    @Test
    void coordinatesMapperPreservesAllProperties() {
        CoordinatesForm form = new CoordinatesForm();
        form.setId(12L);
        form.setX(-42.5);
        form.setY(780.25f);
        CoordinatesMapper mapper = Mappers.getMapper(CoordinatesMapper.class);
        Coordinates entity = mapper.toEntity(form);
        CoordinatesForm result = mapper.toForm(entity);
        assertAll(
                () -> assertEquals(form.getId(), result.getId()),
                () -> assertEquals(form.getX(), result.getX()),
                () -> assertEquals(form.getY(), result.getY()));
    }

    @Test
    void coordinatesJsonRoundTripPreservesContract() throws Exception {
        String input = "{\"id\":12,\"x\":-42.5,\"y\":780.25}";
        Coordinates coordinates = json.readValue(input, Coordinates.class);
        assertEquals(json.readTree(input), json.readTree(json.writeValueAsString(coordinates)));
    }

    @Test
    void responsePreservesNestedJsonAndBooleanPropertyNames() throws Exception {
        HumanBeingResponse response = new HumanBeingResponse();
        response.setCoordinates(json.readValue("{\"id\":12,\"x\":-42.5,\"y\":780.25}", Coordinates.class));
        Car car = new Car();
        car.setId(7L);
        car.setCool(true);
        response.setCar(car);
        response.setRealHero(true);
        response.setHasToothpick(true);
        var tree = json.readTree(json.writeValueAsString(response));
        assertEquals(json.readTree("{\"id\":12,\"x\":-42.5,\"y\":780.25}"), tree.get("coordinates"));
        assertEquals(json.readTree("{\"id\":7,\"cool\":true}"), tree.get("car"));
        assertTrue(tree.get("realHero").booleanValue());
        assertTrue(tree.get("hasToothpick").booleanValue());
        assertFalse(tree.has("isRealHero"));
        assertFalse(tree.has("isHasToothpick"));
    }

    @Test
    void entitiesRetainPublicNoArgumentConstructors() throws Exception {
        for (Class<?> type : new Class<?>[]{Coordinates.class, Car.class, HumanBeing.class, User.class, LoadingRecord.class}) {
            assertNotNull(type.getConstructor().newInstance());
        }
    }

    @Test
    void customBooleanAccessorsRetainTheirSignaturesAndBehavior() throws Exception {
        assertEquals(Boolean.class, Car.class.getMethod("getCool").getReturnType());
        assertTrue(Car.class.getMethod("getCool").getAnnotatedReturnType().isAnnotationPresent(NotNull.class));
        assertEquals(boolean.class, HumanBeing.class.getMethod("isRealHero").getReturnType());
        assertNotNull(HumanBeing.class.getMethod("setRealHero", boolean.class));
        HumanBeing human = new HumanBeing();
        assertThrows(NullPointerException.class, human::isRealHero);
        human.setRealHero(false);
        assertFalse(human.isRealHero());
        human.setRealHero(true);
        assertTrue(human.isRealHero());
    }
}
