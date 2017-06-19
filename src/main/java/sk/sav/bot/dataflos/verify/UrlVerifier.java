/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

/**
 *
 * @author Matus
 */
public class UrlVerifier extends InputVerifier {
    
    private static Logger log = Logger.getLogger(UrlVerifier.class.getName());

    @Override
    public boolean verify(JComponent input) {
        if (input == null) {
            log.error("Url to verify is NULL");
            throw new NullPointerException("input");
        }
        if (input instanceof JTextField) {
            JTextField field = (JTextField) input;
            String text = field.getText();
            if (!text.isEmpty()) {
                return text.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
            }
        }
        return false;
    }
}
