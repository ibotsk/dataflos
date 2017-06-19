/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
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
public class EntryTest {
    
    Entry instance;
    String text;
    
    public EntryTest() {
        text = "Filed text";
        instance = new Entry(text, 50, null);
    }

    /**
     * Test of enableAdd method, of class Entry.
     */
    @Test
    public void testEnableAdd() {
    }

    /**
     * Test of enableMinus method, of class Entry.
     */
    @Test
    public void testEnableMinus() {
    }

    /**
     * Test of getText method, of class Entry.
     */
    @Test
    public void testGetText() {
        System.out.println("getText");
        
        String expResult = text;
        String result = instance.getText();
        assertEquals(expResult, result);
    }
    
}
