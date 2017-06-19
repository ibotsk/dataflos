package sk.sav.bot.dataflos.util;

import sk.sav.bot.dataflos.entity.Brumit4;
import sk.sav.bot.dataflos.entity.Casopisy;
import sk.sav.bot.dataflos.entity.FamilyApg;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Genus;
import sk.sav.bot.dataflos.entity.Herbar;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.Obec;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sk.sav.bot.dataflos.entity.TaxonEndemizmus;
import sk.sav.bot.dataflos.entity.TaxonOhrozenost;
import sk.sav.bot.dataflos.entity.TaxonPovodnost;

/**
 *
 * @author Matus
 */
public class HibernateQuery {

    private final Session session;
    private static HibernateQuery instance;
    private final String login;
    private boolean admin;
    private static final String sqlVerejnePristupny = "u.verejnePristupny = TRUE OR (u.verejnePristupny = FALSE AND u.verejnePristupnyOd <= to_char(current_timestamp, 'YYYYMMDD'))";

    private static Logger log = Logger.getLogger(HibernateQuery.class.getName());
    
    private HibernateQuery(String login, String pass) {
        this.session = HibernateUtil.getSessionFactory(login, pass).openSession();
        this.login = login;
        this.admin = userIsAdmin(login);
        log.info("Hibernate session opened");
    }

    public static synchronized HibernateQuery getInstance(String login, String pass) {
        if (instance == null) {
            instance = new HibernateQuery(login, pass);
        }
        return instance;
    }

    public Session getSession() {
        return this.session;
    }
    
    public boolean isAdmin(){
        return admin;
    }

    public List<Entity> getAllRecords(String tablename) {
        return getAllRecords(tablename, "id");
    }

    public List<Entity> getAllRecords(String tablename, String orderBy) {
        if (orderBy == null) {
            log.error("In getAllRecords method orderBy is NULL");
            throw new NullPointerException("order by");
        }
        if (orderBy.isEmpty()) {
            log.error("In getAllRecords method orderBy is empty");
            throw new IllegalArgumentException("order by");
        }
        //this.session.beginTransaction();
        String hql = "FROM " + tablename + " AS tbl ORDER BY tbl." + orderBy;
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return q.list();
    }
    
    public List<Udaj> getUdajAll() {
        String hql = "FROM Udaj AS u";
        if (!admin){
            hql = hql.concat(" WHERE ("+sqlVerejnePristupny+" OR u.uzivatel = '"+getLogin()+"')");
        }
        hql = hql.concat(" ORDER BY u.id");
        
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return q.list();
    }
    
    public long getUdajAllCount() {
        String hql = "SELECT COUNT(*) FROM Udaj AS u";
        if (!admin){
            hql = hql.concat(" WHERE ("+sqlVerejnePristupny+" OR u.uzivatel = '"+getLogin()+"')");
        }
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return (Long) q.uniqueResult();
    }

    public List<Udaj> getUdajInsertedBy() {
        String hql = "FROM Udaj AS u WHERE u.uzivatel = '"+getLogin()+"' order by u.id";
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return q.list();
    }
    
    public long getAllMyRecordsCount() {
        String hql = "SELECT COUNT(*) FROM Udaj AS u WHERE u.uzivatel = '"+getLogin()+"'";
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return (Long) q.uniqueResult();
    }
    
    public List<String> getDataFromTableColumn(String tablename, String column) {
        if (column == null) {
            log.error("In getDataFromTableColumn method specific column is NULL");
            throw new NullPointerException("column");
        }
        if (column.isEmpty()) {
            log.error("In getDataFromTableColumn method specific column is empty");
            throw new IllegalArgumentException("column");
        }

        //this.session.beginTransaction();
        // bez duplikatov
        String hql = "SELECT DISTINCT tbl."+ column +" FROM " + tablename + " AS tbl ORDER BY tbl." + column;
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return q.list();
    }
    
    //ziskaj posledny zadany udaj od uzivatela
    public Udaj getLastUdaj() {
        String hql = "FROM Udaj AS u";
        if (!admin){
            hql = hql.concat(" WHERE ("+sqlVerejnePristupny+" OR u.uzivatel = '"+getLogin()+"')");
        }
        hql = hql.concat(" ORDER BY u.id DESC LIMIT 1");
        
        Query q = this.session.createQuery(hql);
        this.session.flush();
    
        //Udaj ret;
        //try {
        //    ret = (Udaj) q.uniqueResult();
        //} catch (NonUniqueResultException nure) {
        //    ret = (Udaj) q.list().get(0);
        //}
        return (Udaj) q.uniqueResult();
    }

    //ziskaj posledny zadany udaj od uzivatela
    public Udaj getMyLastUdaj() {
        String hql = "FROM Udaj AS u WHERE u.uzivatel = '"+getLogin()+"' ORDER BY u.id DESC LIMIT 1";
        Query q = this.session.createQuery(hql);
        this.session.flush();
    
        //Udaj ret;
        //try {
        //    ret = (Udaj) q.uniqueResult();
        //} catch (NonUniqueResultException nure) {
        //    ret = (Udaj) q.list().get(0);
        //}
        return (Udaj) q.uniqueResult();
    }
    
    //ziskaj n-ty najnovsi udaj v databaze (vyuzite pri prezerani udajov)
    public Udaj getNthLastUdaj(int offset) {
        String hql = "FROM Udaj AS u";
        if (!admin){
            hql = hql.concat(" WHERE ("+sqlVerejnePristupny+" OR u.uzivatel = '"+getLogin()+"')");
        }
        hql = hql.concat(" ORDER BY u.id DESC LIMIT 1 OFFSET " + offset);
        
        Query q = this.session.createQuery(hql);
        this.session.flush();
        
        Udaj ret;
        try {
            ret = (Udaj) q.uniqueResult();
        } catch (NonUniqueResultException nure) {
            ret = (Udaj) q.list().get(0);
        }
        return ret;
    }
    
    //ziskaj n-ty najnovsi udaj z databazy od konkretneho pouzivatela
    public Udaj getMyNthLastUdaj(int offset) {
        String hql = "FROM Udaj AS u WHERE u.uzivatel = '"+getLogin()+"' ORDER BY u.id DESC LIMIT 1 OFFSET " + offset;
        Query q = this.session.createQuery(hql);
        this.session.flush();
        
        Udaj ret;
        try {
            ret = (Udaj) q.uniqueResult();
        } catch (NonUniqueResultException nure) {
            ret = (Udaj) q.list().get(0);
        }
        return ret;
    }

    public Herbar getHerbarBySkratka(String skratka) {
        //this.session.beginTransaction();
        String hql = "FROM Herbar WHERE skratka_herb = '" + skratka.trim() + "'";
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return (Herbar) q.uniqueResult();
    }

    public Object getById(Class type, int id) {
        Object ent = this.session.load(type, new Integer(id));
        return ent;
    }

    public void insertOrUpdateEntity(Entity entity) {
        Transaction tx = null;
        try {
            tx = this.session.beginTransaction();
            this.session.saveOrUpdate(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
            log.error("insertOrUpdateEntity cancelled with exception: "+e);
        }
    }

    public void insertEntities(Set<Entity> entities) {
        Transaction tx = null;
        try {
            tx = this.session.beginTransaction();
            for (Entity entity : entities) {
                this.session.save(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
            log.error("insertEntities cancelled with exception: "+e);
        }
    }

    public void deleteEntity(Entity entity) {
        if (entity != null) {
            Transaction tx = null;
            try {
                tx = this.session.beginTransaction();
                this.session.delete(entity);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                    throw e;
                }
            }
        }
    }

    //ziskaj pocet udajov, ktore maju mensie id ako id zadaneho udaja na urcenie pozicie v tabulke
    public long getUdajIdPosition(int id) {

        String hql = "SELECT COUNT(*) FROM Udaj AS u WHERE";
        if (!admin){
            hql = hql.concat(" ("+sqlVerejnePristupny+" OR u.uzivatel = '" + getLogin() + "') AND");
        }
        hql = hql.concat(" u.id <= "+id);
        
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return (long) q.uniqueResult();
    }

    //ziskaj pocet udajov daneho pouzivatela, ktore maju mensie id ako id zadaneho udaja na urcenie pozicie v tabulke
    public long getMyUdajIdPosition(int id) {
        
        String hql = "SELECT COUNT(*) FROM Udaj AS u WHERE u.uzivatel = '" + getLogin() + "' AND u.id <= "+id;
        
        //this.session.beginTransaction();
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return (long) q.uniqueResult();
    }

    public List<Udaj> filterByCriteria(String idUdaj, String cisloPolozky, String ciarKod, Herbar herbar, MenaZberRev autorZberu, Brumit4 stat, Ftgokres ftg, Obec obec, Kvadrant kvadrant, String rok, Casopisy casopis, MenaZberRev autorPublikacie, ListOfSpecies los, String taxonMatch, Genus genus, FamilyApg familyAPG, String recNumber, boolean poslRev, boolean ochrana, TaxonOhrozenost ohrozenost, TaxonEndemizmus endemizmus, TaxonPovodnost povodnost, boolean myRecords) {
        boolean first = true;
        
        String hql = "FROM Udaj AS u"; 
        
        //this.session.beginTransaction();
        if (!idUdaj.isEmpty()){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.id = "+idUdaj+")");
        }
        if (!cisloPolozky.isEmpty()){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.herbarPolozky.cisloPol = '"+cisloPolozky+"')");
        }
        if (!ciarKod.isEmpty()){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.herbarPolozky.cisloCkFull = '"+ciarKod+"')");
        }
        if (herbar != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.herbarPolozky.herbar.id = "+herbar.getId()+")");
        }
        if (autorZberu != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT uza.udaj FROM UdajZberAsoc AS uza INNER JOIN uza.menaZberRev AS mzr WHERE mzr.id="+autorZberu.getId()+"))");
        }
        if (stat != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.lokality.brumit4.id = "+stat.getId()+")");
        }
        if (obec != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.lokality.obec.id = "+obec.getId()+")");
        }
        if (ftg != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.lokality IN (SELECT lfa.lokality FROM LokalityFtgokresAsoc AS lfa INNER JOIN lfa.ftgokres AS ftg WHERE ftg.id="+ftg.getId()+"))");
        }
        if (kvadrant != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.lokality IN (SELECT lka.lokality FROM LokalityKvadrantAsoc AS lka INNER JOIN lka.kvadrant AS kvad WHERE kvad.id="+kvadrant.getId()+"))");
        }
        // TODO: do buducna, kontrolovat nazov casopisu aj v lit. reviziach?
        if (casopis != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT lzr.udaj FROM LitZdrojRev AS lzr WHERE lzr.litZdroj.casopis.id = "+casopis.getId()+"))"); // OR (u IN (SELECT lzr.udaj FROM LitZdrojRev AS lzr WHERE lzr.litZdroj.casopis.id = "+casopis.getId()+"))
        }
        // TODO: do buducna, kontrolovat autora aj v lit. reviziach?
        if (autorPublikacie != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u.litZdroj IN (SELECT lza.litZdroj FROM LzdrojAutoriAsoc AS lza INNER JOIN lza.menoAutora AS ma WHERE ma.id="+autorPublikacie.getId()+"))");
        }
        // TODO: do buducnam, kontrolovat rok vydania aj lit. revizii?
        if (rok != null && !rok.isEmpty()){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(");
            hql = hql.concat("((u.litZdroj.rok = '"+rok+"'))"); // OR (u IN (SELECT lzr.udaj FROM LitZdrojRev AS lzr WHERE lzr.litZdroj.rok = '"+rok+"')))");
            hql = hql.concat(")");
        }
        // metoda .toLowerCase() a LOWER v query pouzite na odstranenie case sensitive porovnavania
        if (los != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(");
            if (poslRev){
                hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND sr.menaTaxonov.listOfSpecies.listOfSpecies.id = "+los.getId()+") OR u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND sr.menaTaxonov.listOfSpecies.id = "+los.getId()+") OR u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND LOWER(sr.menaTaxonov.menoScheda) LIKE '"+los.getMeno().toLowerCase()+"%') OR ");
            }
            hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND sr.menaTaxonov.listOfSpecies.listOfSpecies.id = "+los.getId()+") OR u.id IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND sr.menaTaxonov.listOfSpecies.id = "+los.getId()+") OR u.id IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND LOWER(sr.menaTaxonov.menoScheda) LIKE '"+los.getMeno().toLowerCase()+"%')");
            hql = hql.concat(")");
        }
        if (!taxonMatch.isEmpty()){
            taxonMatch = taxonMatch.toLowerCase();
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(");
            if (poslRev){
                hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND LOWER(sr.menaTaxonov.listOfSpecies.listOfSpecies.meno) LIKE '%"+taxonMatch+"%') OR u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND LOWER(sr.menaTaxonov.listOfSpecies.meno) LIKE '%"+taxonMatch+"%') OR u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND LOWER(sr.menaTaxonov.menoScheda) LIKE '%"+taxonMatch+"%') OR ");
            }
            hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND LOWER(sr.menaTaxonov.listOfSpecies.listOfSpecies.meno) LIKE '%"+taxonMatch+"%') OR u.id IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND LOWER(sr.menaTaxonov.listOfSpecies.meno) LIKE '%"+taxonMatch+"%') OR u.id IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = FALSE AND LOWER(sr.menaTaxonov.menoScheda) LIKE '%"+taxonMatch+"%')");
            hql = hql.concat(")");
        } 
        if (genus != null){
            hql = addKeyWords(first, hql);
            first = false;
            if (poslRev){
                hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND sr.menaTaxonov.listOfSpecies.genus.id = "+genus.getId()+") OR ");
            }
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.genus.id = "+genus.getId()+"))");
        }
        if (familyAPG != null){
            hql = addKeyWords(first, hql);
            first = false;
            if (poslRev){
                hql = hql.concat("u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.FRevizia = TRUE AND sr.menaTaxonov.listOfSpecies.genus.familyApg.id = "+familyAPG.getId()+") OR ");
            }
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.genus.familyApg.id = "+familyAPG.getId()+"))");
        }
        // berieme do uvahy iba ak je ochrana zaskrtnuta
        if (ochrana){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.taxonOchrana = "+ochrana+"))");
        }
        if (ohrozenost != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.taxonOhrozenost.id = "+ohrozenost.getId()+"))");
        }
        if (endemizmus != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.taxonEndemizmus.id = "+endemizmus.getId()+"))");
        }
        if (povodnost != null){
            hql = addKeyWords(first, hql);
            first = false;
            hql = hql.concat("(u IN (SELECT sr.udaj FROM SkupRev AS sr WHERE sr.menaTaxonov.listOfSpecies.taxonPovodnost.id = "+povodnost.getId()+"))");
        }
        
        
        if (myRecords){
            hql = addKeyWords(first, hql);
            hql = hql.concat("u.uzivatel = '" + getLogin() + "' ");
        } else if (!admin){
            hql = addKeyWords(first, hql);
            hql = hql.concat("("+sqlVerejnePristupny+" OR u.uzivatel = '" + getLogin() + "') ");
        }
        
        if (!recNumber.equals("")) {
            hql = hql.concat(" ORDER BY u.id DESC LIMIT " + Integer.parseInt(recNumber));
        } else {
            hql = hql.concat(" ORDER BY u.id ASC");
        }
        
        Query q = this.session.createQuery(hql);
        this.session.flush();
        //this.session.getTransaction().commit();
        return q.list();
    }

    private String addKeyWords(boolean first, String hql) {
        if (first){
            return hql.concat(" WHERE ");
        } else {
            return hql.concat(" AND ");
        }
    }

    public String getLogin() {
        return login;
    }
    
    private boolean userIsAdmin(String userLogin){
        Query q = this.session.createQuery("SELECT us.admin FROM Uzivatelia AS us WHERE us.login = '"+userLogin+"'");
        this.session.flush();
        return (boolean) q.uniqueResult();
    }
        
}
