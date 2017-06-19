/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author jakub.husar
 */
public class PositiveNumberVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        JTextField yearField = (JTextField) input;
        String text = yearField.getText();
        if (text.isEmpty()) {
            return true;
        } else {
            try  
            {  
              int i = Integer.parseInt(text);
              if (i <= 0){
                  return false;
              }
            }  
            catch(NumberFormatException nfe)  
            {  
              return false;  
            }  
            return true;
        }
    }
    
}
