/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static org.hamcrest.CoreMatchers.instanceOf;
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
public class HandyUtilsTest {
    
    public HandyUtilsTest() {
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
     * Test of eToN method, of class HandyUtils.
     */
    @Test
    public void testEToN() {
        System.out.println("eToN");
        assertEquals(null, HandyUtils.eToN(""));
        assertEquals(" ", HandyUtils.eToN(" "));
        assertEquals("non-empty", HandyUtils.eToN("non-empty"));
        
        try {
            assertEquals(null, HandyUtils.eToN(null));
            // should no pass
            fail("The test with null value should not pass");
        } catch (Exception e){
            assertThat(e, instanceOf(NullPointerException.class));
        }
    }

    /**
     * Test of isNE method, of class HandyUtils.
     */
    @Test
    public void testIsNE() {
        System.out.println("isNE");
        assertEquals(true, HandyUtils.isNE(""));
        assertEquals(true, HandyUtils.isNE(null));
        assertEquals(false, HandyUtils.isNE("not-null-empty"));
    }

    /**
     * Test of createDate method, of class HandyUtils.
     */
    @Test
    public void testCreateDate() {
        System.out.println("createDate");
        assertEquals("", HandyUtils.createDate("", "", ""));
        assertEquals("", HandyUtils.createDate("14", "12", ""));
        assertEquals("09830000", HandyUtils.createDate("", "", "983"));
        assertEquals("00150411", HandyUtils.createDate("11", "4", "15"));
        assertEquals("19910700", HandyUtils.createDate("", "7", "1991"));
        assertEquals("20140908", HandyUtils.createDate("8", "9", "2014"));
        
        try {
            assertEquals(null, HandyUtils.createDate(null, "", "1996"));
            // should no pass
            fail("The test with null value should not pass");
        } catch (Exception e){
            assertThat(e, instanceOf(NullPointerException.class));
        }
    }

    /**
     * Test of convertStringToDate method, of class HandyUtils.
     */
    @Test
    public void testConvertStringToDate() {
        System.out.println("convertStringToDate");
        assertEquals("", HandyUtils.convertStringToDate(null));
        assertEquals("", HandyUtils.convertStringToDate(""));
        assertEquals("", HandyUtils.convertStringToDate(" "));
        assertEquals("", HandyUtils.convertStringToDate("1995"));
        assertEquals("", HandyUtils.convertStringToDate("00000000"));
        assertEquals("2001", HandyUtils.convertStringToDate("20010000"));
        assertEquals("2001", HandyUtils.convertStringToDate("20010400"));
        assertEquals("2001", HandyUtils.convertStringToDate("20010018"));
        assertEquals("26. 12. 2014", HandyUtils.convertStringToDate("20141226"));
        
    }

    /**
     * Test of iconToByteArray method, of class HandyUtils.
     */
    @Test
    public void testImageToByteArray() {
        System.out.println("iconToByteArray");
        BufferedImage ii;
        try {
            ii = ImageIO.read(new File(getClass().getResource("/app_logo.png").getFile()));
            assertArrayEquals(new byte[0], HandyUtils.imageToByteArray(null));
            assertTrue(HandyUtils.imageToByteArray(ii).length > 0);
        } catch (IOException ex) {
            Logger.getLogger(HandyUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        /**
     * Test of doubleToCoordinates method, of class HandyUtils.
     */
    @Test
    public void testDoubleToCoordinates() {
        System.out.println("doubleToCoordinates");
        assertEquals("00°00'00''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("00°00'00''")));
        assertEquals("35°00'00''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("35°00'00''")));
        assertEquals("40°25'12''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("40°25'12''")));
        assertEquals("73°58'48''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("73°58'48''")));
        assertEquals("71°43'24.6''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("71°43'24.6''")));
        assertEquals("75°22'19.95''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("75°22'19.95''")));
        assertEquals("78°30'29.452''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("78°30'29.452''")));
        assertEquals("-16°10'23.6''", HandyUtils.doubleToCoordinates(HandyUtils.coordinatesToDoule("-16°10'23.6''")));
    }

    /**
     * Test of coordinatesToDoule method, of class HandyUtils.
     */
    @Test
    public void testCoordinatesToDoule() {
        System.out.println("coordinatesToDoule");
        assertEquals(40.42, HandyUtils.coordinatesToDoule("40°25'12.0''"), 0.05);
        assertEquals(75.3722, HandyUtils.coordinatesToDoule("75°22'19.9''"), 0.05);
        assertEquals(71.7235, HandyUtils.coordinatesToDoule(HandyUtils.doubleToCoordinates(71.7235)), 0.05);
        assertEquals(31.36217, HandyUtils.coordinatesToDoule(HandyUtils.doubleToCoordinates(31.36217)), 0.05);
    }
    
}
