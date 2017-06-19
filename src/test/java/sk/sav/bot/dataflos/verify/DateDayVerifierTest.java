/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import javax.swing.JTextField;
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
public class DateDayVerifierTest {
    
    static DateDayVerifier instance = new DateDayVerifier();
    
    public DateDayVerifierTest() {
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
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyNonSense() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("http://ww.avan")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyWrongFormatofDay() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField(".8")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyIllegalDay() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("100")));
        assertEquals(false, instance.verify(new JTextField("32")));
        assertEquals(false, instance.verify(new JTextField("0")));
        assertEquals(false, instance.verify(new JTextField("-1")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyCorrectDay() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("11")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyCorrectZeroStartingDay() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("07")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyEmptyDay() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("")));
    }
    
    /**
     * Test of verify method, of class DateDayVerifier.
     */
    @Test
    public void testVerifyWhitespaceDay() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("  ")));
    }
    
}
