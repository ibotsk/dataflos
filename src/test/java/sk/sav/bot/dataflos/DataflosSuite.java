/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author jakub.husar
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({sk.sav.bot.dataflos.verify.VerifySuite.class, sk.sav.bot.dataflos.exception.ExceptionSuite.class, sk.sav.bot.dataflos.factory.FactorySuite.class, sk.sav.bot.dataflos.gui.GuiSuite.class, sk.sav.bot.dataflos.models.ModelsSuite.class, sk.sav.bot.dataflos.main.MainSuite.class, sk.sav.bot.dataflos.auth.AuthSuite.class, sk.sav.bot.dataflos.excelimport.ExcelimportSuite.class, sk.sav.bot.dataflos.util.UtilSuite.class})
public class DataflosSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
