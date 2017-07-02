
package sk.sav.bot.dataflos.entity.interf;

import java.util.List;

/**
 * Tables in databases must implement this interface. It allows a dialog box for
 * each table to be create on the fly.
 * @author Matus
 */
public interface AssociableEntity {
    
    /**
     * Defines a mapping of table fields names and values
     * @return 
     */
    List<String[]> namesAndValues();
        
}
