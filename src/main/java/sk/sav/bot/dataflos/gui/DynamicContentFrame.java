/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import com.jidesoft.swing.AutoCompletionComboBox;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.interf.Entity;
import sk.sav.bot.dataflos.util.HibernateQuery;
import sk.sav.bot.dataflos.util.SpringUtilities;
import sk.sav.bot.dataflos.util.StdMenoFontTypeRenderer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import sk.sav.bot.dataflos.models.PagingModel;

/**
 * This class represents a frame used for editing the entity or creating the new
 * one. Class has assigned Entity class that operates with. According to the
 * Entity implementing class, the frame is populated with editable components
 * for values input. The order, types and names of the entity attributes are
 * reflected in order, type and label of components. !! Neberie do uvahy prvy a
 * posledny atribut (id, schvalene)!!
 *
 * @author Matus
 */
public class DynamicContentFrame extends JDialog {

    private Entity entity;
    private JPanel jPanel = new JPanel(new SpringLayout());
    private JButton btnOk = new JButton();
    private JButton btnCancel = new JButton();
    private boolean isNew = true;
    private HibernateQuery hq;
    private List<Component> fields = new ArrayList<>();
    private PagingModel model;
    
    private static Logger log = Logger.getLogger(DynamicContentFrame.class.getName());

    public DynamicContentFrame(HibernateQuery hq) {
        this.hq = hq;
        init();
    }

    private void init() {
        this.setModal(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });

        this.clear();
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        this.setLayout(layout);
        this.setTitle("Nový");
        this.setAlwaysOnTop(true);

        jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jPanel).addGroup(layout.createSequentialGroup().addComponent(btnOk).addComponent(btnCancel)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(jPanel).addGroup(layout.createParallelGroup().addComponent(btnOk).addComponent(btnCancel)));

        btnOk.setAction(new BtnOkAction());
        btnOk.setText("Uložiť");
        btnCancel.setAction(new BtnCancelAction());
        btnCancel.setText("Zrušiť");
    }

    /*
     * private Component createComponent(String[] fields) { if (fields.length ==
     * 3) { String[] tokens = fields[2].split(" "); switch (tokens[0]) { case
     * "C": //combo box String[] values = tokens[1].split(","); return new
     * JComboBox<>(values); case "F": List<Entity> ents =
     * this.hq.getAllRecords(tokens[1]); AutoCompletionComboBox ac = new
     * AutoCompletionComboBox(ents.toArray()); ac.setSelectedItem(null); return
     * ac; default: break; } } return new JTextField(30); }
     */
    /**
     * According to the fld type, this method creates either JComboBox for type
     * Character, AutoCompletionComboBox for type implementing interface Entity,
     * or JTextField for any other type. JComboBox is filled with values D, S,
     * since we need this for ListOfSpecies.typ only. If isNew is true, all
     * components will be empty, otherwise components will be set to value fld
     * carries.
     *
     * @param fld Field of type java.lang.reflection.Field
     * @param isNew true for empty component
     * @return editable component
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Component createComponent(Field fld, boolean isNew) throws IllegalArgumentException, IllegalAccessException {
        if (fld == null) {
            log.error("createComponent: supplied field is NULL");
            throw new NullPointerException("fld");
        }
        log.info("Setting up component field "+fld.getName()+" of type "+fld.getType().getSimpleName());
        Class[] intfcs = fld.getType().getInterfaces();
        if (fld.getType().equals(Character.class)) {
            
            JComboBox cb = null;
            
            if (this.entity.getClass().equals(LitZdroj.class)){
                String[] values = {" "}; // TODO: bude sa potencialne dorabat? ak ide o typ z LitZdroja{"K", "Č", "R"}
                cb = new JComboBox<>(values);
            } else if (this.entity.getClass().equals(ListOfSpecies.class)){
                String[] values = {"A", "S"};
                cb = new JComboBox<>(values);
                if (!isNew && this.entity != null) {
                    if (fld.get(this.entity) != null){
                        cb.setSelectedIndex(((char) fld.get(this.entity) == 'A') ? 0 : 1);
                    }
                }
            }

            return cb;
            
        } else if (fld.getType().equals(boolean.class)){
            String[] values = {"áno", "nie"};
            JComboBox cb = new JComboBox<>(values);
            if (!isNew && this.entity != null) {
                if (fld.get(this.entity) != null){
                    cb.setSelectedIndex((boolean) fld.get(this.entity) == true ? 0 : 1);
                }
            }
            // uzivatel iny ako admin, nema moznost menit schvalenie polozky
            if (fld.getName().equals("schvalene") && !hq.isAdmin()){
                cb.setEnabled(false);
            }
            return cb;
        } else if (intfcs.length > 1 && intfcs[1].equals(Entity.class)) {
            
            String column = "meno";
            
            // niektory z atributov LOS (classy zacinajuce s "Taxon"): pochybnost, povodnost, ohrozenost, endemizmus
            if (fld.getType().getSimpleName().startsWith("Taxon")){
                column = "skratka";
            }
            
            List<Entity> ents = this.hq.getAllRecords(fld.getType().getCanonicalName(), column);
            
            // v pripade tohto pola - "Akceptovane meno", sa v ponuke ukazu nie vsetky polozky z ListOfSpecies, ale iba tie akceptovane
            if (fld.getType().equals(ListOfSpecies.class)){
                
                //zobraz iba akceptovane mena
                List<Entity> entListTMP = new ArrayList<>(ents);
                ents.clear();
                for (Entity entTMP : entListTMP){
                    ListOfSpecies valueLOS = (ListOfSpecies) entTMP;
                    if (valueLOS.getTyp() != null && valueLOS.getTyp() == 'A'){
                        ents.add(entTMP);
                    }
                }
            }
                
            AutoCompletionComboBox ac = new AutoCompletionComboBox(ents.toArray());
            ac.insertItemAt(null, 0);
            if (!isNew && this.entity != null) {
                ac.setSelectedItem(fld.get(this.entity));
            } else {
                ac.setSelectedItem(null);
            }
            
            // v pripade pola LOS odlisime formatovanim schvalene a neschvalene akc. mena
            if (fld.getType().equals(ListOfSpecies.class)){
                ac.setRenderer(new StdMenoFontTypeRenderer());
            }
            
            return ac;
        }
        JTextField tf = new JTextField(30);
        if (!isNew && this.entity != null) {
            tf.setText(fld.get(this.entity) == null ? "" : fld.get(this.entity).toString());
        }
        return tf;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * This method sets entity and populates this frame with components.
     *
     * @param entity entity assigned to this frame o operate with
     */
    public void setEntity(Entity ent) {
        try {
            Object obj = ent;
            if (ent instanceof HibernateProxy){   
                obj = ((HibernateProxy) ent).getHibernateLazyInitializer().getImplementation();   
            }
            entity = (Entity) obj;
            int size = 0;
            ResourceBundle rsc = PropertyResourceBundle.getBundle("resource");
            if (rsc == null){
                log.error("Resource properties not found");
                throw new MissingResourceException("Property file not found!", "org.apache.xerces.impl.msg.XMLSchemaMessages", "");
            }
            Field[] flds = entity.getClass().getDeclaredFields();
            String entityName = entity.getClass().getSimpleName();
            log.info("Setting up dynamic content frame with entity of type "+entityName);
            
            if (entityName.contains("_")){
               log.error("Problem with HibernateLazyInitializer. Entity name is not valid"); 
               throw new IllegalArgumentException("Problem with HibernateLazyInitializer transformation of argument");
            } else {
                this.setTitle(entityName);
            }
            setFieldsAccessible(flds, true);
            for (Field fld : flds) {
                if (!fld.getName().equals("id") && !fld.getName().equals("udaj") && !fld.getType().equals(Set.class)) { // ? !fld.getName().equals("typ") ?
                    JLabel lab = new JLabel(rsc.getString(fld.getName()), JLabel.TRAILING);
                    jPanel.add(lab);
                    Component tf = createComponent(fld, this.isNew);
                    lab.setLabelFor(tf);
                    jPanel.add(tf);
                    fields.add(tf);
                    size++;
                }
            }
            setFieldsAccessible(flds, false);
            SpringUtilities.makeCompactGrid(jPanel, size, 2, 6, 6, 6, 6);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error("DCF - Set entity exception", ex);
        }
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public HibernateQuery getHq() {
        return hq;
    }

    public void setHq(HibernateQuery hq) {
        this.hq = hq;
    }

    public void showFrame(PagingModel model) {
        this.model = model;
        this.pack();
        this.setVisible(true);
    }

    private void clear() {
        jPanel.removeAll();
        fields.clear();
    }

    private void closeDialog() {
        clear();
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Akcia, ktora je priradena tlacitku OK.
     */
    private class BtnOkAction extends AbstractAction {

        /**
         * Vytvori novu instanciu triedy, ktora je zhodna s typom atributu entity.
         * Z poli vyberie hodnoty a nastavi ich novej instancii. Na koniec vlozi 
         * novu entitu do databazy, atribut entity nastavi na vytvorenu instanciu a zavrie okno.
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                log.info("Entity to be saved.");
                boolean canSave = true;
                Class cls = entity.getClass();
                // kontrola - ak synonymum, musi mat uvedene akc. meno, v opacnom pripade nemoze mat
                if (entity.getClass().getSimpleName().equals("ListOfSpecies")){
                    Component component = fields.get(0);
                    JComboBox cb = (JComboBox) component;
                    String value = (String) cb.getSelectedItem();
                    switch (value) {
                        case "S":
                            {
                                Component component2 = fields.get(3);
                                AutoCompletionComboBox ac2 = (AutoCompletionComboBox) component2;
                                if (ac2.getSelectedItem() == null){
                                    canSave = false;
                                    log.warn("Entity cannot be saved - not specified accepted name for synonymum");
                                    JOptionPane.showMessageDialog(DynamicContentFrame.this, "Neuvedené akceptované meno pre synonymum", "Chyba", JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                            }
                        case "A":
                            {
                                Component component2 = fields.get(3);
                                AutoCompletionComboBox ac2 = (AutoCompletionComboBox) component2;
                                if (ac2.getSelectedItem() != null){
                                    canSave = false;
                                    log.warn("Entity cannot be saved - specified accepted name for accepted name");
                                    JOptionPane.showMessageDialog(DynamicContentFrame.this, "Uvedené akceptované meno pre akceptované meno", "Chyba", JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                            }
                    }
                }

                if (canSave){
                    Entity entToSave;
                    if (isNew) {
                        entToSave = (Entity) cls.newInstance();
                    } else {
                        entToSave = entity;
                    }
                    
                    log.info("Saving entity...");
                    
                    Field[] flds = cls.getDeclaredFields();

                    setFieldsAccessible(flds, true);
                    //get component value and set the field
                    for (int i = 0; i < fields.size(); i++) {
                        Component component = fields.get(i);
                        Object value = null;
                        if (component instanceof AutoCompletionComboBox) {
                            AutoCompletionComboBox ac = (AutoCompletionComboBox) component;
                            if (ac.getSelectedItem() == null){
                                value = null;
                            } else {
                                value = (Entity) ac.getSelectedItem();
                            }
                        } else if (component instanceof JTextField) {
                            JTextField tf = (JTextField) component;
                            if (!tf.getText().isEmpty()) {
                                // podla typu field-u priradime hodnotu
                                if (flds[i+1].getType().equals(Integer.TYPE)) {
                                    value = Integer.valueOf((String) tf.getText());
                                } else {
                                    value = (String) tf.getText();
                                }
                            }
                        } else if (component instanceof JComboBox) {

                            JComboBox cb = (JComboBox) component;
                            String sel = (String) cb.getSelectedItem();
                            if (flds[i+1].getType().equals(Character.class)) {
                                value = (char) sel.charAt(0);
                            } else if (flds[i+1].getType().equals(boolean.class)) {
                                // ak ide o field "schvalene" a pouzivatel nie je admin, automaticky nastav "false"
                                if (flds[i+1].getName().equals("schvalene") && !hq.isAdmin()){
                                    value = false;
                                } else {
                                    value = sel.equals("áno");
                                }
                            } else {
                                value = sel;
                            }
                        } 

                        flds[i+1].set(entToSave, value);
                    }

                    setFieldsAccessible(flds, false);
                    hq.insertOrUpdateEntity(entToSave);
                    log.info("Entity saved successfully.");
                    closeDialog();
                    if (isNew) {
                        model.addRow(entToSave);
                    }
                }    
            } catch (InstantiationException | IllegalAccessException ex) {
                log.error("Exception while saving entity", ex);
            }
        }
    }

    private class BtnCancelAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.info("Window closed without save.");
            closeDialog();
        }
    }

    private void setFieldsAccessible(Field[] fields, boolean isAccessible) {
        for (Field field : fields) {
            field.setAccessible(isAccessible);
        }
    }
}
