package sk.sav.bot.dataflos.util;

import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.Udaj;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author .jakub
 */

//trieda na porovnavanie medzi 2 FtgOkres objektami
public class ComparatorFTG implements Comparator<Udaj> {
    
    // mnozina tych ftg okresov, ktore v ktorych sa usporiadavaju kvadranty od najzapadnejsie polozeneho po najvychodnejsie
    private static final Set<String> ftgOkresSortZV = new HashSet<>(Arrays.asList(new String[] { "1", "2", "3", "5", "6", "7", "9", "14", "14d", "14f", "15", "16", "17", "20", "21b", "21d", "22", "23a", "23b", "23c", "24", "26a", "28", "29", "30", "30c", "31" }));

    private static Logger log = Logger.getLogger(ComparatorFTG.class.getName());
        @Override
        public int compare(Udaj u1, Udaj u2) {
            
            if (u1 == null || u2 == null){
                log.error("Ftg comparator: at least one udaj is NULL");
                throw new NullPointerException("udaj");
            }
            
            Ftgokres ftg1 = null;
            Ftgokres ftg2 = null;
            Set u1ftgs, u2ftgs;
            
            //z obidvoch mnozin ftgokresov zoberieme ten "najmensi"(s najnizsim cislom) a tieto budeme porovnavat, ak prazdna mnozina -> ftgokres = null
            if (u1.getLokality() != null && !(u1ftgs = u1.getLokality().getLokalityFtgokresAsocs()).isEmpty()){
                ftg1 = getSmallestFtgokres(u1ftgs);
            }
            if (u2.getLokality() != null && !(u2ftgs = u2.getLokality().getLokalityFtgokresAsocs()).isEmpty()){
                ftg2 = getSmallestFtgokres(u2ftgs);
            }
            if (ftg2 == null){
                if (ftg1 == null){
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (ftg1 == null){
                    return 1;
                } else {
                    String ftg1num = ftg1.getCislo();
                    String ftg2num = ftg2.getCislo();
                    if (ftg1num.length() < 2){ ftg1num = "0"+ftg1num; }
                    if (ftg2num.length() < 2){ ftg2num = "0"+ftg2num; }

                    if (ftg1num.compareTo(ftg2num) == 0){
                        // ak sa ftgokresy zhoduju, je potrebne ich porovnat podla kvadrantov
                        return compareFtgByKvadrant(ftg1, u1.getLokality().getLokalityKvadrantAsocs(), u2.getLokality().getLokalityKvadrantAsocs());
                    } else {
                        return ftg1num.compareTo(ftg2num);
                    }
                }
            }         
        }

        private int compareFtgByKvadrant(Ftgokres ftg, Set kvadranty1, Set kvadranty2) {
            
            if (kvadranty1 == null || kvadranty2 == null){
                log.error("CompareFtgByKvadrant: at least on set of kvadrants is NULL ");
                throw new NullPointerException("lokalityKvadrantAsocs");
            }
            
            boolean kvad1Empty = kvadranty1.isEmpty();
            boolean kvad2Empty = kvadranty2.isEmpty();
            
            if (kvad2Empty){
                if (kvad1Empty){
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (kvad1Empty){
                    return 1;
                } else {
            
                    if (ftgOkresSortZV.contains(ftg.getCislo())){
                        Kvadrant kvadrant1 = getSmallestKvadrant("ZV", kvadranty1);
                        Kvadrant kvadrant2 = getSmallestKvadrant("ZV", kvadranty2);
                        String [] kvadLettersSortZV = new String[]{"a", "c", "b", "d"};
                        // zoraduje sa od najmensich cisiel
                        if (kvadrant1.getMeno().substring(2, 5).compareTo(kvadrant2.getMeno().substring(2, 5)) < 0){
                            return -1;
                        } else if (((kvadrant1.getMeno().substring(2, 5).compareTo(kvadrant2.getMeno().substring(2, 5)) == 0) && (Arrays.asList(kvadLettersSortZV).indexOf(kvadrant1.getMeno().substring(5, 6)) < Arrays.asList(kvadLettersSortZV).indexOf(kvadrant2.getMeno().substring(5, 6))))){
                            return -1;
                        } else if (((kvadrant1.getMeno().substring(2, 5).compareTo(kvadrant2.getMeno().substring(2, 5)) == 0) && (Arrays.asList(kvadLettersSortZV).indexOf(kvadrant1.getMeno().substring(5, 6)) == Arrays.asList(kvadLettersSortZV).indexOf(kvadrant2.getMeno().substring(5, 6))))){
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        Kvadrant kvadrant1 = getSmallestKvadrant("JS", kvadranty1);
                        Kvadrant kvadrant2 = getSmallestKvadrant("JS", kvadranty2);
                        String [] kvadLettersSortJS = new String[]{"c", "d", "a", "b"};
                        // zoraduje sa od najvacsich cisiel
                        if (kvadrant1.getMeno().substring(0, 2).compareTo(kvadrant2.getMeno().substring(0, 2)) > 0){
                            return -1;
                        } else if (((kvadrant1.getMeno().substring(0, 2).compareTo(kvadrant2.getMeno().substring(0, 2)) == 0) && (Arrays.asList(kvadLettersSortJS).indexOf(kvadrant1.getMeno().substring(5, 6)) < Arrays.asList(kvadLettersSortJS).indexOf(kvadrant2.getMeno().substring(5, 6))))){
                            return -1;
                        } else if (((kvadrant1.getMeno().substring(0, 2).compareTo(kvadrant2.getMeno().substring(0, 2)) == 0) && (Arrays.asList(kvadLettersSortJS).indexOf(kvadrant1.getMeno().substring(5, 6)) == Arrays.asList(kvadLettersSortJS).indexOf(kvadrant2.getMeno().substring(5, 6))))){
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                }
            }
        }
        
    // vyber najmensieho ftgokresu zavisi od priradeneho cisla
    public static Ftgokres getSmallestFtgokres(Set lokalityFtgokresAsocs) {
        
        if (lokalityFtgokresAsocs == null){
            log.error("GetSmallestFtgokres: lokalityFtgokresAsocs in NULL");
            throw new NullPointerException("lokalityFtgokresAsocs");
        }
        
        List<Ftgokres> ftgokresy = new ArrayList<>();
        for (Object object : lokalityFtgokresAsocs) {
            LokalityFtgokresAsoc lfa = (LokalityFtgokresAsoc) object;
            ftgokresy.add(lfa.getFtgokres());
        }
        Ftgokres smallestFtg = null;
        String smallestFtgCislo = "99e";
        for (Ftgokres ftgokres : ftgokresy) {
            String ftgCislo = (ftgokres.getCislo().length() < 2) ? "0"+ftgokres.getCislo() : ftgokres.getCislo();
            if (ftgCislo.compareTo(smallestFtgCislo) < 0) {
                smallestFtgCislo = ftgCislo;
                smallestFtg = ftgokres;
            }
        }
        return smallestFtg;
    }
    
    // vyber najmensieho kvadrantu zavisi od orientacie ftg okresu
    // pre ZV a JS orientacie su odlisne pristupy porovnavania
    private static Kvadrant getSmallestKvadrant(String orientation, Set kvadranty) {
        List<Kvadrant> quadrants = new ArrayList<>();
        for (Object object : kvadranty) {
            LokalityKvadrantAsoc lka = (LokalityKvadrantAsoc) object;
            quadrants.add(lka.getKvadrant());
        }

        Kvadrant smallestKvadrant = null;

        if (orientation.equals("ZV")){ // ak zoradujeme podla Z - V, je rozhodujuce : 3., 4. a 5. cislo + a, c, b, d - zoradovat od najmensieho
            String smallestKvadrantNum = "99";
            String smallestKvadrantLetter = "d";
            for (Kvadrant quadrant : quadrants) {
                String [] kvadLettersSortZV = new String[]{"a", "c", "b", "d"};
                if (quadrant.getMeno().length() >= 0) {
                    if (quadrant.getMeno().length() < 6) {
                        quadrant.setMeno(quadrant.getMeno().concat("d"));
                    }
                    if ((quadrant.getMeno().substring(2, 5).compareTo(smallestKvadrantNum) < 0) || ((quadrant.getMeno().substring(2, 5).compareTo(smallestKvadrantNum) == 0) && Arrays.asList(kvadLettersSortZV).indexOf(quadrant.getMeno().substring(5, 6)) < Arrays.asList(kvadLettersSortZV).indexOf(smallestKvadrantLetter))) {
                        smallestKvadrantNum = quadrant.getMeno().substring(2, 5);
                        smallestKvadrantLetter = quadrant.getMeno().substring(5, 6);
                        smallestKvadrant = quadrant;
                    }
                }
            }
            return smallestKvadrant;
        } else { // ak zoradujeme podla J - S, je rozhodujuce : 1. a 2. cislo + c, d, a, b - zoradovat od najvacsieho
            String smallestKvadrantNum = "0";
            String smallestKvadrantLetter = "b";
            for (Kvadrant quadrant : quadrants) {
                String [] kvadLettersSortJS = new String[]{"c", "d", "a", "b"};
                if (quadrant.getMeno().length() >= 0) {
                    if (quadrant.getMeno().length() < 6) {
                        quadrant.setMeno(quadrant.getMeno().concat("b"));
                    }
                    if ((quadrant.getMeno().substring(0, 2).compareTo(smallestKvadrantNum) > 0) || ((quadrant.getMeno().substring(0, 2).compareTo(smallestKvadrantNum) == 0) && Arrays.asList(kvadLettersSortJS).indexOf(quadrant.getMeno().substring(5, 6)) < Arrays.asList(kvadLettersSortJS).indexOf(smallestKvadrantLetter))) {
                        smallestKvadrantNum = quadrant.getMeno().substring(0, 2);
                        smallestKvadrantLetter = quadrant.getMeno().substring(5, 6);
                        smallestKvadrant = quadrant;
                    }
                }
            }
            return smallestKvadrant;
        }
    }
}
