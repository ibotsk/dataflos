/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 * Defines a contract to a component whose values are taken from a list of values
 * @author Matus
 */
public interface MoznostiCaller {
    
    /**
     * This method is called when a value is selected in the options list.
     * Implementation decides what to do with the selected value.
     * @param selected 
     */
    void setSelected(Object selected);
    
    void clear();
    
    /**
     * This method should implement behavior when a value is double clicked in the options list.
     * @param selected double clicked item
     */
    void addOnDoubleClick(Object selected);
}
