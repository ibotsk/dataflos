/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.util.DisplayEntities;

/**
 *
 * @author jakub.husar
 */
public class TableListModelTest {
    
    TableListModel model = new TableListModel(DisplayEntities.getInstance());
    
    public TableListModelTest() {
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
     * Test of setDispEntities method, of class TableListModel.
     */
    @Test
    public void testSetDispEntities() {
        System.out.println("setDispEntities");
        model.setDispEntities(null);
        try {
            model.getElementAt(0);
            fail("Should not pass");
        } catch (Exception e){
            assertThat(e, instanceOf(NullPointerException.class));
        }
        model.setDispEntities(DisplayEntities.getInstance());
        assertNotNull(model.getElementAt(0));
        
    }

    /**
     * Test of getSize method, of class TableListModel.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        assertTrue(model.getSize() > 20);
    }

    /**
     * Test of getElementAt method, of class TableListModel.
     */
    @Test
    public void testGetElementAt() {
        System.out.println("getElementAt");
        assertEquals("Brumit1", model.getElementAt(0));
    }
    
}
