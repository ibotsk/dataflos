/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.factory;

import java.util.ArrayList;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.SkupRevDet;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.LzdrojEditoriAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsocId;
import sk.sav.bot.dataflos.entity.LzdrojEditoriAsocId;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsocId;
import sk.sav.bot.dataflos.entity.UdajZberAsoc;
import sk.sav.bot.dataflos.entity.SkupRevDetId;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsocId;
import sk.sav.bot.dataflos.entity.UdajZberAsocId;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author Matus
 */
public class ListAddRemoveUtils {
    
    private static Logger log = Logger.getLogger(ListAddRemoveUtils.class.getName());

    public static SkupRev setRevisorsAndIdentificators(SkupRev skupRev, List<MenaZberRev> people) {
        if (skupRev == null) {
            log.error("setRevisorsAndIdentificators: supplied skupRev is NULL");
            throw new NullPointerException("skupRev");
        }
        if (people == null) {
            log.error("setRevisorsAndIdentificators: supplied people are NULL");
            throw new NullPointerException("people");
        }
        Set<PeopleAsoc> dets;
        if (skupRev.getSkupRevDets() == null || skupRev.getSkupRevDets().isEmpty()) { //este nema zoznam
            if (!people.isEmpty()) {
                int poradie = 1;
                for (MenaZberRev menaZberRev : people) {
                    SkupRevDet srd = new SkupRevDet(skupRev, menaZberRev, poradie);
                    srd.setId(new SkupRevDetId(-1, menaZberRev.getId()));
                    skupRev.getSkupRevDets().add(srd);
                    poradie++;
                }
            } //else (people is empty) -> nebudeme nic vytvarat ani priradovat
        } else {
            dets = skupRev.getSkupRevDets();
            dets = updatePplList(dets, people);
            skupRev.getSkupRevDets().addAll(dets);
        }
        return skupRev;
    }

    public static Set<PeopleAsoc> setCollectors(Udaj udaj, List<MenaZberRev> people) {
        if (people == null) {
            log.error("setCollectors: supplied people are NULL");
            throw new NullPointerException("people");
        }
        Set<PeopleAsoc> zbers = null;

        if (udaj == null || udaj.getUdajZberAsocs() == null || udaj.getUdajZberAsocs().isEmpty()) { //prazdny zoznam zberatelov
            if (!people.isEmpty()) {
                zbers = new HashSet<>(5);
                int poradie = 1;
                for (MenaZberRev menaZberRev : people) {
                    UdajZberAsoc uza = new UdajZberAsoc(udaj, menaZberRev, poradie);
                    uza.setId(new UdajZberAsocId(-1, menaZberRev.getId()));
                    zbers.add(uza);
                    poradie++;
                }
            } //else (people is empty) -> nebudeme nic vytvarat ani priradovat
        } else { //neprazdny zoznam zberatelov
            zbers = udaj.getUdajZberAsocs();
            zbers = updatePplList(zbers, people);
        }
        return zbers;
    }
    
    public static LitZdroj setLitAuthors(LitZdroj lzdroj, List<MenaZberRev> people) {
        if (lzdroj == null) {
            log.error("setLitAuthors: supplied lzdroj is NULL");
            throw new NullPointerException("lzdroj");
        }
        if (people == null) {
            log.error("setLitAuthors: supplied people are NULL");
            throw new NullPointerException("people");
        }
        Set<PeopleAsoc> authors;
        if (lzdroj.getLzdrojAutoriAsocs() == null || lzdroj.getLzdrojAutoriAsocs().isEmpty()) { //prazdny zoznam zberatelov
            if (!people.isEmpty()) {
                int poradie = 1;
                for (MenaZberRev menaZberRev : people) {
                    LzdrojAutoriAsoc laa = new LzdrojAutoriAsoc(lzdroj, menaZberRev, poradie);
                    laa.setId(new LzdrojAutoriAsocId(-1, menaZberRev.getId()));
                    lzdroj.getLzdrojAutoriAsocs().add(laa);
                    poradie++;
                }
            } //else (people is empty) -> nebudeme nic vytvarat ani priradovat
        } else {
            authors = lzdroj.getLzdrojAutoriAsocs();
            authors = updatePplList(authors, people);
            lzdroj.getLzdrojAutoriAsocs().addAll(authors);
        }
        return lzdroj;
    }
    
    public static LitZdroj setLitEditors(LitZdroj lzdroj, List<MenaZberRev> people) {
        if (lzdroj == null) {
            log.error("setLitEditors: supplied lzdroj is NULL");
            throw new NullPointerException("lzdroj");
        }
        if (people == null) {
            log.error("setLitEditors: supplied people are NULL");
            throw new NullPointerException("people");
        }
        Set<PeopleAsoc> editors;
        if (lzdroj.getLzdrojEditoriAsocs() == null || lzdroj.getLzdrojEditoriAsocs().isEmpty()) { //prazdny zoznam zberatelov
            if (!people.isEmpty()) {
                int poradie = 1;
                for (MenaZberRev menaZberRev : people) {
                    LzdrojEditoriAsoc laa = new LzdrojEditoriAsoc(lzdroj, menaZberRev, poradie);
                    laa.setId(new LzdrojEditoriAsocId(-1, menaZberRev.getId()));
                    lzdroj.getLzdrojEditoriAsocs().add(laa);
                    poradie++;
                }
            } //else (people is empty) -> nebudeme nic vytvarat ani priradovat
        } else {
            editors = lzdroj.getLzdrojEditoriAsocs();
            editors = updatePplList(editors, people);
            lzdroj.getLzdrojEditoriAsocs().addAll(editors);
        }
        return lzdroj;
    }
    
    public static Lokality setFtgokresy(Lokality lokalita, List<Ftgokres> ftgokresy) {
        if (lokalita == null) {
            log.error("setFtgokresy: supplied lokalita is NULL");
            throw new NullPointerException("lokalita");
        }
        if (ftgokresy == null) {
            log.error("setFtgokresy: supplied ftgokresy are NULL");
            throw new NullPointerException("ftgokresy");
        }
        Set<LokalityFtgokresAsoc> ftgokresyAsoc;
        if (lokalita.getLokalityFtgokresAsocs() == null || lokalita.getLokalityFtgokresAsocs().isEmpty()) { //lokalita nema ziadne ftgokresy
            if (!ftgokresy.isEmpty()) {
                for (Ftgokres ftgokres : ftgokresy) {
                    LokalityFtgokresAsoc lfa = new LokalityFtgokresAsoc(lokalita, ftgokres);
                    lfa.setId(new LokalityFtgokresAsocId(-1, ftgokres.getId()));
                    lokalita.getLokalityFtgokresAsocs().add(lfa);
                }
            } //else (ftgokresy is empty) -> nebudeme nic vytvarat ani priradovat
        } else { //neprazdny zoznam ftgokresov
            ftgokresyAsoc = lokalita.getLokalityFtgokresAsocs();
            ftgokresyAsoc = updateFtgokresList(ftgokresyAsoc, ftgokresy);
            lokalita.getLokalityFtgokresAsocs().addAll(ftgokresyAsoc);
        }
        return lokalita;
    }

    public static Lokality setQuadrants(Lokality lokalita, List<Kvadrant> quadrants) {
        if (lokalita == null) {
            log.error("setQuadrants: supplied lokalita is NULL");
            throw new NullPointerException("lokalita");
        }
        if (quadrants == null) {
            log.error("setQuadrants: supplied quadrants is NULL");
            throw new NullPointerException("quadrants");
        }
        Set<LokalityKvadrantAsoc> quadAsoc;
        if (lokalita.getLokalityKvadrantAsocs() == null || lokalita.getLokalityKvadrantAsocs().isEmpty()) { //lokalita doteraz nemala ziadne kvadranty
            if (!quadrants.isEmpty()) {
                for (Kvadrant kvadrant : quadrants) {
                    LokalityKvadrantAsoc lka = new LokalityKvadrantAsoc(lokalita, kvadrant);
                    lka.setId(new LokalityKvadrantAsocId(-1, kvadrant.getId()));
                    lokalita.getLokalityKvadrantAsocs().add(lka);
                }
            } //else (quadrants is empty) -> nebudeme nic vytvarat ani priradovat
        } else { // ak lokalita mala neprazdny zoznam kvadrantov
            quadAsoc = lokalita.getLokalityKvadrantAsocs();
            quadAsoc = updateQuadrantList(quadAsoc, quadrants);
            lokalita.getLokalityKvadrantAsocs().addAll(quadAsoc);
        }
        return lokalita;
    }
    
//    public static List<LitZdrojRev> setLitZdrojs(Udaj udaj, List<LitZdrojRev> litRevs) {
//        if (litRevs == null) {
//            log.error("setLitZdrojs: supplied litRevs are NULL");
//            throw new NullPointerException("litRevs");
//        }
//        List<LitZdrojRev> lits = null;
//
//        if (udaj == null || udaj.getLitZdrojRevs() == null || udaj.getLitZdrojRevs().isEmpty()) { //prazdny zoznam zberatelov
//            if (!litRevs.isEmpty()) {
//                lits = new ArrayList<>();
//                for (LitZdrojRev lzr : litRevs) {
//                    lits.add(lzr);
//                }
//            } //else (people is empty) -> nebudeme nic vytvarat ani priradovat
//        } else { //neprazdny zoznam zberatelov
//            lits = udaj.getLitZdrojRevs();
//            lits = updateLzrList(lits, litRevs);
//        }
//        return lits;
//    }

    /**
     * Removes or adds records in toUpdate according to list updateBy based on
     * type. This method takes care of SkupRevDet and UdajZberAsoc in such way
     * that only records that need to be removed are removed. Same applies for
     * adding.
     *
     * @param toUpdate set of PeopleAsoc that is updated. Cannot be empty.
     * @param updateBy liost of people as tempate to update by
     * @return
     */
    private static Set updatePplList(Set<PeopleAsoc> toUpdate, List<MenaZberRev> updateBy) {
        if (toUpdate == null) {
            log.error("updatePplList: original people asociations to update are NULL");
            throw new NullPointerException("toUpdate");
        }
        if (updateBy == null) {
            log.error("updatePplList: new people to update are NULL");
            throw new NullPointerException("updateBy");
        }
        if (toUpdate.isEmpty()) {
            throw new IllegalArgumentException("toUpdate");
        }
        //zmazanie odstranenych
        for (Iterator<PeopleAsoc> it = toUpdate.iterator(); it.hasNext();) {
            PeopleAsoc skupRevDet = it.next();
            if (!updateBy.contains(skupRevDet.getMenaZberRev())) {
                it.remove();
            }
        }

        //vlozenie pridanych
        boolean addFlag;
        for (MenaZberRev mzr : updateBy) { //vzorovy zoznam moze mat polozky zostavajuce alebo nove
            addFlag = true;
            for (Iterator<PeopleAsoc> it = toUpdate.iterator(); it.hasNext();) {
                PeopleAsoc srd = it.next();
                if (srd.getMenaZberRev().equals(mzr)) {
                    addFlag = false;
                    break;
                }
            }

            if (addFlag) {
                Iterator<PeopleAsoc> it = toUpdate.iterator();
                if (it.hasNext()) {
                    PeopleAsoc inst = it.next();
                    if (inst instanceof SkupRevDet) {
                        SkupRevDet srdNew = new SkupRevDet(null, mzr);
                        srdNew.setId(new SkupRevDetId(-1, mzr.getId())); //spravne id sa priradi v UdajFactory
                        toUpdate.add(srdNew);
                    } else if (inst instanceof UdajZberAsoc) {
                        UdajZberAsoc uzaNew = new UdajZberAsoc(null, mzr);
                        uzaNew.setId(new UdajZberAsocId(-1, mzr.getId()));
                        toUpdate.add(uzaNew);
                    } else if (inst instanceof LzdrojAutoriAsoc) {
                        LzdrojAutoriAsoc lauthNew = new LzdrojAutoriAsoc(null, mzr);
                        lauthNew.setId(new LzdrojAutoriAsocId(-1, mzr.getId()));
                        toUpdate.add(lauthNew);
                    } else if (inst instanceof LzdrojEditoriAsoc) {
                        LzdrojEditoriAsoc lediNew = new LzdrojEditoriAsoc(null, mzr);
                        lediNew.setId(new LzdrojEditoriAsocId(-1, mzr.getId()));
                        toUpdate.add(lediNew);
                    }
                }
            }
        }
        //update poradia
        for (PeopleAsoc peopleAsoc : toUpdate) {
            peopleAsoc.setPoradie(updateBy.indexOf(peopleAsoc.getMenaZberRev()));
        }
        return toUpdate;
    }
    
    private static Set<LokalityFtgokresAsoc> updateFtgokresList(Set<LokalityFtgokresAsoc> toUpdate, List<Ftgokres> updateBy) {
        if (toUpdate == null) {
            log.error("updateFtgokresList: original ftgokresy asociations to update are NULL");
            throw new NullPointerException("ftgokresAsoc");
        }
        if (updateBy == null) {
            log.error("updateFtgokresList: new ftgokresy to update are NULL");
            throw new NullPointerException("ftgokresy");
        }
        if (toUpdate.isEmpty()) {
            log.error("updateFtgokresList: original ftgokresy asociations are empty");
            throw new IllegalArgumentException("toUpdate");
        }
        //zmazanie vystavacnych
        for (Iterator<LokalityFtgokresAsoc> it = toUpdate.iterator(); it.hasNext();) {
            LokalityFtgokresAsoc lfa = it.next();
            if (!updateBy.contains(lfa.getFtgokres())) {
                it.remove();
            }
        }
        boolean addFlag;
        for (Ftgokres ftgokres : updateBy) {
            addFlag = true; //nove ftg
            for (Iterator<LokalityFtgokresAsoc> it = toUpdate.iterator(); it.hasNext();) {
                LokalityFtgokresAsoc lfa = it.next();
                if (lfa.getFtgokres().equals(ftgokres)) {
                    addFlag = false;
                    break;
                }
            }

            if (addFlag) {
                LokalityFtgokresAsoc lfaNew = new LokalityFtgokresAsoc(null, ftgokres);
                lfaNew.setId(new LokalityFtgokresAsocId(-1, ftgokres.getId()));
                toUpdate.add(lfaNew);
            }
        }
        return toUpdate;
    }

    private static Set<LokalityKvadrantAsoc> updateQuadrantList(Set<LokalityKvadrantAsoc> toUpdate, List<Kvadrant> updateBy) {
        if (toUpdate == null) {
            log.error("updateQuadrantList: original ftgokresy asociations to update are NULL");
            throw new NullPointerException("quadAsoc");
        }
        if (updateBy == null) {
            log.error("updateQuadrantList: new ftgokresy to update are NULL");
            throw new NullPointerException("quadrants");
        }
        if (toUpdate.isEmpty()) {
            throw new IllegalArgumentException("toUpdate");
        }
        //zmazanie vystavacnych
        for (Iterator<LokalityKvadrantAsoc> it = toUpdate.iterator(); it.hasNext();) {
            LokalityKvadrantAsoc lka = it.next();
            if (!updateBy.contains(lka.getKvadrant())) {
                it.remove();
            }
        }
        boolean addFlag; // ma sa pridat dalsi kvadrant? nie je rovnaky uz obsiahnuty v povodnych?
        for (Kvadrant kvadrant : updateBy) {
            addFlag = true;
            for (Iterator<LokalityKvadrantAsoc> it = toUpdate.iterator(); it.hasNext();) {
                LokalityKvadrantAsoc lka = it.next();
                if (lka.getKvadrant().equals(kvadrant)) {
                    addFlag = false;
                    break;
                }
            }

            if (addFlag) {
                LokalityKvadrantAsoc lkaNew = new LokalityKvadrantAsoc(null, kvadrant);
                lkaNew.setId(new LokalityKvadrantAsocId(-1, kvadrant.getId()));
                toUpdate.add(lkaNew);
            }
        }
        return toUpdate;
    }
}
