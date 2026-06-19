package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvenementTest {

    @Test
    void constructeurDoitInitialiserToutesLesInformations() {

        Evenement evenement = new Evenement(
                "Concert Paris",
                "Concert",
                "2026-08-15",
                "Accor Arena",
                59.90,
                "event1.jpg"
        );

        assertEquals("Concert Paris", evenement.getTitre());
        assertEquals("Concert", evenement.getCategorie());
        assertEquals("2026-08-15", evenement.getDate());
        assertEquals("Accor Arena", evenement.getLieu());
        assertEquals(59.90, evenement.getPrix(), 0.001);
        assertEquals("event1.jpg", evenement.getImage());
    }

    @Test
    void idDoitEtreZeroPourUnEvenementCreeSansIdentifiant() {

        Evenement evenement = new Evenement(
                "Match PSG",
                "Sport",
                "2026-09-20",
                "Parc des Princes",
                45.00,
                "psg.jpg"
        );

        assertEquals(0, evenement.getId());
    }

    @Test
    void prixPeutContenirDesDecimales() {

        Evenement evenement = new Evenement(
                "Spectacle",
                "Spectacle",
                "2026-10-10",
                "Théâtre",
                24.99,
                "spectacle.jpg"
        );

        assertEquals(24.99, evenement.getPrix(), 0.001);
    }
}