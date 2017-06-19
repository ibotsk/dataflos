/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LitZdrojRev;

/**
 *
 * @author jakub.husar
 */
public class LiteraturesModelTest {
    
    public LiteraturesModelTest() {
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
     * Test of getLiteratures method, of class LiteraturesModel.
     */
    @Test
    public void testGetSetLiteratures() {
        System.out.println("getLiteratures");
        List<LitZdrojRev> literatures = new ArrayList<>();
        LitZdroj lz = new LitZdroj();
        lz.setRocnik("12");
        LitZdrojRev lzr = new LitZdrojRev();
        lzr.setLitZdroj(lz);
        literatures.add(lzr);
        LiteraturesTableModel instance = new LiteraturesTableModel();
        instance.setLiteratures(literatures);
        List<LitZdrojRev> result = instance.getLiteratures();
        assertEquals(literatures, result);
    }

    /**
     * Test of getRowCount method, of class LiteraturesModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        LiteraturesTableModel instance = new LiteraturesTableModel();
        int expResult = 0;
        int result = instance.getRowCount();
        assertEquals(expResult, result);

        LitZdrojRev lzr = new LitZdrojRev();
        LitZdroj lz = new LitZdroj();
        lz.setRok("1243");
        lzr.setLitZdroj(lz);
        LiteraturesTableModel instance2 = new LiteraturesTableModel();
        instance2.addRow(lzr);
        int expResult2 = 1;
        int result2 = instance2.getRowCount();
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getColumnCount method, of class LiteraturesModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        LiteraturesTableModel instance = new LiteraturesTableModel();
        int expResult = 4;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueAt method, of class LiteraturesModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        String expResult = "Nazov noveho clanku";
        int row = 0;
        int column = 2;
        LitZdrojRev lzr = new LitZdrojRev();
        LitZdroj lz = new LitZdroj();
        lz.setNazovClanku(expResult);
        lzr.setLitZdroj(lz);
        LiteraturesTableModel instance = new LiteraturesTableModel();
        instance.addRow(lzr);
        Object result = instance.getValueAt(row, column);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnName method, of class LiteraturesModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int column = 1;
        LiteraturesTableModel instance = new LiteraturesTableModel();
        String expResult = "Autori";
        String result = instance.getColumnName(column);
        assertEquals(expResult, result);
    }
    
}
