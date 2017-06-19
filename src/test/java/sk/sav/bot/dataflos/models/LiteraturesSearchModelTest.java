/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.interf.Entity;

/**
 *
 * @author jakub.husar
 */
public class LiteraturesSearchModelTest {
    
    public LiteraturesSearchModelTest() {
    }

    /**
     * Test of getLiteratures method, of class LiteraturesSearchModel.
     */
    @Test
    public void testGetSetLiteratures() {
        System.out.println("getLiteratures");
        
        List<Entity> literatures = new ArrayList<>();
        LitZdroj lz = new LitZdroj();
        lz.setRocnik("12");
        literatures.add(lz);
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        instance.setLiteratures(literatures);
        List<Entity> result = instance.getLiteratures();
        assertEquals(literatures, result);
    }

    /**
     * Test of getRowCount method, of class LiteraturesSearchModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        int expResult = 0;
        int result = instance.getRowCount();
        assertEquals(expResult, result);
        
        LitZdroj lz = new LitZdroj();
        lz.setRok("1243");
        LiteraturesSearchModel instance2 = new LiteraturesSearchModel();
        instance2.addRow(lz);
        int expResult2 = 1;
        int result2 = instance2.getRowCount();
        assertEquals(expResult2, result2);
        
    }

    /**
     * Test of getColumnCount method, of class LiteraturesSearchModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        int expResult = 5;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueAt method, of class LiteraturesSearchModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        String expResult = "1987";
        int row = 0;
        int column = 2;
        LitZdroj lz = new LitZdroj();
        lz.setRok(expResult);
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        instance.addRow(lz);
        Object result = instance.getValueAt(row, column);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnName method, of class LiteraturesSearchModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int column = 2;
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        String expResult = "Rok vydania";
        String result = instance.getColumnName(column);
        assertEquals(expResult, result);
    }

    /**
     * Test of addRow method, of class LiteraturesSearchModel.
     */
    @Test
    public void testAddRow() {
        System.out.println("addRow");
        Entity ent = null;
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        int startCount = instance.getLiteratures().size();
        instance.addRow(ent);
        assertTrue(startCount < instance.getLiteratures().size());
    }

    /**
     * Test of setEmpty method, of class LiteraturesSearchModel.
     */
    @Test
    public void testSetEmpty() {
        System.out.println("setEmpty");
        LiteraturesSearchModel instance = new LiteraturesSearchModel();
        instance.setEmpty();
        assertTrue(instance.getLiteratures().isEmpty());
    }
    
}
