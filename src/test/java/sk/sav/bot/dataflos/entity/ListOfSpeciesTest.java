/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class ListOfSpeciesTest {
    
    public ListOfSpeciesTest() {
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
     * Test of getGenus method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetGenus() {
        System.out.println("getGenus");
        ListOfSpecies instance = new ListOfSpecies();
        Genus expGenus = new Genus(null, null, "meno", "", true, null, null);
        instance.setGenus(expGenus);
        Genus result = instance.getGenus();
        assertEquals(expGenus.getMeno(), result.getMeno());
    }

    /**
     * Test of getListOfSpecies method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetListOfSpecies() {
        System.out.println("getListOfSpecies");
        
        ListOfSpecies instance = new ListOfSpecies();
        ListOfSpecies innerLOS = new ListOfSpecies();
        instance.setAcceptedName(innerLOS);
        ListOfSpecies result = instance.getAcceptedName();
        assertEquals(innerLOS, result);
    }

    /**
     * Test of getTyp method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetTyp() {
        System.out.println("getTyp");
        Character typ = 'A';
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTyp(typ);
        Character result = instance.getTyp();
        assertEquals(typ, result);
    }

    /**
     * Test of getMeno method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetMeno() {
        System.out.println("getMeno");
        String meno = "LOS Meno";
        ListOfSpecies instance = new ListOfSpecies();
        instance.setMeno(meno);
        String result = instance.getMeno();
        assertEquals(meno, result);
    }

    /**
     * Test of getAutori method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetAutori() {
        System.out.println("getAutori");
        String autori = "Autor meno";
        ListOfSpecies instance = new ListOfSpecies();
        instance.setAutori(autori);
        String result = instance.getAutori();
        assertEquals(autori, result);
    }

    /**
     * Test of isSchvalene method, of class ListOfSpecies.
     */
    @Test
    public void testIsSetSchvalene() {
        System.out.println("isSchvalene");
        ListOfSpecies instance = new ListOfSpecies();
        instance.setSchvalene(true);
        boolean result = instance.isSchvalene();
        assertEquals(true, result);
    }

    /**
     * Test of getListOfSpecieses method, of class ListOfSpecies.
     */
    @Test
    public void testGetListOfSpecieses() {
        System.out.println("getListOfSpecieses");
        Set loses = new HashSet<>();
        loses.add(new ListOfSpecies());
        loses.add(new ListOfSpecies());
        ListOfSpecies instance = new ListOfSpecies();
        instance.setListOfSynonyms(loses);
        Set result = instance.getListOfSynonyms();
        assertEquals(2, result.size());
    }

    /**
     * Test of getMenaTaxonovs method, of class ListOfSpecies.
     */
    @Test
    public void testGetMenaTaxonovs() {
        System.out.println("getMenaTaxonovs");
        Set mts = new HashSet<>();
        mts.add(new MenaTaxonov());
        mts.add(new MenaTaxonov());
        mts.add(new MenaTaxonov());
        ListOfSpecies instance = new ListOfSpecies();
        instance.setMenaTaxonovs(mts);
        Set result = instance.getMenaTaxonovs();
        assertEquals(3, result.size());
    }

    /**
     * Test of getTaxonPochybnost method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetTaxonPochybnost() {
        System.out.println("getTaxonPochybnost");
        TaxonPochybnost expResult = new TaxonPochybnost();
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonPochybnost(expResult);
        TaxonPochybnost result = instance.getTaxonPochybnost();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTaxonOhrozenost method, of class ListOfSpecies.
     */
    @Test
    public void testGetSetTaxonOhrozenost() {
        System.out.println("getTaxonOhrozenost");
        TaxonOhrozenost expResult = new TaxonOhrozenost();
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonOhrozenost(expResult);
        TaxonOhrozenost result = instance.getTaxonOhrozenost();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTaxonEndemizmus method, of class ListOfSpecies.
     */
    @Test
    public void testGetTaxonEndemizmus() {
        System.out.println("getTaxonEndemizmus");
        TaxonEndemizmus expResult = new TaxonEndemizmus();
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonEndemizmus(expResult);
        TaxonEndemizmus result = instance.getTaxonEndemizmus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTaxonPovodnost method, of class ListOfSpecies.
     */
    @Test
    public void testGetTaxonPovodnost() {
        System.out.println("getTaxonPovodnost");
        TaxonPovodnost expResult = new TaxonPovodnost();
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonPovodnost(expResult);
        TaxonPovodnost result = instance.getTaxonPovodnost();
        assertEquals(expResult, result);
    }

    /**
     * Test of isTaxonOchrana method, of class ListOfSpecies.
     */
    @Test
    public void testIsTaxonOchrana() {
        System.out.println("isTaxonOchrana");
        boolean expResult = false;
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonOchrana(expResult);
        boolean result = instance.isTaxonOchrana();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTaxonSvkNazov method, of class ListOfSpecies.
     */
    @Test
    public void testGetTaxonSvkNazov() {
        System.out.println("getTaxonSvkNazov");
        String expResult = "Svk Nazov";
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonSvkNazov(expResult);
        String result = instance.getTaxonSvkNazov();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ListOfSpecies.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String meno = "Los meno";
        String autori = null;
        ListOfSpecies instance = new ListOfSpecies();
        instance.setMeno(meno);
        instance.setAutori(autori);
        String result = instance.toString();
        assertEquals(meno, result);

        autori = "Autor, A.";
        instance.setAutori(autori);
        result = instance.toString();
        assertEquals(meno+" "+autori, result);
        
    }

    /**
     * Test of namesAndValues method, of class ListOfSpecies.
     */
    @Test
    public void testNamesAndValues() {
        System.out.println("namesAndValues");
        String nAvTP = "Pov.Skr.";
        TaxonPovodnost taxonPov = new TaxonPovodnost();
        taxonPov.setSkratka(nAvTP);
        ListOfSpecies instance = new ListOfSpecies();
        instance.setTaxonPovodnost(taxonPov);
        List<String[]> result = instance.namesAndValues();
        assertTrue(result != null);
        assertTrue(result.size()>0);
        assertEquals(12, result.size());
        assertEquals(nAvTP, result.get(9)[1]);
    }
    
}
