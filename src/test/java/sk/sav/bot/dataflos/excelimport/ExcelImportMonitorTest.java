/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.excelimport;

import java.beans.PropertyChangeEvent;
import java.io.File;
import javax.swing.JTable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.factory.UdajFactory;
import sk.sav.bot.dataflos.util.HibernateQuery;

/**
 *
 * @author jakub.husar
 */
public class ExcelImportMonitorTest {
    
    ExcelImportMonitor instance;
    
    public ExcelImportMonitorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = ExcelImportMonitor.getInstance();
    }
    
    @After
    public void tearDown() {
        instance.setVisible(false);
    }

    /**
     * Test of getInstance method, of class ExcelImportMonitor.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        assertNotNull(instance);
    }

    /**
     * Test of prepareImport method, of class ExcelImportMonitor.
     */
    @Test
    public void testPrepareImport() throws Exception {
        System.out.println("doImport");
        File excelFile = null;
        HibernateQuery hq = null;
        UdajFactory uf = null;
        instance.setHq(hq);
        // instance.prepareImport(excelFile);
        //assertTrue(instance.isVisible());
        // TODO: add some other tests
    }

    /**
     * Test of propertyChange method, of class ExcelImportMonitor.
     */
    @Test
    public void testPropertyChange() {
        // TODO not implemented
    }

    /**
     * Test of setImportEnabled method, of class ExcelImportMonitor.
     */
    @Test
    public void testSetImportEnabled() {
        System.out.println("setImportEnabled");
        boolean enabled = true;
        instance.setImportEnabled(enabled);
        assertEquals(enabled, instance.btnDoImport.isEnabled());
    }

    /**
     * Test of getImportMonitorTable method, of class ExcelImportMonitor.
     */
    @Test
    public void testGetImportMonitorTable() {
        System.out.println("getImportMonitorTable");
        JTable result = instance.getImportMonitorTable();
        assertNotNull(result);
    }
    
}
