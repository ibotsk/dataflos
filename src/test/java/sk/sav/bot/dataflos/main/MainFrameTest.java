/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.main;

import java.awt.AWTException;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.sav.bot.dataflos.entity.HerbarPolozky;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.UdajObrazky;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import static sk.sav.bot.dataflos.main.MainFrameTest.instance;

/**
 *
 * @author jakub.husar
 */
public class MainFrameTest {
    static MainFrame instance;
    
    public MainFrameTest() {
    }
    
    // zatial sa nepodarilo cez reflexie zautomatizovat login do aplikacie
    // preto su momentalne zakomentovane test metody
    
    @BeforeClass
    public static void setUpClass() throws AWTException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        
//        instance = new MainFrame();
//        instance.setVisible(false);
//        
//        Field field = instance.getClass().getDeclaredField("tfLogin");
//        field.setAccessible(true);
//        JTextField tf = (JTextField) field.get(JTextField.class);
//        tf.setText("pmereda");
//        field.set(instance, tf);
//        Field field2 = instance.getClass().getDeclaredField("pfPassword");
//        field2.setAccessible(true);
//        JPasswordField pf = (JPasswordField) field2.get(JPasswordField.class);
//        pf.setText("PaloMer*");
//        field.set(instance, pf);
//        
//        Field field3 = instance.getClass().getDeclaredField("btnLogin");
//        field3.setAccessible(true);
//        
//        JButton button = (JButton) field3.get(JButton.class);
//        Point point = button.getLocationOnScreen();
//        Robot robot = new Robot();
//        robot.setAutoWaitForIdle(true);
//        robot.mouseMove(point.x + 10, point.y + 10);
//        robot.mousePress(MouseEvent.BUTTON1_MASK);
//        robot.mouseRelease(MouseEvent.BUTTON1_MASK);
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
     * Test of literatureSelected method, of class MainFrame.
     */
    @Test
    public void testLiteratureSelected() {
//        System.out.println("literatureSelected");
//        instance.literatureSelected(null);
//        assertNull(instance.getSelectedLzdroj());
//        LitZdroj lz = new LitZdroj();
//        instance.literatureSelected(lz);
//        assertEquals(lz, instance.getSelectedLzdroj());
    }

    /**
     * Test of literatureAddNew method, of class MainFrame.
     */
    @Test
    public void testLiteratureAddNew() {
//        System.out.println("literatureAddNew");
//        instance.literatureAddNew();
//        assertEquals("", instance.getTfLitBook().getText());
//        assertTrue(instance.getTfLitBook().isEnabled());
//        assertNull(instance.getSelectedLzdroj());
    }

    /**
     * Test of propertyChange method, of class MainFrame.
     */
    @Test
    public void testPropertyChange() {
        // TODO: do test
    }

    /**
     * Test of createOrModifyHerbPolozka method, of class MainFrame.
     */
    @Test
    public void testCreateOrModifyHerbPolozka() throws Exception {
//        System.out.println("createOrModifyHerbPolozka");
//        HerbarPolozky herbPol = null;
//        HerbarPolozky expResult = null;
//        HerbarPolozky result = instance.createOrModifyHerbPolozka(herbPol);
//        // TODO: not so easy to implement due to private components
    }

    /**
     * Test of createUdajObrazky method, of class MainFrame.
     */
    @Test
    public void testCreateUdajObrazky() {
//        System.out.println("createUdajObrazky");
//        // TODO: temporary
//        Set<UdajObrazky> expResult = null;
//        Set<UdajObrazky> result = instance.createUdajObrazky();
//        // TODO: not so easy to implement due to private components
    }

    /**
     * Test of createOrModifyZberatelia method, of class MainFrame.
     */
    @Test
    public void testCreateOrModifyZberatelia() {
//        System.out.println("createOrModifyZberatelia");
//        Udaj udaj = null;
//        Set<PeopleAsoc> expResult = null;
//        Set<PeopleAsoc> result = instance.createOrModifyZberatelia(udaj);
//        // TODO: not so easy to implement due to private components
    }

    /**
     * Test of createOrModifyLitZdrojRev method, of class MainFrame.
     */
    @Test
    public void testCreateOrModifyLitZdrojRev() {
//        System.out.println("createOrModifyLitZdrojRev");
//        LitZdrojRev lzdrojRev = null;
//        LitZdrojRev expResult = null;
//        LitZdrojRev result = instance.createOrModifyLitZdrojRev(lzdrojRev);
//        // TODO: not so easy to implement due to private components
    }

    /**
     * Test of populateLitZdrojFields method, of class MainFrame.
     */
    @Test
    public void testPopulateLitZdrojFields() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//        System.out.println("populateLitZdrojFields");
//        LitZdroj lzdroj = new LitZdroj();
//        instance.populateLitZdrojFields(lzdroj);
//        
//        Field privateStringField = MainFrame.class.getDeclaredField("tfLitPublTitle");
//        privateStringField.setAccessible(true);
//        JTextField result = (JTextField) privateStringField.get(instance);
//        
//        assertEquals("", result.getText());
//                
//        String expResult = "Nazov clanku";
//        lzdroj.setNazovClanku(expResult);
//        instance.populateLitZdrojFields(lzdroj);
//        
//        result = (JTextField) privateStringField.get(instance);
//        
//        assertEquals(expResult, result.getText());
    }

    /**
     * Test of populateRevisionFields method, of class MainFrame.
     */
    @Test
    public void testPopulateRevisionFields() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//        System.out.println("populateRevisionFields");
//        SkupRev rev = new SkupRev();
//        instance.populateRevisionFields(rev);
//        
//        Field privateStringField = MainFrame.class.getDeclaredField("tfDatumRevD");
//        privateStringField.setAccessible(true);
//        JTextField result = (JTextField) privateStringField.get(instance);
//        
//        assertEquals("", result.getText());
//        
//        String date = "19981020";
//        rev.setDatum(date);
//        instance.populateRevisionFields(rev);
//        String expResult = date.substring(6);
//        
//        result = (JTextField) privateStringField.get(instance);
//        
//        assertEquals(expResult, result.getText());
    }

    /**
     * Test of setTabsAndFocus method, of class MainFrame.
     */
    @Test
    public void testSetTabsAndFocus() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//        System.out.println("setTabsAndFocus");
//        Character typ = 'H';
//        boolean b = true;
//        instance.setTabsAndFocus(typ, b);
//        
//        Field privateStringField = MainFrame.class.getDeclaredField("jScrollPaneLitZdroj");
//        privateStringField.setAccessible(true);
//        JScrollPane result = (JScrollPane) privateStringField.get(instance);
//        
//        assertEquals("Literárny zdroj", result.getName());
//        
//        Field privateStringField2 = MainFrame.class.getDeclaredField("tabbedPaneInsert");
//        privateStringField2.setAccessible(true);
//        JTabbedPane result2 = (JTabbedPane) privateStringField2.get(instance);
//        
//        int expFocusTabIndex = 1;
//        assertEquals(expFocusTabIndex, result2.getSelectedIndex());
                
    }

    /**
     * Test of setNewRecordTabName method, of class MainFrame.
     */
    @Test
    public void testSetNewRecordTabNameNull() {
//        try {
//            System.out.println("setNewRecordTabName");
//            Character typ = null;
//            instance.setNewRecordTabName(typ);
//            fail("Test should give exception.");
//        } catch (Exception e) {
//            assertThat(e, instanceOf(NullPointerException.class));
//        }
    }
    
    /**
     * Test of setNewRecordTabName method, of class MainFrame.
     */
    @Test
    public void testSetNewRecordTabNameUnknown() {
//        try {
//            System.out.println("setNewRecordTabName");
//            Character typ = 'K';
//            instance.setNewRecordTabName(typ);
//            fail("Test should give exception.");
//        } catch (Exception e) {
//            assertThat(e, instanceOf(IllegalArgumentException.class));
//        }
    }
    
    /**
     * Test of setNewRecordTabName method, of class MainFrame.
     */
    @Test
    public void testSetNewRecordTabNameCorrect() {
//            System.out.println("setNewRecordTabName");
//            Character typ = 'T';
//            instance.setNewRecordTabName(typ);
//            assertEquals("Nový terénny údaj", instance.getRecordTab().getName());
    }

    /**
     * Test of getRecordTab method, of class MainFrame.
     */
    @Test
    public void testGetRecordTab() {
//        System.out.println("getRecordTab");
//        JPanel result = instance.getRecordTab();
//        assertNotNull(result);
    }
    
}
