package sk.sav.bot.dataflos.util;

import java.util.Comparator;
import org.apache.log4j.Logger;
import sk.sav.bot.dataflos.entity.UdajObrazky;

/**
 *
 * @author .jakub
 */

//trieda na porovnavanie medzi 2 obrazkami
public class ComparatorObr implements Comparator<UdajObrazky> {
    

    private static Logger log = Logger.getLogger(ComparatorObr.class.getName());
    
    @Override
    public int compare(UdajObrazky uo1, UdajObrazky uo2) {

        if (uo1 == null || uo2 == null){
            log.error("Obrazky comparator: at least one image is NULL");
            throw new NullPointerException("udajObrazky");
        }

        int imgId1 = uo1.getId();
        int imgId2 = uo2.getId();

        if (imgId1 > imgId2) {
            return -1;
         } else if (imgId1 < imgId2) {
            return 1;
         } else {
            return 0;
         }

    }
}
