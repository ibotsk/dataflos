/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import sk.sav.bot.dataflos.entity.Brumit1;
import sk.sav.bot.dataflos.entity.Brumit2;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Matus
 */
public class HibernateUtil {

    private static HibernateUtil singleton;
    private static SessionFactory sessionFactory;//final
    
    private static Logger log = Logger.getLogger(HibernateUtil.class.getName());

    private HibernateUtil(String login, String pass) {

        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            Configuration cfg = new Configuration().setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    //.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/dataflos_testing")
                    //.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5433/sav")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://147.213.82.11:5432/sav")
                    .setProperty("hibernate.connection.username", login)
                    .setProperty("hibernate.connection.password", pass)
                    //.setProperty("hibernate.show_sql", "true")
                    //.setProperty("hibernate.type", pass)
                    .setProperty("hibernate.query.factory_class", "org.hibernate.hql.internal.classic.ClassicQueryTranslatorFactory")
                    .setProperty("current_session_context_class", "thread");
            cfg.addResource("hbm-xml/Kolekcie.hbm.xml").addResource("hbm-xml/HerbarPolozky.hbm.xml")
                    .addResource("hbm-xml/Udaj.hbm.xml").addResource("hbm-xml/FamilyApg.hbm.xml")
                    .addResource("hbm-xml/Ftgokres.hbm.xml").addResource("hbm-xml/Ftgobvod.hbm.xml")
                    .addResource("hbm-xml/LitZdroj.hbm.xml").addResource("hbm-xml/LzdrojAutoriAsoc.hbm.xml")
                    .addResource("hbm-xml/LzdrojEditoriAsoc.hbm.xml").addResource("hbm-xml/LitZdrojRev.hbm.xml")
                    .addResource("hbm-xml/Ftgoblast.hbm.xml").addResource("hbm-xml/MenaTaxonov.hbm.xml")
                    .addResource("hbm-xml/Genus.hbm.xml").addResource("hbm-xml/Flora.hbm.xml")
                    .addResource("hbm-xml/UdajZberAsoc.hbm.xml").addResource("hbm-xml/Nac.hbm.xml")
                    .addResource("hbm-xml/Exsikaty.hbm.xml")//.addResource("hbm-xml/Brumit1.hbm.xml")
                    .addResource("hbm-xml/Vac.hbm.xml").addResource("hbm-xml/Family.hbm.xml")
                    .addResource("hbm-xml/Casopisy.hbm.xml").addResource("hbm-xml/DallaTorre.hbm.xml")
                    .addResource("hbm-xml/SkupRev.hbm.xml").addResource("hbm-xml/MenaZberRev.hbm.xml")
                    .addResource("hbm-xml/Voucher.hbm.xml").addResource("hbm-xml/UdajObrazky.hbm.xml")
                    .addResource("hbm-xml/Chu.hbm.xml")//.addResource("hbm-xml/Brumit2.hbm.xml")
                    .addResource("hbm-xml/Brumit4.hbm.xml").addResource("hbm-xml/Herbar.hbm.xml")
                    .addResource("hbm-xml/SkupRevDet.hbm.xml").addResource("hbm-xml/Ngc.hbm.xml")
                    .addResource("hbm-xml/Lokality.hbm.xml").addResource("hbm-xml/Obec.hbm.xml")
                    .addResource("hbm-xml/ListOfSpecies.hbm.xml").addResource("hbm-xml/Brumit3.hbm.xml")
                    .addResource("hbm-xml/Vgc.hbm.xml").addResource("hbm-xml/LokalityKvadrantAsoc.hbm.xml")
                    .addResource("hbm-xml/LokalityFtgokresAsoc.hbm.xml").addResource("hbm-xml/Kvadrant.hbm.xml")
                    .addResource("hbm-xml/Uzivatelia.hbm.xml")//.addResource("hbm-xml/Revizie.hbm.xml")
                    .addResource("hbm-xml/TaxonPochybnost.hbm.xml").addResource("hbm-xml/TaxonOhrozenost.hbm.xml")
                    .addResource("hbm-xml/TaxonEndemizmus.hbm.xml").addResource("hbm-xml/TaxonPovodnost.hbm.xml");
            cfg.addAnnotatedClass(Brumit1.class).addAnnotatedClass(Brumit2.class);
            sessionFactory = cfg.buildSessionFactory();
            log.info("Initial Hibernate SessionFactory created.");
        } catch (Throwable ex) { 
            log.error("Initial Hibernate SessionFactory creation failed. " + login + ". " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(String login, String pass) {
        if (singleton == null) {
            singleton = new HibernateUtil(login, pass);
        }
        return sessionFactory;

    }
}
