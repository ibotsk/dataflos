/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
public class TableColumnsWidthCounterTest {
    
    public TableColumnsWidthCounterTest() {
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
     * Test of setWidthOfColumns method, of class TableColumnsWidthCounter.
     */
    @Test
    public void testSetWidthOfColumns() {
        System.out.println("setWidthOfColumns");
        JTable table = new JTable(new DefaultTableModel());
        table.setSize(400, 300);
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.addColumn("ID");
        dtm.addColumn("Nazov");
        dtm.addColumn("Komplexne riesenie");
        TableColumnsWidthCounter.setWidthOfColumns(table);
        assertTrue(table.getColumnModel().getColumn(0).getPreferredWidth() < table.getColumnModel().getColumn(1).getPreferredWidth());
        assertTrue(table.getColumnModel().getColumn(1).getPreferredWidth() < table.getColumnModel().getColumn(2).getPreferredWidth());
        
        dtm.addRow(new Object[]{"112", "Mienkotvorny predvolebny buletin", "Zase nic"});
        TableColumnsWidthCounter.setWidthOfColumns(table);
        assertTrue(table.getColumnModel().getColumn(0).getPreferredWidth() < table.getColumnModel().getColumn(1).getPreferredWidth());
        assertTrue(table.getColumnModel().getColumn(1).getPreferredWidth() > table.getColumnModel().getColumn(2).getPreferredWidth());
    }
    
}
