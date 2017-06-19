/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.excelimport;

import java.awt.Window;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import sk.sav.bot.dataflos.factory.UdajFactory;
import sk.sav.bot.dataflos.util.HibernateQuery;

/**
 *
 * @author jakub.husar
 */
@Ignore
public class ExcelPrepareImportTest {
    
    HibernateQuery hq;
    
    public ExcelPrepareImportTest() {
        hq = HibernateQuery.getInstance("pmereda", "PaloMer*");
    }

    /**
     * Test of doInBackground method, of class ExcelPrepareImport.
     */
    
    // PROBLEM S VYSKAKUJUCIM JOPTION_PANE OKNOM
    
    @Test
    public void testDoInBackground() {
//        System.out.println("doInBackground");
//        //File file = new File(getClass().getResource("/Import_example.xls").getFile());
//        File file = new File(getClass().getResource("/Chenopodium_example.xls").getFile());
//        ExcelPrepareImport instance = new ExcelPrepareImport(ExcelImportMonitor.getInstance(), file, hq);
//        instance.doInBackground();
//        closeJOptionPane();
//        assertEquals(5, instance.importUdaje.size());
          
    }

    /**
     * Test of process method, of class ExcelPrepareImport.
     */
    @Test
    public void testProcess() {
        // TODO not implemented
    }

    /**
     * Test of prepareImport method, of class ExcelPrepareImport.
     */
    @Test
    public void testDoImport() {
//        System.out.println("doImport");
//        long beforeUdajCount = hq.getUdajAllCount();
//        File file = new File(getClass().getResource("/Import_example.xls").getFile());
//        ExcelPrepareImport instance = new ExcelPrepareImport(ExcelImportMonitor.getInstance(), file, hq);
//        instance.doInBackground();
//        closeJOptionPane();
//        long afterUdajCount = hq.getUdajAllCount();
        //assertEquals(beforeUdajCount + instance.importUdaje.size(), afterUdajCount);
    }
    
    private void closeJOptionPane(){
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1
                    && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                    dialog.dispose();
                }
            }
        }
    }
    
}
