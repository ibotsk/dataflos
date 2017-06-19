/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.models.ImportMonitorModel.ImportMonitorInfo;

/**
 *
 * @author jakub.husar
 */
public class ImportMonitorModelTest {
    
    public ImportMonitorModelTest() {
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
     * Test of getRowCount method, of class ImportMonitorModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        ImportMonitorModel instance = new ImportMonitorModel();
        instance.addRow("H", 12, "Msg", null);
        instance.addRow("L", 4, "Warn not exists", null);
        instance.addRow("T", 8, "Err on line 8", null);
        int count = instance.getRowCount();
        assertEquals(3, count);
    }

    /**
     * Test of getColumnCount method, of class ImportMonitorModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        ImportMonitorModel instance = new ImportMonitorModel();
        int expResult = 4;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnClass method, of class ImportMonitorModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        int columnIndex = 2;
        ImportMonitorModel instance = new ImportMonitorModel();
        Class expResult = Object.class;
        Class result = instance.getColumnClass(columnIndex);
        assertEquals(expResult, result);
        
        int columnIndex2 = 3;
        Class expResult2 = ImageIcon.class;
        Class result2 = instance.getColumnClass(columnIndex2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getValueAt method, of class ImportMonitorModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        int rowIndex = 1;
        int columnIndex = 2;
        ImportMonitorModel instance = new ImportMonitorModel();
        instance.addRow("H", 12, "Msg", null);
        instance.addRow("L", 4, "Warn not exists", null);
        instance.addRow("T", 8, "Err on line 8", null);
        Object result = instance.getValueAt(rowIndex, columnIndex);
        assertEquals("Warn not exists", result);
        
    }

    /**
     * Test of getColumnName method, of class ImportMonitorModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int column = 3;
        ImportMonitorModel instance = new ImportMonitorModel();
        String expResult = "Stav importu";
        String result = instance.getColumnName(column);
        assertEquals(expResult, result);
    }

    /**
     * Test of addRow method, of class ImportMonitorModel.
     */
    @Test
    public void testAddRow() {
        System.out.println("addRow");
        String typ = "H";
        int rowNumber = 12;
        String message = "Wihout warnings";
        ImageIcon icon = null;
        ImportMonitorModel instance = new ImportMonitorModel();
        int oldCount = instance.getRowCount();
        instance.addRow(typ, rowNumber, message, icon);
        int newCount = instance.getRowCount();
        assertTrue(oldCount < newCount);
    }

    /**
     * Test of clear method, of class ImportMonitorModel.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        String typ = "H";
        int rowNumber = 12;
        String message = "Wihout warnings";
        ImageIcon icon = null;
        ImportMonitorModel instance = new ImportMonitorModel();
        instance.addRow(typ, rowNumber, message, icon);
        int count = instance.getRowCount();
        assertTrue(0 < count);
        instance.clear();
        int newCount = instance.getRowCount();
        assertEquals(0, newCount);
    }
    
}
