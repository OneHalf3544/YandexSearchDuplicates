package ru.yandex.test.shingles;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author OneHalf
 */
public class ShingleTest {
    
    private String exampleString = 
            " Я помню чудное мгновенье:\n"
            + "Передо мной явилась ты, \n"
            + "Как мимолетное виденье, \n"
            + "Как гений чистой красоты.";
    
    private String exampleString2 = 
            "В томленьях грусти безнадежной\n"
            + "В тревогах шумной суеты,\n"
            + "Звучал мне долго голос нежный\n"
            + "И снились милые черты.\n";

    private String exampleString3 = 
            "Шли годы. Бурь порыв мятежный\n"
            + "Рассеял прежние мечты,\n"
            + "И я забыл твой голос нежный,\n"
            + "Твои небесные черты.";

    private String exampleString4 = 
            "В глуши, во мраке заточенья\n"
            + "Тянулись тихо дни мои\n"
            + "Без божества, без вдохновенья,\n"
            + "Без слез, без жизни, без любви.\n";

    private String exampleString5 = 
            "Душе настало пробужденье:\n"
            + "И вот опять явилась ты,\n"
            + "Как мимолетное виденье,\n"
            + "Как гений чистой красоты.\n";

    
    public ShingleTest() {
    }

    /**
     * Test of canonize method, of class Shingle.
     */
    @Test
    public void testCanonize() {
        System.out.println("canonize");
        
        String expResult = 
                "я помню чудное мгновенье "
                + "передо мной явилась ты "
                + "мимолетное виденье "
                + "гений чистой красоты";
        String result = Shingle.canonize(exampleString);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCorellation() {
        System.out.println("Corellation");
        
        double corellation = Shingle.corellation(new Shingle(exampleString), new Shingle(exampleString));
        assertEquals(100.0, corellation, 0.001);
        
        corellation = Shingle.corellation(new Shingle(exampleString), new Shingle(exampleString5));
        assertEquals(35.0, corellation, 0.5);
        
        corellation = Shingle.corellation(
                new Shingle(exampleString + exampleString2), 
                new Shingle(exampleString4 + exampleString4));
        assertEquals(0.0, corellation, 0.001);
    }
}
