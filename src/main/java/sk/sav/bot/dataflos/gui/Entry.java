/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * not used
 * @author Matus
 */
public class Entry extends JPanel {

    private JTextField textField;
    private JButton plus;
    private JButton minus;
    private EntryList parent;

    public Entry(String textFieldText, int textFieldLength, EntryList list) {
        this.parent = list;
        this.minus = new JButton(new RemoveEntryAction());
        this.textField = new JTextField(textFieldLength);
        this.textField.setText(textFieldText);
        add(this.minus);
        add(this.textField);
    }

    public class RemoveEntryAction extends AbstractAction {

        public RemoveEntryAction() {
            super("-");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.removeItem(Entry.this);
        }
    }

    public void enableAdd(boolean enabled) {
        this.plus.setEnabled(enabled);
    }

    public void enableMinus(boolean enabled) {
        this.minus.setEnabled(enabled);
    }
    
    public String getText() {
        return this.textField.getText();
    }
}
