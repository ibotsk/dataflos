/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import com.jidesoft.swing.AutoCompletionComboBox;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.ComboBoxModel;
//import javax.swing.JComboBox;

/**
 *
 * @author Jakub
 */
public class ResizeableComboBox extends AutoCompletionComboBox {
  
    public ResizeableComboBox() { 
    } 
 
    public ResizeableComboBox(final Object items[]){ 
        super(items);
    } 
 
    public ResizeableComboBox(Vector items) { 
        super(items);
    } 
 
    public ResizeableComboBox(ComboBoxModel aModel) { 
        super(aModel);
        
    }
    
    private boolean layingOut = false; 
 
    public void doLayout(){ 
        try{ 
            layingOut = true; 
            super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 
 
    public Dimension getSize(){ 
        Dimension dim = super.getSize(); 
        if(!layingOut) 
            dim.width = Math.max(dim.width, getPreferredSize().width);
        return dim; 
    }
    
    public void insertItemAt(Object item, int i){
        super.insertItemAt(item, i);
    }
    
}
