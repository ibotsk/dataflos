/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.util.DisplayEntities;
import javax.swing.AbstractListModel;

/**
 *
 * @author .jakub
 */
    /*
     * Models
     */
public class TableListModel extends AbstractListModel<String> {
    
    DisplayEntities dispEnt = null;
    
    public TableListModel(){
        
    }
    
    public TableListModel(DisplayEntities dispEnt){
        this.dispEnt = dispEnt;
    }
    
    public void setDispEntities(DisplayEntities dispEnt){
        this.dispEnt = dispEnt;
    }

    @Override
    public int getSize() {
        return dispEnt.getEntitiesToDisplay().size();
    }

    @Override
    public String getElementAt(int index) {
        return dispEnt.getEntitiesToDisplay().get(index);
    }
}
