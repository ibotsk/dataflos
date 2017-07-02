/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.Family;
import sk.sav.bot.dataflos.entity.Genus;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 *
 * @author jakub.husar
 */
public class PagingModelTest {
    
    PagingModel instance;
    
    public PagingModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<AssociableEntity> entity = new ArrayList<>();
        List<Genus> genuses = new ArrayList<>();
        Genus genus = new Genus();
        genus.setAutor("Autor rodu");
        genuses.add(genus);
        entity.addAll(genuses);
        instance = new PagingModel(entity);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRowCount method, of class PagingModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        int expResult = 1;
        int result = instance.getRowCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnCount method, of class PagingModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        int expResult = 6; //nAv size+1 of genuses
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueAt method, of class PagingModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        int row = 0;
        int col = 1;
        Object expResult = "Autor rodu";
        Object result = instance.getValueAt(row, col);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnName method, of class PagingModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int col = 2;
        String expResult = "Čeľaď";
        String result = instance.getColumnName(col);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnClass method, of class PagingModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        int columnIndex = 2;
        Class expResult = String.class;
        Class result = instance.getColumnClass(columnIndex);
        assertEquals(expResult, result);
        
        int columnIndex2 = 4;
        Class expResult2 = ImageIcon.class;
        Class result2 = instance.getColumnClass(columnIndex2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getPageOffset method, of class PagingModel.
     */
    @Test
    public void testGetPageOffset() {
        System.out.println("getPageOffset");
        int expResult = 0;
        int result = instance.getPageOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPageCount method, of class PagingModel.
     */
    @Test
    public void testGetPageCount() {
        System.out.println("getPageCount");
        int expResult = 1;
        int result = instance.getPageCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getData method, of class PagingModel.
     */
    @Test
    public void testGetSetData() {
        System.out.println("getData");
        List<AssociableEntity> expResult = new ArrayList<>();
        expResult.add(new LitZdroj());
        expResult.add(new LitZdroj());
        instance.setData(expResult);
        List<AssociableEntity> result = instance.getData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRealRowCount method, of class PagingModel.
     */
    @Test
    public void testGetRealRowCount() {
        System.out.println("getRealRowCount");
        int expResult = 1;
        int result = instance.getRealRowCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPageSize method, of class PagingModel.
     */
    @Test
    public void testGetSetPageSize() {
        System.out.println("getPageSize");
        int pageSize = 20;
        instance.setPageSize(pageSize);
        int result = instance.getPageSize();
        assertEquals(pageSize, result);
    }

    /**
     * Test of goToPage method, of class PagingModel.
     */
    @Test
    public void testGoToPage() {
        System.out.println("goToPage");
        int page = 2;
        List<AssociableEntity> entity = new ArrayList<>();
        List<Genus> genuses = new ArrayList<>();
        for (int i=0; i<320; i++){
            Genus genus = new Genus();
            genus.setAutor("Autor rodu");
            genuses.add(genus);
        }
        entity.addAll(genuses);
        instance = new PagingModel(entity);
        
        instance.setPageSize(100);
        instance.goToPage(page);
        assertEquals(page, instance.pageOffset);
        
        instance.goToPage(10);
        assertEquals(4, instance.pageOffset);
    }

    /**
     * Test of pageDown method, of class PagingModel.
     */
    @Test
    public void testPageDown() {
        System.out.println("pageDown");
        List<AssociableEntity> entity = new ArrayList<>();
        List<Genus> genuses = new ArrayList<>();
        for (int i=0; i<320; i++){
            Genus genus = new Genus();
            genus.setAutor("Autor rodu");
            genuses.add(genus);
        }
        entity.addAll(genuses);
        instance = new PagingModel(entity);
        
        instance.setPageSize(100);
        instance.pageUp();
        int page_old = instance.pageOffset;
        instance.pageDown();
        int page_new = instance.pageOffset;
        assertEquals(page_old, page_new+1);
    }

    /**
     * Test of pageUp method, of class PagingModel.
     */
    @Test
    public void testPageUp() {
        System.out.println("pageUp");
        List<AssociableEntity> entity = new ArrayList<>();
        List<Genus> genuses = new ArrayList<>();
        for (int i=0; i<320; i++){
            Genus genus = new Genus();
            genus.setAutor("Autor rodu");
            genuses.add(genus);
        }
        entity.addAll(genuses);
        instance = new PagingModel(entity);
        
        instance.setPageSize(100);
        int page_old = instance.pageOffset;
        instance.pageUp();
        int page_new = instance.pageOffset;
        assertEquals(page_old+1, page_new);
        
    }
    
}
