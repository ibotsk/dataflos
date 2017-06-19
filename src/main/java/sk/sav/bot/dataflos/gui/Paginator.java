/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Matus
 */
public class Paginator extends JPanel {

    private final JButton previous = new JButton("<");
    private final JButton next = new JButton(">");
    private List<JButton> pageBttns = new ArrayList<>();
    private PagingTableInterf model;

    public Paginator() {
        super(new BorderLayout());
    }
    
    public Paginator(PagingTableInterf modell) {
        super(new BorderLayout());
        
        this.model = modell;
        for (int i = 1; i < this.model.getPageCount() + 1; i++) {
            pageBttns.add(new JButton(String.valueOf(i)));
        }
        this.previous.setEnabled(false);
        this.pageBttns.get(0).setEnabled(false);
        JPanel p = new JPanel(new BorderLayout());
        p.add(this.previous, BorderLayout.NORTH);
        for (JButton jButton : pageBttns) {
            p.add(jButton, BorderLayout.NORTH);
        }
        p.add(this.next, BorderLayout.NORTH);
        this.add(p);
        
        if (this.model != null) {
            this.previous.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.pageDown();
                }
            });
            this.next.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    model.pageUp();
                }
            });
        }
        
    }
    
    public void setModel(PagingTableInterf model) {
        this.model = model;
    }
    
}
