/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.MenaTaxonov;
import sk.sav.bot.dataflos.entity.SkupRev;

/**
 *
 * @author jakub.husar
 */
public class RevisionsModelTest {
    
    RevisionsTableModel model;
    List<SkupRev> list;
    SkupRev sr;
    
    public RevisionsModelTest() {
        sr = new SkupRev();
        sr.setDatum("12. 4. 2008");
        sr.setMenaTaxonov(new MenaTaxonov(null, "Meno taxona", null, null));
        sr.setSkupRevDets(new HashSet<>(Arrays.asList("Botanik")));
        SkupRev sr2 = new SkupRev();
        sr2.setDatum("14. 10. 1908");
        sr2.setMenaTaxonov(new MenaTaxonov(null, "Neakcept Meno taxona", null, null));
        sr2.setSkupRevDets(new HashSet<>(Arrays.asList("Amater")));
        list = new ArrayList();
        list.add(sr);
        list.add(sr2);
        model = new RevisionsTableModel(list);
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
     * Test of getRevisions method, of class RevisionsModel.
     */
    @Test
    public void testGetRevisions() {
        System.out.println("getRevisions");
        assertEquals(sr, model.getRevisions().get(0));
    }

    /**
     * Test of setRevisions method, of class RevisionsModel.
     */
    @Test
    public void testSetRevisions() {
        System.out.println("setDispEntities");
        model.setRevisions(null);
        try {
            model.getRevisions().get(0);
            fail("Should not pass");
        } catch (Exception e){
            assertThat(e, instanceOf(NullPointerException.class));
        }
        model.setRevisions(list);
        assertNotNull(model.getRevisions().get(0));
    }

    /**
     * Test of getRowCount method, of class RevisionsModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        assertEquals(2, model.getRowCount());
    }

    /**
     * Test of getColumnCount method, of class RevisionsModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        assertEquals(3, model.getColumnCount());
    }

    /**
     * Test of getValueAt method, of class RevisionsModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        assertEquals("Botanik", model.getValueAt(0, 2));
    }

    /**
     * Test of getColumnName method, of class RevisionsModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        assertEquals("Dátum", model.getColumnName(0));
        assertEquals("Revidovatelia", model.getColumnName(2));
    }

    /**
     * Test of getColumnClass method, of class RevisionsModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        assertEquals(SkupRev.class, model.getColumnClass(0));
        assertEquals(String.class, model.getColumnClass(1));
    }
    
}
