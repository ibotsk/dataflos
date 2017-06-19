/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.Brumit4;
import sk.sav.bot.dataflos.entity.Casopisy;
import sk.sav.bot.dataflos.entity.DallaTorre;
import sk.sav.bot.dataflos.entity.FamilyApg;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Genus;
import sk.sav.bot.dataflos.entity.Herbar;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.Obec;
import sk.sav.bot.dataflos.entity.TaxonEndemizmus;
import sk.sav.bot.dataflos.entity.TaxonOhrozenost;
import sk.sav.bot.dataflos.entity.TaxonPochybnost;
import sk.sav.bot.dataflos.entity.TaxonPovodnost;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.Entity;

/**
 *
 * @author jakub.husar
 */
public class HibernateQueryTest {
    
    HibernateQuery hq = HibernateQuery.getInstance("pmereda", "PaloMer*");
    
    public HibernateQueryTest() {
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
     * Test of getInstance method, of class HibernateQuery.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        // not null
        assertNotNull(hq);
        // always the same instance
        assertEquals(hq, HibernateQuery.getInstance("pmereda", "PaloMer*"));
    }

    /**
     * Test of getSession method, of class HibernateQuery.
     */
    @Test
    public void testGetSession() {
        System.out.println("getSession");
        // should be not null
        assertNotNull(hq.getSession());
    }

    /**
     * Test of isAdmin method, of class HibernateQuery.
     */
    @Test
    public void testIsAdmin() {
        System.out.println("isAdmin");
        // for this hq instance, should be true
        assertEquals(true, hq.isAdmin());
    }

    /**
     * Test of getAllRecords method, of class HibernateQuery.
     */
    @Test
    public void testGetAllRecords_String() {
        System.out.println("getAllRecords");
        
        List<Entity> udajList = hq.getAllRecords("Udaj");
        assertNotNull(udajList);
        assertTrue(udajList.size() > 0);
        Entity ent = udajList.get(0);
        assertThat(ent, instanceOf(Udaj.class));
        Udaj u = (Udaj) ent;
        assertTrue(u.getId() > 0);
        assertTrue(((Udaj)udajList.get(1)).getId() < ((Udaj)udajList.get(2)).getId());
        
        assertTrue(hq.getAllRecords("MenaZberRev").size() > 0);
        assertTrue(hq.getAllRecords("LitZdroj").size() > 0);
    }

    /**
     * Test of getAllRecords method, of class HibernateQuery.
     */
    @Test
    public void testGetAllRecords_String_String() {
        System.out.println("getAllRecords");
        
        List<Entity> casopisyList = hq.getAllRecords("Casopisy", "meno");
        assertNotNull(casopisyList);
        assertTrue(casopisyList.size() > 0);
        Entity ent = casopisyList.get(0);
        assertThat(ent, instanceOf(Casopisy.class));
        Casopisy casopis = (Casopisy) ent;
        assertNotNull(casopis.getMeno());
        assertTrue(((Casopisy)casopisyList.get(0)).getMeno().compareTo(((Casopisy)casopisyList.get(10)).getMeno()) < 0);
        List<Entity> ftgList = hq.getAllRecords("Ftgokres", "cislo");
        assertTrue(((Ftgokres)ftgList.get(0)).getCislo().compareTo(((Ftgokres)ftgList.get(10)).getCislo()) < 0);
        List<Entity> dtList = hq.getAllRecords("DallaTorre", "id");
        assertTrue(((DallaTorre)dtList.get(0)).getIdEvid().compareTo(((DallaTorre)dtList.get(10)).getIdEvid()) < 0);
    }

    /**
     * Test of getUdajAll method, of class HibernateQuery.
     */
    @Test
    public void testGetUdajAll() {
        System.out.println("getUdajAll");
        
        List<Udaj> udajs = hq.getUdajAll();
        assertNotNull(udajs);
        for (int i = 0; i < 100; i++){
            assertTrue(udajs.get((int) (Math.random() * udajs.size())).isVerejnePristupny() || hq.isAdmin());
        }
    }

    /**
     * Test of getUdajAllCount method, of class HibernateQuery.
     */
    @Test
    public void testGetUdajAllCount() {
        System.out.println("getUdajAllCount");
        long udajCount = hq.getUdajAllCount();
        assertTrue(udajCount > 100000);
        assertEquals(udajCount, hq.getUdajAll().size());
    }

    /**
     * Test of getUdajInsertedBy method, of class HibernateQuery.
     */
    @Test
    public void testGetUdajInsertedBy() {
        System.out.println("getUdajInsertedBy");
        List<Udaj> udajs = hq.getUdajInsertedBy();
        for (int i = 0; i < 100; i++){
            assertEquals(udajs.get(i).getUzivatel(), hq.getLogin());
        }
    }

    /**
     * Test of getAllMyRecordsCount method, of class HibernateQuery.
     */
    @Test
    public void testGetAllMyRecordsCount() {
        System.out.println("getAllMyRecordsCount");
        long myUdajCount = hq.getAllMyRecordsCount();
        assertTrue(myUdajCount >= 0);
        assertTrue(hq.getUdajAllCount() >= myUdajCount);
    }

    /**
     * Test of getDataFromTableColumn method, of class HibernateQuery.
     */
    @Test
    public void testGetDataFromTableColumn() {
        System.out.println("getDataFromTableColumn");
        List<String> columnData = hq.getDataFromTableColumn("Herbar", "skratkaHerb");
        for (int i = 0; i < 10; i++){
            int nth = (int) (Math.random() * columnData.size()-1);
            String skratka1 = columnData.get(nth).toUpperCase();
            String skratka2 = columnData.get(nth+1).toUpperCase();
            assertTrue("Next (" + skratka2 + ") should be greater than current (" + skratka1 + ")", skratka1.compareTo(skratka2) <= 0);
        }
    }

    /**
     * Test of getLastUdaj method, of class HibernateQuery.
     */
    @Test
    public void testGetLastUdaj() {
        System.out.println("getLastUdaj");
        Udaj lastUdaj = hq.getLastUdaj();
        assertTrue(lastUdaj.getId() >= hq.getUdajAllCount());
        List<Udaj> udajs = hq.getUdajAll();
        for (int i = 0; i < 100; i++){
            assertTrue(lastUdaj.getId() >= udajs.get((int) (Math.random() * udajs.size())).getId());
        }
    }

    /**
     * Test of getMyLastUdaj method, of class HibernateQuery.
     */
    @Test
    public void testGetMyLastUdaj() {
        System.out.println("getMyLastUdaj");
        Udaj myLastUdaj = hq.getMyLastUdaj();
        assertTrue(myLastUdaj.getId() >= hq.getAllMyRecordsCount());
        List<Udaj> myUdajs = hq.getUdajInsertedBy();
        for (int i = 0; i < 100; i++){
            assertTrue(myLastUdaj.getId() >= myUdajs.get((int) (Math.random() * myUdajs.size())).getId());
        }
    }

    /**
     * Test of getNthLastUdaj method, of class HibernateQuery.
     */
    @Test
    public void testGetNthLastUdaj() {
        System.out.println("getNthLastUdaj");
        long count = hq.getUdajAllCount();
        for (int i = 0; i < 10; i++){
            int nth = (int) (Math.random() * count-1);
            // n-ty vs (n+1)-vy najnovsi udaj - celkovy
            Udaj udaj1 = hq.getNthLastUdaj(nth);
            Udaj udaj2 = hq.getNthLastUdaj(nth+1);
            assertTrue(udaj1.getId() > udaj2.getId());
        }
    }

    /**
     * Test of getMyNthLastUdaj method, of class HibernateQuery.
     */
    @Test
    public void testGetMyNthLastUdaj() {
        System.out.println("getMyNthLastUdaj");
        long count = hq.getAllMyRecordsCount();
        if (count > 1){
            for (int i = 0; i < 10; i++){
                int nth = (int) (Math.random() * count-1);
                // n-ty vs (n+1)-vy najnovsi udaj patriaci user-ovi
                Udaj udaj1 = hq.getMyNthLastUdaj(nth);
                Udaj udaj2 = hq.getMyNthLastUdaj(nth+1);
                assertTrue(udaj1.getId() > udaj2.getId());
            }
        }
    }

    /**
     * Test of getHerbarBySkratka method, of class HibernateQuery.
     */
    @Test
    public void testGetHerbarBySkratka() {
        System.out.println("getHerbarBySkratka");
        String skratka = "BRA";
        assertEquals(skratka, hq.getHerbarBySkratka(skratka).getSkratkaHerb());
    }

    /**
     * Test of getById method, of class HibernateQuery.
     */
    @Test
    public void testGetById() {
        System.out.println("getById");
        int id = 26;
        Object ent = hq.getById(Udaj.class, id);
        assertThat(ent, instanceOf(Udaj.class));
        assertEquals(id, ((Udaj) hq.getById(Udaj.class, id)).getId());
    }

    /**
     * Test of insertOrUpdateEntity method, of class HibernateQuery.
     */
    @Test
    public void testInsertOrUpdateEntity() {
        // not tested yet
    }

    /**
     * Test of insertEntities method, of class HibernateQuery.
     */
    @Test
    public void testInsertEntities() {
        // not tested yet
    }

    /**
     * Test of deleteEntity method, of class HibernateQuery.
     */
    @Test
    public void testDeleteEntity() {
        // not tested yet
    }

    /**
     * Test of getUdajIdPosition method, of class HibernateQuery.
     */
    @Test
    public void testGetUdajIdPosition() {
        System.out.println("getUdajIdPosition");
        assertEquals(1, hq.getUdajIdPosition(1));
        assertEquals(hq.getUdajAllCount(), hq.getUdajIdPosition(hq.getLastUdaj().getId()));
        int count = (int) hq.getUdajAllCount();
        int nth = (int) (Math.random()*count);
        assertEquals(count-nth, hq.getUdajIdPosition(hq.getNthLastUdaj(nth).getId()));
    }

    /**
     * Test of getMyUdajIdPosition method, of class HibernateQuery.
     */
    @Test
    public void testGetMyUdajIdPosition() {
        System.out.println("getMyUdajIdPosition");
        assertEquals(hq.getAllMyRecordsCount(), hq.getMyUdajIdPosition(hq.getMyLastUdaj().getId()));
        int count = (int) hq.getAllMyRecordsCount();
        int nth = (int) (Math.random()*count);
        assertEquals(count-nth, hq.getMyUdajIdPosition(hq.getMyNthLastUdaj(nth).getId()));
    }

    /**
     * Test of filterByCriteria method, of class HibernateQuery.
     */
    @Test
    public void testFilterByCriteria() {
        System.out.println("filterByCriteria");
        String idUdaj = "";
        String cisloPolozky = "";
        String ciarKod = "";
        Herbar herbar;
        MenaZberRev autorZberu = null;
        Brumit4 stat = null;
        Ftgokres ftg = null;
        Obec obec = null;
        Kvadrant kvadrant = null;
        String rok = "";
        Casopisy casopis = null;
        MenaZberRev autorPublikacie = null;
        ListOfSpecies los = null;
        String taxonMatch = "";
        Genus genus = null;
        FamilyApg familyAPG = null;
        String recNumber = "";
        boolean poslRev = false;
        boolean ochrana = false;
        TaxonOhrozenost ohrozenost = null;
        TaxonEndemizmus endemizmus = null;
        TaxonPovodnost povodnost = null;
        boolean myRecords = false;
        
        herbar = hq.getHerbarBySkratka("BRA");
        
        List<Udaj> filteredList = hq.filterByCriteria(idUdaj, cisloPolozky, ciarKod, herbar, autorZberu, stat, ftg, obec, kvadrant, rok, casopis, autorPublikacie, los, taxonMatch, genus, familyAPG, recNumber, poslRev, ochrana, ohrozenost, endemizmus, povodnost, myRecords);
        assertEquals(herbar, filteredList.get((int) (Math.random() * filteredList.size())).getHerbarPolozky().getHerbar());
    }
    
}
