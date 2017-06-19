/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import javax.swing.JComponent;
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
public class PositiveNumberVerifierTest {
    
    PositiveNumberVerifier instance = new PositiveNumberVerifier();
    
    public PositiveNumberVerifierTest() {
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
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyNonSense() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("http://ww.avan")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyDecimalInputNumber() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("12,8")));
        assertEquals(false, instance.verify(new JTextField("12.8")));
        assertEquals(false, instance.verify(new JTextField(".8")));
        assertEquals(false, instance.verify(new JTextField(",8")));
        assertEquals(false, instance.verify(new JTextField("8.")));
        assertEquals(false, instance.verify(new JTextField("8,")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyZeroInputNumber() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("0")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyIllegalInputNumber() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("-1")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyCorrectInputNumber() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("198")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyEmptyInputNumber() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("")));
    }
    
    /**
     * Test of verify method, of class PositiveNumberVerifier.
     */
    @Test
    public void testVerifyWhitespaceInputNumber() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField(" ")));
    }
    
}
