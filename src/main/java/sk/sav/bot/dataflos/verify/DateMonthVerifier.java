/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Matus
 */
public class DateMonthVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent jc) {
        JTextField monthField = (JTextField) jc;
        String text = monthField.getText();
        if (text.isEmpty()) {
            return true;
        }
        if (!text.isEmpty() && text.length() < 2) {
            return false;
        }
        try {
            int value = Integer.parseInt(monthField.getText());
            
            return value > -1 && value < 13;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
}
