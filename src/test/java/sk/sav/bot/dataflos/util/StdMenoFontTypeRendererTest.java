/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import sk.sav.bot.dataflos.gui.FilterJList;

/**
 *
 * @author jakub.husar
 */
public class StdMenoFontTypeRendererTest {
    
    public StdMenoFontTypeRendererTest() {
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
     * Test of getListCellRendererComponent method, of class StdMenoFontTypeRenderer.
     */
    @Test
    public void testGetListCellRendererComponent() {
        System.out.println("getListCellRendererComponent");
        
        StdMenoFontTypeRenderer instance = new StdMenoFontTypeRenderer();
        JList listMoznosti = new JList(new DefaultListModel());
        DefaultListModel<AssociableEntity> model1 = (DefaultListModel<AssociableEntity>) listMoznosti.getModel();
        
        MenaZberRev mzr = new MenaZberRev();
        model1.addElement(mzr);
        JLabel result = (JLabel) instance.getListCellRendererComponent(listMoznosti, mzr, 0, false, false);
        assertEquals(Color.BLACK, result.getForeground());
        assertTrue(result.getFont().isPlain());
        
        ListOfSpecies losNotSchv = new ListOfSpecies();
        losNotSchv.setSchvalene(false);
        model1.addElement(losNotSchv);
        JLabel result2 = (JLabel) instance.getListCellRendererComponent(listMoznosti, losNotSchv, 1, false, false);
        assertEquals(new Color(163,163,163), result2.getForeground());
        assertTrue(result2.getFont().isPlain());
        
        ListOfSpecies losSynSchv = new ListOfSpecies();
        losSynSchv.setTyp('S');
        losSynSchv.setSchvalene(true);
        model1.addElement(losSynSchv);
        JLabel result3 = (JLabel) instance.getListCellRendererComponent(listMoznosti, losSynSchv, 3, false, false);
        assertEquals(new Color(85,128,59), result3.getForeground());
        assertTrue(result3.getFont().isPlain());
        
        ListOfSpecies losAkcSchv = new ListOfSpecies();
        losAkcSchv.setTyp('A');
        losAkcSchv.setSchvalene(true);
        model1.addElement(losAkcSchv);
        JLabel result4 = (JLabel) instance.getListCellRendererComponent(listMoznosti, losAkcSchv, 4, false, false);
        assertEquals(new Color(85,128,59), result4.getForeground());
        assertTrue(result4.getFont().isBold());
        
        JLabel result5 = (JLabel) instance.getListCellRendererComponent(listMoznosti, null, 5, false, false);
        assertEquals(Color.BLACK, result5.getForeground());
        assertTrue(result5.getFont().isPlain());
    }
    
}
