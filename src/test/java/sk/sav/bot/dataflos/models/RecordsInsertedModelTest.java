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
import sk.sav.bot.dataflos.entity.Udaj;

/**
 *
 * @author jakub.husar
 */
public class RecordsInsertedModelTest {
    
    public RecordsInsertedModelTest() {
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
     * Test of getRowCount method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 105;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        int result = instance.getRowCount();
        assertEquals(100, result);
        instance.pageUp();
        int result2 = instance.getRowCount();
        assertEquals(5, result2);
    }

    /**
     * Test of getColumnCount method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int expResult = 18;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnName method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int column = 2;
        RecordsInsertedModel instance = new RecordsInsertedModel();
        String expResult = "Užívateľ";
        String result = instance.getColumnName(column);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnClass method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        int columnIndex = 0;
        RecordsInsertedModel instance = new RecordsInsertedModel();
        Class expResult = Integer.class;
        Class result = instance.getColumnClass(columnIndex);
        assertEquals(expResult, result);
        
        int columnIndex2 = 1;
        Class expResult2 = String.class;
        Class result2 = instance.getColumnClass(columnIndex2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getValueAt method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        String expResult = "newUser";
        int rowIndex = 0;
        int columnIndex = 2;
        RecordsInsertedModel instance = new RecordsInsertedModel();
        Udaj u = new Udaj();
        u.setUzivatel(expResult);
        instance.addRow(u);
        Object result = instance.getValueAt(rowIndex, columnIndex);
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmpty method, of class RecordsInsertedModel.
     */
    @Test
    public void testSetEmpty() {
        System.out.println("setEmpty");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        instance.addRow(new Udaj());
        instance.setEmpty();
        assertEquals(0, instance.getRowCount());
    }

    /**
     * Test of addRow method, of class RecordsInsertedModel.
     */
    @Test
    public void testAddRow() {
        System.out.println("addRow");
        
        Udaj udaj = new Udaj();
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int oldCount = instance.getRowCount();
        instance.addRow(udaj);
        int newCount = instance.getRowCount();
        assertTrue(oldCount < newCount);
    }

    /**
     * Test of getData method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetSetData() {
        System.out.println("getData");
        List<Udaj> expResult = new ArrayList<>();
        expResult.add(new Udaj());
        RecordsInsertedModel instance = new RecordsInsertedModel();
        instance.setData(expResult);
        List<Udaj> result = instance.getData();
        assertEquals(expResult, result);
    }

    /**
     * Test of pageUp method, of class RecordsInsertedModel.
     */
    @Test
    public void testPageUp() {
        System.out.println("pageUp");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 105;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        int page_old = instance.pageOffset;
        instance.pageUp();
        int page_new = instance.pageOffset;
        assertEquals(page_old+1, page_new);
    }

    /**
     * Test of pageDown method, of class RecordsInsertedModel.
     */
    @Test
    public void testPageDown() {
        System.out.println("pageDown");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 105;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        instance.pageUp();
        int page_old = instance.pageOffset;
        instance.pageDown();
        int page_new = instance.pageOffset;
        assertEquals(page_old, page_new+1);
    }

    /**
     * Test of getPageSize method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetSetPageSize() {
        System.out.println("getPageSize");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int size = 50;
        instance.setPageSize(size);
        int result = instance.getPageSize();
        assertEquals(size, result);
    }

    /**
     * Test of getRealRowCount method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetRealRowCount() {
        System.out.println("getRealRowCount");
        
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 105;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        int result = instance.getRealRowCount();
        assertEquals(count, result);
    }

    /**
     * Test of goToPage method, of class RecordsInsertedModel.
     */
    @Test
    public void testGoToPage() {
        System.out.println("goToPage");
        int page = 5;
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 705;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        instance.goToPage(page);
        assertEquals(page, instance.pageOffset);
        
        instance.goToPage(10);
        assertEquals(8, instance.pageOffset);
        
    }

    /**
     * Test of getPageCount method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetPageCount() {
        System.out.println("getPageCount");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int count = 1105;
        for (int i = 1; i<=count; i++){
            Udaj u = new Udaj();
            instance.addRow(u);
        }
        int expResult = 12;
        int result = instance.getPageCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPageOffset method, of class RecordsInsertedModel.
     */
    @Test
    public void testGetSetPageOffset() {
        System.out.println("getPageOffset");
        RecordsInsertedModel instance = new RecordsInsertedModel();
        int expResult = 50;
        instance.setPageOffset(expResult);
        int result = instance.getPageOffset();
        assertEquals(expResult, result);
    }
    
}
