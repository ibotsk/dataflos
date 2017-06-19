/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

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
@Suite.SuiteClasses({sk.sav.bot.dataflos.verify.DateMonthVerifierTest.class, sk.sav.bot.dataflos.verify.DateDayVerifierTest.class, sk.sav.bot.dataflos.verify.UrlVerifierTest.class, sk.sav.bot.dataflos.verify.DateYearVerifierTest.class, sk.sav.bot.dataflos.verify.DataVerifierTest.class})
public class VerifySuite {

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
