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
public class UrlVerifierTest {
    
    UrlVerifier instance = new UrlVerifier();
    
    public UrlVerifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        //String REGEX_URL = "^[A-Za-z][A-Za-z0-9+.-]{1,120}:[A-Za-z0-9/](([A-Za-z0-9$_.+!*,;/?:@&~=-])|%[A-Fa-f0-9]{2}){1,333}(#([a-zA-Z0-9][a-zA-Z0-9$_.+!*,;/?:@&~=%-]{0,1000}))?$";
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
     * Test of verify method, of class UrlVerifier.
     */
    @Test
    public void testVerifyWrongURL() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("hptp/ww.avan")));
        assertEquals(false, instance.verify(new JTextField("https:\\google.com")));
        assertEquals(false, instance.verify(new JTextField("www.sk")));
        
    }
    
    /**
     * Test of verify method, of class UrlVerifier.
     */
    @Test
    public void testVerifyCorrectURL() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("https://www.avanade.com")));
        assertEquals(true, instance.verify(new JTextField("http://www.sav.sk")));
    }
    
    /**
     * Test of verify method, of class UrlVerifier.
     */
    @Test
    public void testVerifyComplexURL() {
        System.out.println("verify");
        assertEquals(true, instance.verify(new JTextField("http://dataflos.sav.sk:8080/phpPgAdmin/nabelek001.jpg")));
    }
    
    /**
     * Test of verify method, of class UrlVerifier.
     */
    @Test
    public void testVerifyEmptyURL() {
        System.out.println("verify");
        assertEquals(false, instance.verify(new JTextField("")));
    }
    
}
