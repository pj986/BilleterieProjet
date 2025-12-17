import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaysTest {

    @BeforeAll
    static void setUpClass() throws Exception {
        System.out.println("→ Avant TOUS les tests (init globale)");
    }

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("→ Avant CHAQUE test (préparation)");
    }

    @Test
    public void testGetPays() {
        Pays pays = new Pays("France");
        assertEquals("France", pays.getPays());
        System.out.println("Test exécuté : getPays retourne France");
    }

    @AfterEach
    void tearDown() throws Exception {
        System.out.println("→ Après CHAQUE test (nettoyage)");
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        System.out.println("→ Après TOUS les tests (fin globale)");
    }
}

