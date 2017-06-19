/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * not used
 * @author Matus
 */
public class EntryList extends JPanel {

    private List<Entry> entries;
    private int fieldLength = 30;

    public EntryList() {
        this.entries = new ArrayList<>();

        JButton addBtn = new JButton("Add");
        addBtn.setAction(new AddEntryAction());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel rngSmplBtns = new JPanel();
        rngSmplBtns.setLayout(new BoxLayout(rngSmplBtns, BoxLayout.X_AXIS));
        rngSmplBtns.add(addBtn);
        this.add(rngSmplBtns);
    }

    public int getFieldLength() {
        return fieldLength;
    }
     
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }
    
    public void cloneEntry(Entry entry) {
        //Object selected = entry.getComboBox().getSelectedItem();
        //JComboBox copy = new JComboBox(comboBoxEntries);
        //copy.setSelectedItem(selected);
        //Entry theClone = new Entry(copy, "", this);
        //addItem(theClone);
    }

    private void addItem(Entry entry) {
        entries.add(entry);
        add(entry);
        refresh();
    }

    public void removeItem(Entry entry) {
        entries.remove(entry);
        remove(entry);
        refresh();
    }

    private void refresh() {
        revalidate();

        /*
         * if (entries.size() == 1) { entries.get(0).enableMinus(false); } else {
         */
        for (Entry e : entries) {
            e.enableMinus(true);
        }
        //}
    }
    
    public List<Entry> getEntries() {
        return this.entries;
    }

    public void clear() {
        this.entries.clear();
    }
    
    public class AddEntryAction extends AbstractAction {

        public AddEntryAction() {
            super("+");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Entry range = new Entry("", getFieldLength(), EntryList.this);
            EntryList.this.addItem(range);
        }
    }

}
