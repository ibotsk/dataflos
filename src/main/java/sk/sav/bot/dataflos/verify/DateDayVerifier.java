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
public class DateDayVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent jc) {
        JTextField dayField = (JTextField) jc;
        String text = dayField.getText();
        if (text.isEmpty()) {
            return true;
        }
        if (!text.isEmpty() && text.length() < 2) {
            return false;
        }
        try {
            int value = Integer.parseInt(dayField.getText());
            
            return value > -1 && value < 32;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
}
