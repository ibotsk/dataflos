/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.factory;

import sk.sav.bot.dataflos.entity.UdajZberAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.HerbarPolozky;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.LzdrojEditoriAsoc;
import sk.sav.bot.dataflos.entity.SkupRevDet;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.UdajObrazky;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Matus
 */
public class UdajFactory {
    
    private static Logger log = Logger.getLogger(UdajFactory.class.getName());

    private static UdajFactory instance = null;
    private Session session;

    private UdajFactory(Session session) {
        this.session = session;
    }

    public static UdajFactory getInstance(Session session) {
        if (instance == null) {
            instance = new UdajFactory(session);
        }
        return instance;
    }

    public Udaj createUdaj(Udaj udaj, Lokality lokalita, HerbarPolozky polozka, List<LitZdrojRev> lits, List<SkupRev> revizie,
            SkupRev urcenie, List<UdajObrazky> obrazky, Set<PeopleAsoc> zberatelia,
            char typ, String datumZberu, String datumSlovom, boolean verejny, String verejnyDatum, String login) {

        log.info("Creating and saving udaj");
        session.beginTransaction();
        if (lokalita != null) {
            
            Set<LokalityFtgokresAsoc> ftgokresAsocsCopy = new HashSet<>(lokalita.getLokalityFtgokresAsocs());
            lokalita.getLokalityFtgokresAsocs().clear();
            session.saveOrUpdate(lokalita);
            for (LokalityFtgokresAsoc lfa : ftgokresAsocsCopy) {
                if (lfa.getId().getIdLokality() < 0) {
                    lfa.getId().setIdLokality(lokalita.getId());
                }
                lfa.setLokality(lokalita);
                session.saveOrUpdate(lfa);
                lokalita.getLokalityFtgokresAsocs().add(lfa);
            }
            session.saveOrUpdate(lokalita);
            
            Set<LokalityKvadrantAsoc> kvadrantAsocsCopy = new HashSet<>(lokalita.getLokalityKvadrantAsocs());
            lokalita.getLokalityKvadrantAsocs().clear();
            session.saveOrUpdate(lokalita);
            for (LokalityKvadrantAsoc lka : kvadrantAsocsCopy) {
                if (lka.getId().getIdLokality() < 0) {
                    lka.getId().setIdLokality(lokalita.getId());
                }
                lka.setLokality(lokalita);
                session.saveOrUpdate(lka);
                lokalita.getLokalityKvadrantAsocs().add(lka);
            }
            session.saveOrUpdate(lokalita);
        }
        
        if (polozka != null) {
            session.saveOrUpdate(polozka);
        }

        if (udaj == null) {
            udaj = new Udaj();
        }
        udaj.setDatumZberu(datumZberu);
        udaj.setDatumZberuSlovom(datumSlovom);
        udaj.setTyp(typ);
        udaj.setVerejnePristupny(verejny);
        udaj.setVerejnePristupnyOd(verejnyDatum);
        udaj.setUzivatel(login);
        udaj.setHerbarPolozky(polozka);
        udaj.setLokality(lokalita);

        session.saveOrUpdate(udaj);

        if (urcenie != null) {
            if (urcenie.getMenaTaxonov() == null) {
                udaj.getSkupRevs().clear();
                session.saveOrUpdate(udaj);
                session.delete(urcenie);
            } else {
                session.saveOrUpdate(urcenie.getMenaTaxonov());
                urcenie.setUdaj(udaj);
                //najprv treba persistovat urcenie s prazdnymi urcovatelmi, pretoze ti este nie su persistovani a to vyhodi chybu.
                Set<SkupRevDet> srdsCopy = new HashSet<>(urcenie.getSkupRevDets());
                urcenie.getSkupRevDets().clear();
                session.saveOrUpdate(urcenie);
                udaj.getSkupRevs().add(urcenie);
                for (SkupRevDet srd : srdsCopy) {
                    if (srd.getId().getIdSkupRev() < 0) { //kontrola pre novych urcovatelov (-1 podla MainFrame.updateDetList)
                        srd.getId().setIdSkupRev(urcenie.getId());
                    }
                    srd.setSkupRev(urcenie);
                    session.saveOrUpdate(srd);
                    urcenie.getSkupRevDets().add(srd);
                }
                //po tom, co su urcovatelia persistentni, mozeme ich priradit urceniu a updatujeme
                session.saveOrUpdate(urcenie);
            }
        }

        if (lits != null) {
            udaj.getLitZdrojRevs().clear();
            session.saveOrUpdate(udaj);
            
            boolean isFirst = true;
            for (LitZdrojRev litZdrojRev : lits) {
                LitZdroj litZdroj = litZdrojRev.getLitZdroj();
                addLitSourcePeople(litZdroj);
                // actually it is not lit. revision, just in litRev wrapper
                if (isFirst) {
                    udaj.setLitZdroj(litZdroj);
                    session.saveOrUpdate(udaj);
                    isFirst = false;
                } else {
                    session.saveOrUpdate(litZdrojRev);
                    litZdrojRev.setUdaj(udaj);
                    session.saveOrUpdate(litZdrojRev);
                    udaj.getLitZdrojRevs().add(litZdrojRev);
                }
            }
            session.saveOrUpdate(udaj);
        }
        
        if (obrazky != null) {
            for (UdajObrazky obr : obrazky) {
                obr.setUdaj(udaj);
                session.saveOrUpdate(obr);
                udaj.getUdajObrazkies().add(obr);
            }
            session.saveOrUpdate(udaj);
        }

        if (revizie != null) {
            for (SkupRev revizia : revizie) {
                this.addRevizia(udaj, revizia);
            }
            udaj.getSkupRevs().addAll(revizie);
            session.saveOrUpdate(udaj);
        }

        if (zberatelia != null) {
            //udaj.getUdajZberAsocs().clear();
            //session.saveOrUpdate(udaj);
            for (PeopleAsoc peopleAsoc : zberatelia) {
                if (peopleAsoc instanceof UdajZberAsoc) {
                    UdajZberAsoc uza = (UdajZberAsoc) peopleAsoc;
                    if (uza.getId().getIdUdaj() < 0) {
                        uza.getId().setIdUdaj(udaj.getId());
                    }
                    uza.setUdaj(udaj);
                    session.saveOrUpdate(uza);
                    udaj.getUdajZberAsocs().add(uza);
                }
            }
            session.saveOrUpdate(udaj);
        }
        session.flush();
        session.getTransaction().commit();

        log.info("Udaj successfully saved");
        
        return udaj;
    }

    private void addRevizia(Udaj udaj, SkupRev revizia) {
        if (udaj == null) {
            log.error("addRevizia: supplied udaj is NULL");
            throw new NullPointerException("udaj");
        }
        if (revizia != null) {
            session.saveOrUpdate(revizia.getMenaTaxonov());
            revizia.setUdaj(udaj);
            Set<SkupRevDet> srdsCopy = new HashSet<>(revizia.getSkupRevDets());
            revizia.getSkupRevDets().clear();
            session.saveOrUpdate(revizia);
            for (Object object : srdsCopy) {
                SkupRevDet srd = (SkupRevDet) object;
                if (srd.getId().getIdSkupRev() < 0) {
                    srd.getId().setIdSkupRev(revizia.getId());
                }
                srd.setSkupRev(revizia);
                session.saveOrUpdate(srd);
                revizia.getSkupRevDets().add(srd);
            }
            session.saveOrUpdate(revizia);
        }
    }

    private void addLitSourcePeople(LitZdroj lzdroj) {
        if (lzdroj != null) {
            Set<LzdrojAutoriAsoc> authorsCopy = new HashSet<>(lzdroj.getLzdrojAutoriAsocs());
            Set<LzdrojEditoriAsoc> editorsCopy = new HashSet<>(lzdroj.getLzdrojEditoriAsocs());
            lzdroj.getLzdrojAutoriAsocs().clear();
            lzdroj.getLzdrojEditoriAsocs().clear();
            session.saveOrUpdate(lzdroj);
            for (LzdrojAutoriAsoc author : authorsCopy) {
                if (author.getId().getIdLitZdroj() < 0) {
                    author.getId().setIdLitZdroj(lzdroj.getId());
                }
                author.setLitZdroj(lzdroj);
                session.saveOrUpdate(author);
                lzdroj.getLzdrojAutoriAsocs().add(author);
            }
            session.saveOrUpdate(lzdroj);
            for (LzdrojEditoriAsoc editor : editorsCopy) {
                if (editor.getId().getIdLitZdroj() < 0) {
                    editor.getId().setIdLitZdroj(lzdroj.getId());
                }
                editor.setLitZdroj(lzdroj);
                session.saveOrUpdate(editor);
                lzdroj.getLzdrojEditoriAsocs().add(editor);
            }
            session.saveOrUpdate(lzdroj);
        }
    }
}
