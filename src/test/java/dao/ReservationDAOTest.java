package dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDAOTest {

    @Test
    void countAllDoitRetournerUnNombrePositifOuNul() {
        int total = ReservationDAO.countAll();

        assertTrue(
                total >= 0,
                "Le nombre total de réservations ne peut pas être négatif."
        );
    }

    @Test
    void existsDoitRetournerFalsePourUneReservationInexistante() {
        String emailInexistant = "reservation.inexistante@test.invalid";
        int evenementInexistant = -9999;

        boolean existe = ReservationDAO.exists(
                emailInexistant,
                evenementInexistant
        );

        assertFalse(
                existe,
                "Une réservation inexistante ne doit pas être détectée."
        );
    }
}