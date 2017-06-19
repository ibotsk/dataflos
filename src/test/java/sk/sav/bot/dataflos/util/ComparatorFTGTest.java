/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.Udaj;

/**
 *
 * @author jakub.husar
 */
public class ComparatorFTGTest {
    
    ComparatorFTG instance = new ComparatorFTG();
    
    private Udaj u1;
    private Udaj u2;
    
    public ComparatorFTGTest() {
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
     * Test of compare method, of class ComparatorFTG.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        try {
            u1 = null;
            u2 = null;
            // assert pre null-ovy vstup
            assertEquals(0, instance.compare(u1, u2));
            fail("The test case should not pass.");
        } catch (Exception e){
            assertThat(e, instanceOf(NullPointerException.class));
        }
        u1 = new Udaj();
        u2 = new Udaj();
        assertTrue(instance.compare(u1, u2) == 0);
        u1.setLokality(new Lokality());
        assertTrue(instance.compare(u1, u2) == 0);
        u1.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u1.getLokality(), new Ftgokres(null, "Bukovske vrchy", "13", false, new HashSet())));
        // assigned udaj with not assigned udaj
        assertTrue(instance.compare(u1, u2) < 0);
        u2.setLokality(new Lokality());
        u2.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u2.getLokality(), new Ftgokres(null, "Bukovske vrchy", "13", false, new HashSet())));
        // two equal assigned udajs
        assertTrue(instance.compare(u1, u2) == 0);
        u2.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u2.getLokality(), new Ftgokres(null, "Male Karpaty", "11", false, new HashSet())));
        assertTrue(instance.compare(u1, u2) > 0);
        u1.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u1.getLokality(), new Ftgokres(null, "Pieniny", "7", false, new HashSet())));
        assertTrue(instance.compare(u1, u2) < 0);
        
    }
    
    /**
     * Test of compare method, of class ComparatorFTG.
     */
    @Test
    public void testCompareEqualFtgKvadarnt13() {
        u1 = new Udaj();
        u2 = new Udaj();
        u1.setLokality(new Lokality());
        u1.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u1.getLokality(), new Ftgokres(null, "Bukovske vrchy", "13", false, new HashSet())));
        u2.setLokality(new Lokality());
        u2.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u2.getLokality(), new Ftgokres(null, "Bukovske vrchy", "13", false, new HashSet())));
        // two equal ftgs assigned to udajs, without additional kvadrants assigned
        assertTrue(instance.compare(u1, u2) == 0);
        u1.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u1.getLokality(), new Kvadrant("86072b", null, false, new HashSet())));
        u1.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u1.getLokality(), new Kvadrant("92072d", null, false, new HashSet())));
        u2.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u2.getLokality(), new Kvadrant("91070c", null, false, new HashSet())));
        // assigned new kvadrants, compared by south -> north direction based on 1st, 2nd number and letter
        // u1's 92072d should be the "smallest"
        assertTrue(instance.compare(u1, u2) < 0);  
    }
    
    /**
     * Test of compare method, of class ComparatorFTG.
     */
    @Test
    public void testCompareEqualFtgKvadarnt14d() {
        u1 = new Udaj();
        u2 = new Udaj();
        u1.setLokality(new Lokality());
        u1.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u1.getLokality(), new Ftgokres(null, "Mala Fatra", "14d", false, new HashSet())));
        u2.setLokality(new Lokality());
        u2.getLokality().getLokalityFtgokresAsocs().add(new LokalityFtgokresAsoc(u2.getLokality(), new Ftgokres(null, "Mala Fatra", "14d", false, new HashSet())));
        // two equal ftgs assigned to udajs, without additional kvadrants assigned
        assertTrue(instance.compare(u1, u2) == 0);
        u1.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u1.getLokality(), new Kvadrant("86072b", null, false, new HashSet())));
        u2.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u2.getLokality(), new Kvadrant("91070c", null, false, new HashSet())));
        u2.getLokality().getLokalityKvadrantAsocs().add(new LokalityKvadrantAsoc(u2.getLokality(), new Kvadrant("82075a", null, false, new HashSet())));
        // assigned new kvadrants, based on 14d ftgokres should be compared by west -> east direction based on 3rd, 4th, 5th number and letter
        // u2's 91070c should be the "smallest"
        assertTrue(instance.compare(u1, u2) > 0);  
    }
    
    

    /**
     * Test of getSmallestFtgokres method, of class ComparatorFTG.
     */
    @Test
    public void testGetSmallestFtgokresEmptySet() {
        System.out.println("getSmallestFtgokres");
        Set lokalityFtgokresAsocs = new HashSet();
        assertEquals(null, ComparatorFTG.getSmallestFtgokres(lokalityFtgokresAsocs));
    }
    
    /**
     * Test of getSmallestFtgokres method, of class ComparatorFTG.
     */
    @Test
    public void testGetSmallestFtgokresNonEmptySet() {
        System.out.println("getSmallestFtgokres");
        Ftgokres ftg1 = new Ftgokres(null, "Siatorska bukovinka", "14", false, new HashSet());
        Ftgokres ftg2 = new Ftgokres(null, "Velka Fatra", "12", false, new HashSet());
        Ftgokres ftg3 = new Ftgokres(null, "Nizke Tatry", "26", false, new HashSet());
        Set lokalityFtgokresAsocs = new HashSet();
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg1));
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg2));
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg3));
        assertEquals(ftg2, ComparatorFTG.getSmallestFtgokres(lokalityFtgokresAsocs));
    }
    
    /**
     * Test of getSmallestFtgokres method, of class ComparatorFTG.
     */
    @Test
    public void testGetSmallestFtgokresEqualNumbers() {
        System.out.println("getSmallestFtgokres");
        Ftgokres ftg1 = new Ftgokres(null, "Siatorska bukovinka", "14d", false, new HashSet());
        Ftgokres ftg2 = new Ftgokres(null, "Vihorlat", "17", false, new HashSet());
        Ftgokres ftg3 = new Ftgokres(null, "Nizke Tatry", "26a", false, new HashSet());
        Ftgokres ftg4 = new Ftgokres(null, "Siatorska bukovinka", "14d", false, new HashSet());
        Set lokalityFtgokresAsocs = new HashSet();
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg1));
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg2));
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg3));
        lokalityFtgokresAsocs.add(new LokalityFtgokresAsoc(null, ftg4));
        assertEquals("14d", ComparatorFTG.getSmallestFtgokres(lokalityFtgokresAsocs).getCislo());
    }
    
    
    
}
