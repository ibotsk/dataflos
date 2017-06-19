/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.factory.ListAddRemoveUtils;
import sk.sav.bot.dataflos.util.HibernateQuery;

/**
 *
 * @author jakub.husar
 */
public class ExportDataFormTest {
    
    String floraFileName;
    
    public ExportDataFormTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        floraFileName = System.getProperty("user.dir")+"\\"+"DATAflosExportFlora_"+reportDate+".docx";
    }
    
    @After
    public void tearDown() {
        File doc = new File(floraFileName);
        doc.delete();
    }

    /**
     * Test of setSimpleLineSpacing method, of class ExportDataForm.
     */
    @Test
    public void testSetSimpleLineSpacing() {
        // System.out.println("setSimpleLineSpacing");
        // TODO not tested yet
    }

    /**
     * Test of displayForm method, of class ExportDataForm.
     */
    @Test
    public void testDisplayForm() {
//        System.out.println("displayForm");
//        ExportDataForm instance = new ExportDataForm(null);
//        instance.displayForm();
//        assertTrue(instance.isVisible());
//        instance.setVisible(false);
    }
    
    /**
     * Test of displayForm method, of class ExportDataForm.
     */
    @Test
    public void testWordFlora() throws FileNotFoundException, IOException, InvalidFormatException {
//        System.out.println("displayForm");
//        ExportDataForm instance = new ExportDataForm(null);
//        List<Udaj> udaje = new ArrayList<Udaj>();
//        Udaj udaj = new Udaj();
//        udaj.setTyp('H');
//        Lokality lokalita = new Lokality();
//        Ftgokres okres = new Ftgokres();
//        okres.setCislo("11");
//        okres.setMeno("Môj ftg okresík");
//        List<Ftgokres> okresy = new ArrayList<Ftgokres>();
//        okresy.add(okres);
//        lokalita = ListAddRemoveUtils.setFtgokresy(lokalita, okresy);
//        lokalita.setOpisLokality("blízko Marienovskej tiesňavy");
//        udaj.setLokality(lokalita);
//        udaje.add(udaj);
//        
//        String fileName = floraFileName;
//        instance.writeToFlora(fileName, udaje);
//        File file = new File(fileName);
//        assertTrue(file.exists());
    }
    
}
