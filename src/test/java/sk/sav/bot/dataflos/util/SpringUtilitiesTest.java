/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.awt.Container;
import java.awt.Label;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
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
public class SpringUtilitiesTest {
    
    public SpringUtilitiesTest() {
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
     * Test of makeCompactGrid method, of class SpringUtilities.
     */
    @Test
    public void testMakeCompactGrid() {
        System.out.println("makeCompactGrid");
        Container panel = new JPanel(new SpringLayout());
        panel.add(new Label("Nazov"));
        panel.add(new JTextField());
        int rows = 1;
        int cols = 2;
        int initialX = 6;
        int initialY = 6;
        int xPad = 6;
        int yPad = 6;
        SpringUtilities.makeCompactGrid(panel, rows, cols, initialX, initialY, xPad, yPad);
        panel.setVisible(true);
        assertNotNull(panel.getComponent(1));
        
    }
    
}
