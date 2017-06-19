/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.util.List;
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
public class DisplayEntitiesTest {
    
    public DisplayEntitiesTest() {
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
     * Test of getInstance method, of class DisplayEntities.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        assertNotNull(DisplayEntities.getInstance());
    }

    /**
     * Test of getEntitiesToDisplay method, of class DisplayEntities.
     */
    @Test
    public void testGetEntitiesToDisplay() {
        System.out.println("getEntitiesToDisplay");
        DisplayEntities instance = DisplayEntities.getInstance();
        List<String> dispEntList = instance.getEntitiesToDisplay();
        assertEquals(false, dispEntList.contains("MoznostPohybu"));
        assertEquals(false, dispEntList.contains("Brumit5"));
        assertEquals(false, dispEntList.contains("Udaj"));
        assertEquals(true, dispEntList.contains("TaxonPovodnost"));
        assertEquals(true, dispEntList.contains("MenaZberRev"));
        assertEquals(true, dispEntList.contains("Casopisy"));
    }
    
}
