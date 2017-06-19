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
public class DateMonthVerifierTest {
    
    static DateMonthVerifier instance = new DateMonthVerifier();
    
    public DateMonthVerifierTest() {
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
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyNonSense() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("http://ww.avan")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyWrongFormatofMonth() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField(".8")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyIllegalMonth() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("20")));
        assertEquals(false, instance.verify(new JTextField("0")));
        assertEquals(false, instance.verify(new JTextField("-1")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyCorrectMonth() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("11")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyCorrectZeroStartingMonth() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("07")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyEmptyMonth() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("")));
    }
    
    /**
     * Test of verify method, of class DateMonthVerifier.
     */
    @Test
    public void testVerifyWhitespaceMonth() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("  ")));
    }
    
}
