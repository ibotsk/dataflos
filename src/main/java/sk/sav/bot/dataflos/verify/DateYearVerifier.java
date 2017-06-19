/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Matus
 */
public class DateYearVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent jc) {
        JTextField yearField = (JTextField) jc;
        String text = yearField.getText();
        if (text.isEmpty()) {
            return true;
        }
        if (!text.isEmpty() && text.length() < 4) {
            return false;
        }
        try {
            int value = Integer.parseInt(yearField.getText());
            Calendar cal = new GregorianCalendar();
            return value > -1 && value < cal.get(Calendar.YEAR) + 1;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
