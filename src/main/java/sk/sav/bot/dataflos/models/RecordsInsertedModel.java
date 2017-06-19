/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.UdajZberAsoc;
import sk.sav.bot.dataflos.gui.PagingTableInterf;
import sk.sav.bot.dataflos.util.HandyUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Matus
 */
public class RecordsInsertedModel extends AbstractTableModel implements PagingTableInterf {

    List<Udaj> data;
    String username;
    
    protected int pageSize;
    protected int pageOffset;

    public RecordsInsertedModel() {
        this(0, 100, new ArrayList<Udaj>());
    }

    public RecordsInsertedModel(List<Udaj> data) {
        this(data == null ? 0 : data.size(), 100, data);
    }

    public RecordsInsertedModel(int numRows, int size, List<Udaj> data) {
        this.data = data;
        this.pageSize = size;
        this.pageOffset = 0;
    }
    
    @Override
    public int getRowCount() {
        if (this.pageOffset == getPageCount() - 1) {
            return getRealRowCount() - this.pageOffset * this.pageSize;
        }
        return Math.min(this.pageSize, this.data == null ? 0 : this.data.size());
    }

    @Override
    public int getColumnCount() {
        return 18; //id, ciar. kod, uzivatel, L/H, akc. meno urcenie, akc. meno poslednej rev., celad, rod, zbierali, ftg okres, najbl. obec, opis lokality, kvadranty, stat, herbar, casopis, autor publ., rok publ.
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID údaja";
            case 1:
                return "Čiarový kód";
            case 2:
                return "Užívateľ";
            case 3:
                return "Typ";
            case 4:
                return "Pôvodné akceptované meno";
            case 5:
                return "Akceptované meno po poslednej revízii";
            case 6:
                return "Rod";
            case 7:
                return "Čeľaď - podľa APG";
            case 8:
                return "Autor zberu";
            case 9:
                return "Fytogeografický okres";
            case 10:
                return "Najbližšia obec";
            case 11:
                return "Opis lokality";
            case 12:
                return "Kvadrant";
            case 13:
                return "Štát";
            case 14:
                return "Herbár";
            case 15:
                return "Časopis";
            case 16:
                return "Autor publikácie";
            case 17:
                return "Rok vydania";
            default:
                throw new AssertionError();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int realRow = rowIndex + (pageOffset * pageSize);
        Udaj udaj = this.data.get(realRow);
        
        if (udaj != null){

           switch (columnIndex) {
               case 0:
                   return udaj.getId();
               case 1:
                   return ((udaj.getHerbarPolozky() != null && udaj.getHerbarPolozky().getCisloCkFull() != null && !HandyUtils.isNE(udaj.getHerbarPolozky().getCisloCkFull())) ? udaj.getHerbarPolozky().getCisloCkFull() : "");
               case 2:
                   return udaj.getUzivatel();
               case 3:
                   return udaj.getTyp() == null ? "" : udaj.getTyp().toString();
               case 4:
                   SkupRev urcenie = udaj.getUrcenie();
                   if (urcenie != null && urcenie.getMenaTaxonov() != null) {
                       if (urcenie.getMenaTaxonov().getListOfSpecies() == null) {
                           return "nzm " + urcenie.getMenaTaxonov().getMenoScheda();
                       }
                       return urcenie.getMenaTaxonov().getListOfSpecies().getListOfSpecies() == null
                               ? urcenie.getMenaTaxonov().getListOfSpecies().getMeno()
                               : urcenie.getMenaTaxonov().getListOfSpecies().getListOfSpecies().getMeno();
                   }
                   return "neurčené";
               case 5:
                   SortedSet<SkupRev> skupRevs = new TreeSet<>(udaj.getRevisions());
                   if (!skupRevs.isEmpty() && skupRevs.last() != null) {
                       if (skupRevs.last().getMenaTaxonov().getListOfSpecies() != null) {
                           return skupRevs.last().getMenaTaxonov().getListOfSpecies().getListOfSpecies() == null
                                   ? skupRevs.last().getMenaTaxonov().getListOfSpecies().getMeno()
                                   : skupRevs.last().getMenaTaxonov().getListOfSpecies().getListOfSpecies().getMeno();
                       }
                       return "r " + skupRevs.last().getMenaTaxonov().getMenoScheda();
                   }
                   return "";
               case 6:
                   SkupRev urc = udaj.getUrcenie();
                   if (urc != null && urc.getMenaTaxonov() != null && urc.getMenaTaxonov().getListOfSpecies() != null) {
                       return urc.getMenaTaxonov().getListOfSpecies().getGenus() == null ? "" : urc.getMenaTaxonov().getListOfSpecies().getGenus().getMeno();
                   }
                   return "";
               case 7:
                   urc = udaj.getUrcenie();
                   if (urc != null && urc.getMenaTaxonov() != null && urc.getMenaTaxonov().getListOfSpecies() != null && urc.getMenaTaxonov().getListOfSpecies().getGenus() != null) {
                       return urc.getMenaTaxonov().getListOfSpecies().getGenus().getFamilyApg() == null ? "" : urc.getMenaTaxonov().getListOfSpecies().getGenus().getFamilyApg().getMeno();
                   }
                   return "";
               case 8:
                   StringBuilder zbierali = new StringBuilder();
                   for (Object object : udaj.getUdajZberAsocs()) {
                       if (object instanceof UdajZberAsoc) {

                           UdajZberAsoc uza = (UdajZberAsoc) object;
                           //pre lepsiu citatelnost pridame ciarku medzi jednotlivymi menami a ak bola ciarka aj po priezvisku, tak tu odstranime
                           zbierali.append(uza.getMenaZberRev().getMeno().replace(",", "")).append(", ");
                       }
                   }
                   if (!zbierali.toString().equals("")){
                       zbierali.delete(zbierali.length()-2, zbierali.length()-1);
                   }
                   return zbierali.toString();
               case 9:
                   StringBuilder ftgOkresy = new StringBuilder();
                   if (udaj.getLokality() != null){
                       for (Object object : udaj.getLokality().getLokalityFtgokresAsocs()) {
                           if (object instanceof LokalityFtgokresAsoc) {

                               LokalityFtgokresAsoc lfa = (LokalityFtgokresAsoc) object;
                               //pre lepsiu citatelnost dame ciarku medzi menami a ak bola ciarka aj po priezvisku, tak tu odstranime
                               ftgOkresy.append(lfa.getFtgokres().getMeno()).append(", ");
                           }
                       }
                       if (!ftgOkresy.toString().equals("")){
                           ftgOkresy.delete(ftgOkresy.length()-2, ftgOkresy.length()-1);
                       }
                   }
                   return ftgOkresy.toString();
               case 10:
                   return (udaj.getLokality() == null || udaj.getLokality().getObec() == null) ? "" : udaj.getLokality().getObec().getMeno();
               case 11:
                   return udaj.getLokality() == null ? "" : udaj.getLokality().getOpisLokality();
               case 12:
                   StringBuilder kvadranty = new StringBuilder();
                   if (udaj.getLokality() != null){
                       for (Object object : udaj.getLokality().getLokalityKvadrantAsocs()) {
                           if (object instanceof LokalityKvadrantAsoc) {

                               LokalityKvadrantAsoc lka = (LokalityKvadrantAsoc) object;
                               //pre lepsiu citatelnost dame ciarku medzi menami a ak bola ciarka aj po priezvisku, tak tu odstranime
                               kvadranty.append(lka.getKvadrant().getMeno()).append(", ");
                           }
                       }
                       if (!kvadranty.toString().equals("")){
                           kvadranty.delete(kvadranty.length()-2, kvadranty.length()-1);
                       }
                   }
                   return kvadranty.toString();
               case 13:
                   if (udaj.getLokality() != null) {
                       return udaj.getLokality().getBrumit4() == null ? "" : udaj.getLokality().getBrumit4().getMeno();
                   }
                   return "";
               case 14:
                   return (udaj.getHerbarPolozky() == null || udaj.getHerbarPolozky().getHerbar() == null) ? "" : udaj.getHerbarPolozky().getHerbar().getSkratkaHerb();
               case 15:
                   if (udaj.getLitZdroj() != null) {
                       return udaj.getLitZdroj().getCasopis() == null ? "" : udaj.getLitZdroj().getCasopis().getMeno();
                   }
                   return "";
               case 16:
                   StringBuilder autori = new StringBuilder();
                   if (udaj.getLitZdroj() != null){
                       if (udaj.getLitZdroj().getLzdrojAutoriAsocs() != null){
                           for (Object object : udaj.getLitZdroj().getLzdrojAutoriAsocs()) {
                               if (object instanceof LzdrojAutoriAsoc) {

                                   LzdrojAutoriAsoc lzaa = (LzdrojAutoriAsoc) object;
                                   //pre lepsiu citatelnost dame ciarku medzi menami a ak bola ciarka po priezvisku, tak tu odstranime
                                   autori.append(lzaa.getMenaZberRev().getMeno().replace(",", "")).append(", ");
                               }
                           }
                           if (!autori.toString().equals("")){
                               autori.delete(autori.length()-2, autori.length()-1);
                           }
                       }
                   }
                   return autori.toString();
               case 17:
                   return (udaj.getLitZdroj() == null || udaj.getLitZdroj().getRok() == null) ? "" : udaj.getLitZdroj().getRok();
               default:
                   return "";
           }
        } else {
            return null;
        }
    }
    
    public void setEmpty(){
        data.clear();
    }
    
    public void addRow(Udaj udaj) {
        data.add(udaj);
    }
    
    public List<Udaj> getData() {
        return data;
    }

    public void setData(List<Udaj> data) {
        this.data = new ArrayList<>(data);
        fireTableDataChanged();
    }

    @Override
    public void pageUp() {
        if (pageOffset < getPageCount() - 1) {
            pageOffset++;
            fireTableDataChanged();
        }
    }
    
    @Override
    public void pageDown() {
        if (pageOffset > 0) {
            pageOffset--;
            fireTableDataChanged();
        }
    }

    public void setPageSize(int s) {
        if (s == pageSize) {
            return;
        }
        int oldPageSize = pageSize;
        pageSize = s;
        pageOffset = (oldPageSize * pageOffset) / pageSize;
        fireTableDataChanged();
    }

    public int getPageSize() {
        return this.pageSize;
    }
    
    // Use this method if you want to know how big the real table is . . . we
    // could also write "getRealValueAt()" if needed.
    public int getRealRowCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    @Override
    public void goToPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page");
        }
        pageOffset = (int) Math.min(page, getPageCount());
        fireTableDataChanged();
    }

    @Override
    public int getPageCount() {
        if (this.data == null) {
            return 0;
        }
        return (int) Math.ceil((double) this.data.size() / pageSize);
    }
    
    @Override
    public int getPageOffset() {
        return pageOffset;
    }
    
    public void setPageOffset(int offset) {
        pageOffset = offset;
    }
    
    
}
