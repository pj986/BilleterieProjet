package test;

import java.util.Stack;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LIFOTest {

    // Créer un attribut pileLIFO de type Stack<String>
    private static Stack<String> pileLIFO;

    // L’instancier avant chaque tests avec deux items (item1 et item2)
    @BeforeEach
    void setUp() throws Exception {
        pileLIFO = new Stack<String>();
        pileLIFO.push("item1");
        pileLIFO.push("item2");
    }

    // Créer un test ajoutTest qui vérifie que l’élément ajouté « item3 » est bien le premier
    @Test // Annotation
    public void ajoutTest() {
        pileLIFO.push("item3");
        assertEquals("item3", pileLIFO.pop());
    }

    // Créer un test tailleTest qui vérifie que la pile a bien deux éléments
    @Test // Annotation
    public void tailleTest() {
        assertEquals(2, pileLIFO.size());
    }

    // Vérifie que le 1° et le 2° éléments sont à leur place
    @Test // Annotation
    public void ordrePremierElementTest() {
        assertEquals("item2", pileLIFO.pop());
    }

    @Test // Annotation
    public void ordreDeuxiemeElementTest() {
        pileLIFO.pop();
        assertEquals("item1", pileLIFO.pop());
    }

    // A la fin de chaque teste effacer (clear) la pile
    @AfterAll
    static void tearDownClass() throws Exception {
        pileLIFO.clear();
    }
}

