/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.factory;

import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.HerbarPolozky;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.UdajObrazky;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import sk.sav.bot.dataflos.util.HibernateQuery;

/**
 *
 * @author jakub.husar
 */
public class UdajFactoryTest {
    
    Session session = null;
    
    public UdajFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //session = HibernateQuery.getInstance("pmereda", "PaloMer*").getSession();
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class UdajFactory.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        UdajFactory result = UdajFactory.getInstance(session);
        assertNotNull(result);
    }

    // TODO: not tested at this moment
//    /**
//     * Test of createUdaj method, of class UdajFactory.
//     */
//    @Test
//    public void testCreateUdaj() {
//        System.out.println("createUdaj");
//        Udaj udaj = null;
//        Lokality lokalita = null;
//        HerbarPolozky polozka = null;
//        List<LitZdrojRev> lits = null;
//        List<SkupRev> revizie = null;
//        SkupRev urcenie = null;
//        List<UdajObrazky> obrazky = null;
//        Set<PeopleAsoc> zberatelia = null;
//        char typ = ' ';
//        String datumZberu = "";
//        String datumSlovom = "";
//        boolean verejny = false;
//        String verejnyDatum = "";
//        String login = "login";
//        UdajFactory instance = UdajFactory.getInstance(session);
//        Udaj expResult = null;
//        Udaj result = instance.createUdaj(udaj, lokalita, polozka, lits, revizie, urcenie, obrazky, zberatelia, typ, datumZberu, datumSlovom, verejny, verejnyDatum, login);
//    }
    
}
