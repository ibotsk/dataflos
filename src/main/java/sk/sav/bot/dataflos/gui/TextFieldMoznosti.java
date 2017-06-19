/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 *
 * @author Matus
 */
public class TextFieldMoznosti extends JPanel implements MoznostiCaller {

    private Entity entity;
//    private JList moznosti;
    private JTextField textField;
//    private JButton btnAdd = new JButton();
    private JButton btnClear = new JButton();
    private String type;

    public TextFieldMoznosti() {
        super(new BorderLayout());
        this.textField = new JTextField(50);
        //this.textField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        this.type = null;
        init();
    }

    public TextFieldMoznosti(final JList moznosti, String type) {
        super(new BorderLayout());
//        this.moznosti = moznosti;
        this.type = type;
        init();
        //this.moznostiAddListener();
        //this.textFieldAddListener();
    }

    public TextFieldMoznosti(int textFieldWidth, String type) {
        super(new BorderLayout());
        this.textField = new JTextField(textFieldWidth);
        this.type = type;
        init();
    }

    public TextFieldMoznosti(int textFieldWidth, String type, boolean necessary) {
        super(new BorderLayout());
        this.textField = new JTextField(textFieldWidth);
        this.type = type;

        if (necessary) {
            //simulacia paddingu pomocou zlozenych ramcekov pricom vnutorny je priesvitny
            Border greenBorder = BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2);
            Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
            Border borderGreenWithPadding = new CompoundBorder(greenBorder, emptyBorder);
            this.textField.setBorder(borderGreenWithPadding);
        }
        init();
    }

    private void init() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(this.textField)
                .addComponent(this.btnClear));
        layout.setVerticalGroup(layout.createParallelGroup().addComponent(this.textField)
                .addComponent(this.btnClear));

        this.btnClear.setAction(new BtnClearAction(null, new ImageIcon(getClass().getResource("/Eraser-icon.png"))));
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (this.entity != null) {
            textField.setText(this.entity.toString());
        } else {
            textField.setText(null);
        }
    }

//    public JList getMoznosti() {
//        return moznosti;
//    }
//
//    public void setMoznosti(JList moznosti) {
//        this.moznosti = moznosti;
//        //this.moznostiAddListener();
//    }
//    public JButton getBtnAdd() {
//        return btnAdd;
//    }
//
//    public void setBtnAdd(JButton btnAdd) {
//        this.btnAdd = btnAdd;
//    }

    public JButton getBtnClear() {
        return btnClear;
    }

    public void setBtnClear(JButton btnClear) {
        this.btnClear = btnClear;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void addFocusListener(FocusListener listener) {
        this.textField.addFocusListener(listener);
//        this.btnAdd.addFocusListener(listener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.textField.setEnabled(enabled);
        this.btnClear.setEnabled(enabled);
    }

//    public void clearFull() {
//        clearMoznosti();
//        this.entity = null;
//        this.textField.setText(null);
//    }
//    public void clearMoznosti() {
//        this.moznosti = null;
//    }
//    private void moznostiAddListener() {
//        if (this.moznosti != null && this.moznosti.getListSelectionListeners().length == 0) {
//            this.moznosti.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//
//                @Override
//                public void valueChanged(ListSelectionEvent e) {
//                    System.out.println("changed");
//                        Object obj = moznosti.getSelectedValue();
//                        //ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//                        
//                        Object selected = obj;
//                        if (obj instanceof HibernateProxy) {
//                            selected = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
//                        }
//                        if (selected != null && selected.getClass().getCanonicalName().equals(type)) {
//                            Entity ent = (Entity) selected;
//                            setEntity(ent);
//                        }
//                    }
//            });
//        }
//    }
    @Override
    public void setSelected(Object source) {
        if (source instanceof Entity) {
            Entity ent = (Entity) source;
            setEntity(ent);
        }
    }

    @Override
    public void clear() {
        this.entity = null;
        this.textField.setText(null);
    }

    @Override
    public void addOnDoubleClick(Object selected) {
        //do nothing, item is added in setSelected()
    }

    private class BtnClearAction extends AbstractAction {

        public BtnClearAction() {
            super();
        }

        public BtnClearAction(String name, Icon icon) {
            super(name, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textField.setText(null);
            setEntity(null);
        }

    }
}
