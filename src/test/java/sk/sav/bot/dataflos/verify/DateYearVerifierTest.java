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
public class DateYearVerifierTest {
    
    static DateYearVerifier instance = new DateYearVerifier();
    
    public DateYearVerifierTest() {
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
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyNonSense() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("http://ww.avan")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyWrongFormatofYear() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("12,8")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyUpcomingYear() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("2031")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyIllegalYear() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("-1")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyCorrectYear() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("1998")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyEmptyYear() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("")));
    }
    
    /**
     * Test of verify method, of class DateYearVerifier.
     */
    @Test
    public void testVerifyWhitespaceYear() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField(" ")));
    }
    
    
}
