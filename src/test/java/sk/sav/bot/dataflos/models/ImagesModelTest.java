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
import sk.sav.bot.dataflos.entity.UdajObrazky;

/**
 *
 * @author jakub.husar
 */
public class ImagesModelTest {
    
    public ImagesModelTest() {
    }

    /**
     * Test of getImages method, of class ImagesModel.
     */
    @Test
    public void testGetSetImages() {
        System.out.println("getImages");
        List<UdajObrazky> images = new ArrayList<>();
        UdajObrazky uo = new UdajObrazky();
        uo.setPopis("descript");
        images.add(uo);
        ImagesTableModel instance = new ImagesTableModel();
        instance.setImages(images);
        List<UdajObrazky> result = instance.getImages();
        assertEquals(images, result);
    }

    /**
     * Test of getRowCount method, of class ImagesModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        ImagesTableModel instance = new ImagesTableModel();
        int expResult = 0;
        int result = instance.getRowCount();
        assertEquals(expResult, result);
        
        UdajObrazky uo = new UdajObrazky();
        uo.setPopis("descript");
        ImagesTableModel instance2 = new ImagesTableModel();
        instance2.getImages().add(uo);
        int expResult2 = 1;
        int result2 = instance2.getRowCount();
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getColumnCount method, of class ImagesModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        ImagesTableModel instance = new ImagesTableModel();
        int expResult = 3;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueAt method, of class ImagesModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        UdajObrazky uo = new UdajObrazky();
        String expResult = "descript";
        uo.setPopis(expResult);
        int row = 0;
        int col = 1;
        ImagesTableModel instance = new ImagesTableModel();
        instance.addRow(uo);
        Object result = instance.getValueAt(row, col);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnName method, of class ImagesModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int col = 1;
        ImagesTableModel instance = new ImagesTableModel();
        String expResult = "Popis";
        String result = instance.getColumnName(col);
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnClass method, of class ImagesModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        int columnIndex = 0;
        ImagesTableModel instance = new ImagesTableModel();
        Class expResult = UdajObrazky.class;
        Class result = instance.getColumnClass(columnIndex);
        assertEquals(expResult, result);
    }
    
}
