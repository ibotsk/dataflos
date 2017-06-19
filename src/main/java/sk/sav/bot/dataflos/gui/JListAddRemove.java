/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * A component designed to hold multiple values. Consists of JList, add button, 
 * and remove button.
 *
 * @author Matus
 * @param <T>
 */
public class JListAddRemove<T> extends JPanel implements MoznostiCaller {

    private JList<T> mainList = new JList();
    private JButton btnAdd = new JButton("<<");
    private JButton btnRemove = new JButton(">>");
    //private JList<T> sourceList;
    private List<T> container = new ArrayList<>();

    private T selected;

    public JListAddRemove() {
        init();
    }

    public JListAddRemove(boolean necessary) {
        if (necessary) {
            this.mainList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        }
        init();
    }

    public JListAddRemove(JList<T> sourceList) {
        //this.sourceList = sourceList;
        init();
    }

    public JListAddRemove(JList<T> sourceList, boolean necessary) {
        //this.sourceList = sourceList;
        if (necessary) {
            this.mainList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        }
        init();
    }

    //ak chceme inu sirku komponentu ako standardne zadanu
    public JListAddRemove(int width) {
        init();
        this.mainList.setPreferredSize(new Dimension(width, 82));
    }

    public JListAddRemove(boolean necessary, int width) {
        if (necessary) {
            this.mainList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        }
        init();
        this.mainList.setPreferredSize(new Dimension(width, 82));
    }

    public JListAddRemove(JList<T> sourceList, boolean necessary, int width) {
        //this.sourceList = sourceList;
        if (necessary) {
            this.mainList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        }
        init();
        this.mainList.setPreferredSize(new Dimension(width, 82));
    }

    /**
     * Initializes basic components, layout, and listeners on buttons
     */
    private void init() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        this.mainList.setVisibleRowCount(8);
        this.mainList.setPreferredSize(new Dimension(380, 130));

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(this.mainList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup().addComponent(btnAdd).addComponent(btnRemove)));

        layout.setVerticalGroup(
                layout.createParallelGroup().addComponent(this.mainList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(btnAdd).addComponent(btnRemove)));

        this.mainList.setModel(new AddRemoveListModel());

        //remove item if double clicked
        this.mainList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && mainList.getSelectedValue() != null) {
                    btnRemove.doClick();
                }
            }
            
        });

        this.btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    container.add(selected);
                    refreshList();
                }
            }
        });

        this.btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                T selected = mainList.getSelectedValue();
                if (selected != null) {
                    container.remove(selected);
                    refreshList();
                }
            }
        });

    }

    /**
     * Add button. 
     * @return 
     */
    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    /**
     * Remove button.
     * @return 
     */
    public JButton getBtnRemove() {
        return btnRemove;
    }

    public void setBtnRemove(JButton btnRemove) {
        this.btnRemove = btnRemove;
    }

    /**
     * List component.
     * @return 
     */
    public JList getMainList() {
        return mainList;
    }

    public void setMainList(JList<T> mainList) {
        this.mainList = mainList;
    }

//    public JList getSourceList() {
//        return sourceList;
//    }
//    public void setSourceList(JList<T> sourceList) {
//        this.sourceList = sourceList;
//    }
    /**
     * Main container that holds values. They are shown in the JList component.
     * @return 
     */
    public List<T> getContainer() {
        return this.container;
    }

    public void setContainer(List<T> container) {
        this.container = container;
        this.mainList.setModel(new AddRemoveListModel());
    }

    public void setListPrefferedSize(Dimension prefSize) {
        this.mainList.setPreferredSize(prefSize);
        this.mainList.repaint();
    }

    public void setVisibleRowCount(int count) {
        this.mainList.setVisibleRowCount(count);
        this.mainList.repaint();
    }

    public void refreshList() {
        this.mainList.setModel(new AddRemoveListModel());
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.mainList.setEnabled(enabled);
        this.btnAdd.setEnabled(enabled);
        this.btnRemove.setEnabled(enabled);
        if (enabled) {
            this.getMainList().setBackground(Color.WHITE);
        } else {
            this.getMainList().setBackground(new Color(222, 225, 229));
        }
    }

    @Override
    public void clear() {
        this.container.clear();
        this.mainList.setModel(new AddRemoveListModel());
    }

    @Override
    public void setSelected(Object selected) {
        this.selected = (T) selected;
    }

    @Override
    public void addOnDoubleClick(Object selected) {
        setSelected(selected);
        this.btnAdd.doClick(); //we want to add item to the list immediately
    }

    private class AddRemoveListModel extends AbstractListModel<T> {

        @Override
        public int getSize() {
            return container.size();
        }

        @Override
        public T getElementAt(int index) {
            return container.get(index);
        }
    }
}
