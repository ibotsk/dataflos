/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jakub.husar
 */
public class DataVerifierTest {
    
    public DataVerifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isIntNumber method, of class DataVerifier.
     */
    @Test
    public void testIsIntNumberWrongFormat() {
        System.out.println("isNumeric");
        assertEquals(false, DataVerifier.isIntNumber("0x71a"));
        assertEquals(false, DataVerifier.isIntNumber("7x71"));
        assertEquals(false, DataVerifier.isIntNumber("aZb"));
        assertEquals(false, DataVerifier.isIntNumber("Seven"));
        assertEquals(false, DataVerifier.isIntNumber("4,7"));
    }
    
    /**
     * Test of isIntNumber method, of class DataVerifier.
     */
    @Test
    public void testIsIntNumberDecimal() {
        System.out.println("isNumeric");
        assertEquals(false, DataVerifier.isIntNumber("-1.4"));
        assertEquals(false, DataVerifier.isIntNumber("111.8"));
        assertEquals(false, DataVerifier.isIntNumber(".0"));
        assertEquals(false, DataVerifier.isIntNumber("8."));
    }
    
    /**
     * Test of isIntNumber method, of class DataVerifier.
     */
    @Test
    public void testIsIntNumberCorrectFormat() {
        System.out.println("isNumeric");
        assertEquals(true, DataVerifier.isIntNumber("182"));
        assertEquals(true, DataVerifier.isIntNumber("-1"));
        assertEquals(true, DataVerifier.isIntNumber("82000"));
    }

    /**
     * Test of isCorrectDateStamp method, of class DataVerifier.
     */
    @Test
    public void testIsCorrectDateStampWrong() {
        System.out.println("isCorrectDateStamp");
        assertEquals(false, DataVerifier.isCorrectDateStamp("rokDatum", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("rokDatum", false));
        assertEquals(false, DataVerifier.isCorrectDateStamp("12.12.12", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("12.12.12", false));
        assertEquals(false, DataVerifier.isCorrectDateStamp("12.2000", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("12.12.2006", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("12.12.2035", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("200075", true));
    }
    
    /**
     * Test of isCorrectDateStamp method, of class DataVerifier.
     */
    @Test
    public void testIsCorrectDateStampEmpty() {
        System.out.println("isCorrectDateStamp");
        assertEquals(false, DataVerifier.isCorrectDateStamp("", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp(" ", true));
        assertEquals(false, DataVerifier.isCorrectDateStamp("", false));
        assertEquals(false, DataVerifier.isCorrectDateStamp(" ", false));
    }
    
    /**
     * Test of isCorrectDateStamp method, of class DataVerifier.
     */
    @Test
    public void testIsCorrectDateStampFuture() {
        System.out.println("isCorrectDateStamp");
        assertEquals(true, DataVerifier.isCorrectDateStamp("20350407", false));
    }
    
    /**
     * Test of isCorrectDateStamp method, of class DataVerifier.
     */
    @Test
    public void testIsCorrectDateStampCorrect() {
        System.out.println("isCorrectDateStamp");
        assertEquals(true, DataVerifier.isCorrectDateStamp("20150407", true));
        assertEquals(true, DataVerifier.isCorrectDateStamp("00351027", true));
    }
    
    /**
     * Test of isKvadrant method, of class DataVerifier.
     */
    @Test
    public void testIsKvadrantWrongFormat() {
        System.out.println("isKvadrant");
        assertEquals(false, DataVerifier.isKvadrant("kvad"));
        assertEquals(false, DataVerifier.isKvadrant("-"));
        assertEquals(false, DataVerifier.isKvadrant("c"));
        assertEquals(false, DataVerifier.isKvadrant("c21345"));
        assertEquals(false, DataVerifier.isKvadrant("2563c"));
    }
    
    /**
     * Test of isKvadrant method, of class DataVerifier.
     */
    @Test
    public void testIsKvadrantEmpty() {
        System.out.println("isKvadrant");
        assertEquals(false, DataVerifier.isKvadrant(""));
        assertEquals(false, DataVerifier.isKvadrant(" "));
    }
    
    /**
     * Test of isKvadrant method, of class DataVerifier.
     */
    @Test
    public void testIsKvadrantCorrect() {
        System.out.println("isKvadrant");
        assertEquals(true, DataVerifier.isKvadrant("80625a"));
        assertEquals(true, DataVerifier.isKvadrant("00000c"));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaNonSense() {
        System.out.println("isNadmorskaVyska");
        assertEquals(false, DataVerifier.isNadmorskaVyska("tristo"));
        assertEquals(false, DataVerifier.isNadmorskaVyska("m.n.m."));
        assertEquals(false, DataVerifier.isNadmorskaVyska("vysoko"));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaWrongFormat() {
        System.out.println("isNadmorskaVyska");
        assertEquals(false, DataVerifier.isNadmorskaVyska("4,9"));
        assertEquals(false, DataVerifier.isNadmorskaVyska("50°2"));
        assertEquals(false, DataVerifier.isNadmorskaVyska("_3027"));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaEmpty() {
        System.out.println("isNadmorskaVyska");
        assertEquals(false, DataVerifier.isNadmorskaVyska(""));
        assertEquals(false, DataVerifier.isNadmorskaVyska(" "));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaInadequate() {
        System.out.println("isNadmorskaVyska");
        assertEquals(false, DataVerifier.isNadmorskaVyska("-300000"));
        assertEquals(false, DataVerifier.isNadmorskaVyska("41000"));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaDecimal() {
        System.out.println("isNadmorskaVyska");
        assertEquals(false, DataVerifier.isNadmorskaVyska("-4.2"));
        assertEquals(false, DataVerifier.isNadmorskaVyska("341.9"));
    }
    
    /**
     * Test of isNadmorskaVyska method, of class DataVerifier.
     */
    @Test
    public void testIsNadmorskaVyskaCorrect() {
        System.out.println("isNadmorskaVyska");
        assertEquals(true, DataVerifier.isNadmorskaVyska("312"));
        assertEquals(true, DataVerifier.isNadmorskaVyska("1046"));
        assertEquals(true, DataVerifier.isNadmorskaVyska("-4"));
    }
    
    /**
     * Test of isZemepisnaPoloha method, of class DataVerifier.
     */
    @Test
    public void testIsZemepisnaPolohaNonSense() {
        System.out.println("isZemepisnaPoloha");
        assertEquals(false, DataVerifier.isZemepisnaPoloha("juhoZapad"));
    }
    
    /**
     * Test of isZemepisnaPoloha method, of class DataVerifier.
     */
    @Test
    public void testIsZemepisnaPolohaWrongFormat() {
        System.out.println("isZemepisnaPoloha");
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12.14.211"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12,14,211"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12°114"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12°11°"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12°11°22°"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12'11'4'"));
        assertEquals(false, DataVerifier.isZemepisnaPoloha("12°11'4\""));
    }
    
    /**
     * Test of isZemepisnaPoloha method, of class DataVerifier.
     */
    @Test
    public void testIsZemepisnaPolohaEmpty() {
        System.out.println("isZemepisnaPoloha");
        assertEquals(false, DataVerifier.isZemepisnaPoloha(""));
        assertEquals(false, DataVerifier.isZemepisnaPoloha(" "));
    }
    
    /**
     * Test of isZemepisnaPoloha method, of class DataVerifier.
     */
    @Test
    public void testIsZemepisnaPolohaCorrectFormat() {
        System.out.println("isZemepisnaPoloha");
        assertEquals(true, DataVerifier.isZemepisnaPoloha("12°11'21''"));
        assertEquals(true, DataVerifier.isZemepisnaPoloha("0°0'0''"));
        assertEquals(true, DataVerifier.isZemepisnaPoloha("00°00'00''"));
        assertEquals(true, DataVerifier.isZemepisnaPoloha("1111111°1111111'21111111''"));
        assertEquals(true, DataVerifier.isZemepisnaPoloha("-1°-1'-1''"));
    }

    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyNonSense() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear("http://ww.avan"));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyWrongFormatofYear() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear("12,8"));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyUpcomingYear() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear("2031"));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyIllegalYear() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear("-1"));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyCorrectYear() {
        System.out.println("isYear");
        assertEquals(true, DataVerifier.isYear("1998"));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyEmptyYear() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear(""));
    }
    
    /**
     * Test of isYear method, of class DataVerifier.
     */
    @Test
    public void testVerifyWhitespaceYear() {
        System.out.println("isYear");
        assertEquals(false, DataVerifier.isYear(" "));
    }
    
}
