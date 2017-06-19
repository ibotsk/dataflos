/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A component composed of free text field, JList, and Add button.
 * The text field serves for filtering the contents. JList can hold any Entities,
 * but only of same class at once. The type is defined by caller. Add button allows 
 * user to insert new record of the T the list currently shows.
 * Any value in the list can be selected and populated into caller component.
 * @author Matus
 * @param <T>
 */
public class FilterJList<T> extends JPanel {

    private JTextField filterField;
    private JList mainList;
    private JButton btnAddNew;
    private List<T> container = new ArrayList<>(); //uchovava vsetky zaznamy bez filtrovania
    //private MoznostiListModel model = new MoznostiListModel();
    private String search;

    private MoznostiCaller caller;

    public FilterJList() {
        init();
    }

    /**
     * Initialization of components and layout.
     */
    private void init() {
        this.filterField = new JTextField(30);
        this.btnAddNew = new JButton();
        this.btnAddNew.setName("jListMoznostiAdd");
//        this.mainList = new JList(model);
        this.mainList = new JList(new MoznostiListModel());
        this.mainList.setDoubleBuffered(true);

        JScrollPane scroll = new JScrollPane(this.mainList);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(this.filterField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE).addComponent(scroll, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addComponent(this.btnAddNew, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.filterField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE).addComponent(scroll, 560, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(this.btnAddNew, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));

        //text is put into the filter field
        this.filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setSearch(filterField.getText().toLowerCase());
                //refilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setSearch(filterField.getText().toLowerCase());
                //refilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setSearch(filterField.getText().toLowerCase());
                //refilter();
            }
        });

        
        this.mainList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (caller != null) {
                    caller.setSelected(mainList.getSelectedValue());
                }
            }
        });

        //allow items to be double-clicked, let the caller handle
        this.mainList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && caller != null) {
                    caller.addOnDoubleClick(mainList.getSelectedValue());
                }
            }
                                    
        });
    }

    /**
     * Free text field to narrow down shown list contents.
     * @return 
     */
    public JTextField getFilterField() {
        return filterField;
    }

    public void setFilterField(JTextField filterField) {
        this.filterField = filterField;
    }

    /**
     * Add button to insert new record of the Entities currently in the list.
     * @return 
     */
    public JButton getBtnAddNew() {
        return btnAddNew;
    }

    public void setBtnAddNew(JButton btnAddNew) {
        this.btnAddNew = btnAddNew;
    }

    /**
     * JList component of the whole composition.
     * @return 
     */
    public JList getMainList() {
        return mainList;
    }

    public void setMainList(JList mainList) {
        this.mainList = mainList;
    }

    /**
     * Holds all loaded Entities of one class (type). Filtering by text field does not
     * remove object from this list.
     * @return 
     */
    public List<T> getContainer() {
        return container;
    }

    public void setContainer(List<T> container) {
        //this.clear();
        this.filterField.setText(null);
        this.container = container;
        updateModel(container);
    }

    public void setSearch(String search) {
        this.search = search;
        refilter();
    }

//    public DefaultListModel<T> getMoznostiListModel() {
//        return this.model;
//    }
    
    /**
     * Handles selection of records from the list that in any way match text in 
     * the filter field. If the field is empty, all record are shown.
     */
    private void refilter() {
//        this.model.clear();
//        //String search = this.filterField.getText().toLowerCase();
//        if (this.search.isEmpty()) {
//            this.model.addAll(this.container); 
//        } else {
//            for (T entity : this.container) {
//                if (entity.toString().toLowerCase().indexOf(search, 0) != -1) {
//                    this.model.addElement(entity);
//                }
//            }
//        }

        if (this.search.isEmpty()) { //nothing to filter, show complete list
            updateModel(this.container);
        } else { //else select record that contain text in filter field and add those to the list model
            List<T> filtered = new ArrayList<>();
            for (T entity : this.container) {
                if (entity.toString().toLowerCase().indexOf(search, 0) != -1) {
                    filtered.add(entity);
                }
            }
            updateModel(filtered);
        }
    }

    /**
     * Load new entities to the list model.
     * @param entities List to be loaded. Can be null -> empty model is created
     */
    private void updateModel(List<T> entities) {
        ListModel<T> lm = new MoznostiListModel();
        if (entities != null && !entities.isEmpty()) {
            lm = new MoznostiListModel(entities);
        }
        this.mainList.setModel(lm);
    }

    /**
     * Clears container and filter field.
     */
    public void clear() {
//        this.container.clear();
//        //this.model.clear();
//        updateModel(null);
        setContainer(new ArrayList<T>());
        this.filterField.setText(null);
    }

    /**
     * Caller is a component which will be populated by the selected value from list.
     * @return 
     */
    public MoznostiCaller getCaller() {
        return caller;
    }

    public void setCaller(MoznostiCaller caller) {
        this.caller = caller;
    }

    /**
     * ListModel that shows records in the JList component, taking filter text field into account.
     */
    private class MoznostiListModel extends AbstractListModel<T> {

        private List<T> entities = new ArrayList<>();

        public MoznostiListModel() {
        }

        public MoznostiListModel(List<T> entities) {
            this.entities = entities;
        }

//        public void addAll(Collection<T> coll) {
//            for (T entity : coll) {
//                this.addElement(entity);
//            }
//        }
        @Override
        public int getSize() {
            return this.entities.size();
        }

        @Override
        public T getElementAt(int index) {
            return this.entities.get(index);
        }

        public void clear() {
            this.entities = new ArrayList<>();
        }

    }

}
