/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsocId;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;

/**
 *
 * @author jakub.husar
 */
public class ListAddRemoveUtilsTest {
    
    public ListAddRemoveUtilsTest() {
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
    
    @Test
    public void test() {
        // TODO
    }

//    /**
//     * Test of setRevisorsAndIdentificators method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetRevisorsAndIdentificators() {
//        System.out.println("setRevisorsAndIdentificators");
//        SkupRev skupRev = null;
//        List<MenaZberRev> people = null;
//        SkupRev expResult = null;
//        SkupRev result = ListAddRemoveUtils.setRevisorsAndIdentificators(skupRev, people);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCollectors method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetCollectors() {
//        System.out.println("setCollectors");
//        Udaj udaj = null;
//        List<MenaZberRev> people = null;
//        Set<PeopleAsoc> expResult = null;
//        Set<PeopleAsoc> result = ListAddRemoveUtils.setCollectors(udaj, people);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setLitAuthors method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetLitAuthors() {
//        System.out.println("setLitAuthors");
//        LitZdroj lzdroj = null;
//        List<MenaZberRev> people = null;
//        LitZdroj expResult = null;
//        LitZdroj result = ListAddRemoveUtils.setLitAuthors(lzdroj, people);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setLitEditors method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetLitEditors() {
//        System.out.println("setLitEditors");
//        LitZdroj lzdroj = null;
//        List<MenaZberRev> people = null;
//        LitZdroj expResult = null;
//        LitZdroj result = ListAddRemoveUtils.setLitEditors(lzdroj, people);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setFtgokresy method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetFtgokresy() {
//        System.out.println("setFtgokresy");
//        Lokality lokalita = new Lokality();
//        List<Ftgokres> ftgs = new ArrayList<>();
//        Ftgokres ftg1 = new Ftgokres();
//        ftg1.setCislo("11");
//        ftg1.setMeno("Ladovieska miska");
//        Ftgokres ftg2 = new Ftgokres();
//        ftg2.setCislo("12");
//        ftg2.setMeno("Medovec");
//        Ftgokres ftg3 = new Ftgokres();
//        ftg3.setCislo("14");
//        ftg3.setMeno("Sulinka");
//        ftgs.add(ftg1);
//        ftgs.add(ftg2);
//        ftgs.add(ftg3);
//        Set<LokalityFtgokresAsoc> ftgokresyAsoc = new HashSet<>(1);
//        for (Ftgokres ftgokres : ftgs) {
//            LokalityFtgokresAsoc lfa = new LokalityFtgokresAsoc(lokalita, ftgokres);
//            lfa.setId(new LokalityFtgokresAsocId(-1, ftgokres.getId()));
//            ftgokresyAsoc.add(lfa);
//        }
//        lokalita.getLokalityFtgokresAsocs().addAll(ftgokresyAsoc);
//        
//        List<Ftgokres> ftgokresy = new ArrayList<>();
//        Ftgokres ftgo1 = new Ftgokres();
//        ftgo1.setCislo("19");
//        ftgo1.setMeno("Ftg new upd");
//        Ftgokres ftgo2 = new Ftgokres();
//        ftg3.setCislo("14");
//        ftg3.setMeno("Sulinka");
//        ftgokresy.add(ftgo1);
//        ftgokresy.add(ftgo2);
//        
//        
//        Lokality result = ListAddRemoveUtils.setFtgokresy(lokalita, ftgokresy);
//        assertEquals(2, result.getLokalityFtgokresAsocs().size());
//    }
//
//    /**
//     * Test of setQuadrants method, of class ListAddRemoveUtils.
//     */
//    @Test
//    public void testSetQuadrants() {
//        System.out.println("setQuadrants");
//        Lokality lokalita = null;
//        List<Kvadrant> quadrants = null;
//        Set<LokalityKvadrantAsoc> expResult = null;
//        Lokality result = ListAddRemoveUtils.setQuadrants(lokalita, quadrants);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
