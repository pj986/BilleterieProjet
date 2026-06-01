package dao;

import models.Evenement;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EvenementDAOTest {

    static Evenement testEvent;

    @BeforeAll
    static void setup() {
        testEvent = new Evenement(
                "TEST_EVENT",
                "test",
                "2026-01-01",
                "Paris",
                10.0,
                ""
        );
    }

    @Test
    @Order(1)
    void testAddEvent() {
        boolean result = EvenementDAO.addEvent(testEvent);
        assertTrue(result);
    }

    @Test
    @Order(2)
    void testGetAll() {
        List<Evenement> events = EvenementDAO.getAll();
        assertNotNull(events);
        assertTrue(events.size() > 0);
    }

    @Test
    @Order(3)
    void testDeleteEvent() {
        boolean result = EvenementDAO.deleteEvent("TEST_EVENT");
        assertTrue(result);
    }
}