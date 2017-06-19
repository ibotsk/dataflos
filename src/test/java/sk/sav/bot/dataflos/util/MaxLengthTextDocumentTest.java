/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

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
public class MaxLengthTextDocumentTest {
    
    JTextField tf = new JTextField();
    
    public MaxLengthTextDocumentTest() {
        tf.setDocument(new MaxLengthTextDocument(25));
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
     * Test of insertString method, of class MaxLengthTextDocument.
     */
    @Test
    public void testInsertString() throws Exception {
        System.out.println("insertString");
        MaxLengthTextDocument mltd = (MaxLengthTextDocument) tf.getDocument();
        mltd.insertString(0, "Nieco co ma len tak nerozhodi", null);
        assertEquals("", tf.getText());
        mltd.insertString(0, "Nieco co ma len tak...", null);
        assertEquals("Nieco co ma len tak...", tf.getText());
    }

    /**
     * Test of getMaxChars method, of class MaxLengthTextDocument.
     */
    @Test
    public void testGetMaxChars() {
        System.out.println("getMaxChars");
        MaxLengthTextDocument mltd = (MaxLengthTextDocument) tf.getDocument();
        assertEquals(25, mltd.getMaxChars());
    }

    /**
     * Test of setMaxChars method, of class MaxLengthTextDocument.
     */
    @Test
    public void testSetMaxChars() {
        System.out.println("setMaxChars");
        MaxLengthTextDocument mltd = (MaxLengthTextDocument) tf.getDocument();
        mltd.setMaxChars(20);
        assertEquals(20, mltd.getMaxChars());
    }
    
}
