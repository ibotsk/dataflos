/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import sk.sav.bot.dataflos.entity.ListOfSpecies;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jakub
 */
// trieda na nastavenie odlisnych atributov pisma pre zoznam ListOfSpecies v listMoznosti
public class StdMenoFontTypeRenderer implements ListCellRenderer{
    
    public DefaultListCellRenderer defau = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){

        JLabel renderer = (JLabel) defau.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // pre prazdne resp. zoznamy odlisne od ListOfSpecies nastavujeme klasicke jednoduche pismo
        if (value == null || !value.getClass().getSimpleName().equals("ListOfSpecies")){
            renderer.setBackground(list.getBackground());
            renderer.setFont(renderer.getFont().deriveFont(Font.PLAIN));
            renderer.setForeground(Color.BLACK);
            renderer.setEnabled(list.isEnabled());
            renderer.setOpaque(true);
            return renderer;

        } else { // ak renderujeme zoznam z ListOfSpecies
            
            ListOfSpecies valueLOS = (ListOfSpecies) value;
                // ak meno nie je schvalene, ma jednoduchu hrubku a sedu farbu
                if (!valueLOS.isSchvalene()){
                    renderer.setBackground(list.getBackground());
                    renderer.setFont(renderer.getFont().deriveFont(Font.PLAIN));
                    renderer.setForeground(new Color(163,163,163));
                } else { 
                    if (valueLOS.getTyp() != null){
                        // ak ma nazov druhu zadany typ bude vyobrazene zelenou farbou
                        // ak pojde o akceptovane meno (typ A) bude boldom a ak o synonymum (typ S) jednoduchej hrubky
                        renderer.setBackground(list.getBackground());
                        renderer.setForeground(new Color(85,128,59));
                        if (valueLOS.getTyp() == 'A'){ 
                            renderer.setFont(renderer.getFont().deriveFont(Font.BOLD));
                        } else if (valueLOS.getTyp() == 'S') {
                            renderer.setFont(renderer.getFont().deriveFont(Font.PLAIN));
                        }
                    } else {
                        // ak nie je zadany typ, tak aktualne maju tieto mena jednoduchu hrubku a sedu farbu
                        // TODO: idealne treba toto opravit v DB, aby ziaden LOS nemal informaciu o type nullovu
                        renderer.setBackground(list.getBackground());
                        renderer.setFont(renderer.getFont().deriveFont(Font.PLAIN));
                        renderer.setForeground(new Color(163,163,163));
                    }
                }
            
            renderer.setEnabled(list.isEnabled());
            renderer.setOpaque(true);
            return renderer;

        }
    }
    
}
