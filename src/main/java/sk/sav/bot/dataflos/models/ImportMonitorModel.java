/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jakub
 */
public class ImportMonitorModel extends AbstractTableModel{

    List<ImportMonitorInfo> monitorInfos;
    
    public ImportMonitorModel() {
        this.monitorInfos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return this.monitorInfos.size();
    }

    @Override
    public int getColumnCount() {
        return 4; //objekt typ poloziek, cislo riadku, hlaska, stav 
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 3){
            return ImageIcon.class;
        }
        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        if (rowIndex < 0) {
            return null;
        }
        ImportMonitorInfo imi = this.monitorInfos.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return imi.typPolozky;
            case 1:
                return imi.getRowNumber();
            case 2:
                return imi.getSprava();
            case 3:
                return imi.icon;

            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Typ";
            case 1:
                return "Číslo riadku";
            case 2:
                return "Chybová správa";
            case 3:
                return "Stav importu";
            default:
                return "";
        }
    } 
   
    public void addRow(String typ, int rowNumber, String message, ImageIcon icon) {
        int rowCount = getRowCount();
        ImportMonitorInfo row = new ImportMonitorInfo(typ, rowNumber, message, icon);
        monitorInfos.add(row);
        fireTableRowsInserted(rowCount, rowCount);
    } 
    
    public void clear(){
        monitorInfos.clear();
    }
    
    public class ImportMonitorInfo {
        private String typPolozky;
        private int rowNumber;
        private String sprava;
        private ImageIcon icon;
        
        ImportMonitorInfo(String typPolozky, int rowNumber, String sprava, ImageIcon icon){
            this.typPolozky = typPolozky;
            this.rowNumber = rowNumber;
            this.sprava = sprava;
            this.icon = icon;
        }
        
        /**
         * @return the rowNumber
         */
        public int getRowNumber() {
            return rowNumber;
        }
        
        /**
         * @param rowNumber the rowNumber to set
         */
        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        /**
         * @return the nazovPolozky
         */
        public String getTypPolozky() {
            return typPolozky;
        }

        /**
         * @param nazovPolozky the nazovPolozky to set
         */
        public void setTypPolozky(String nazovPolozky) {
            this.typPolozky = nazovPolozky;
        }

        /**
         * @return the icon
         */
        public ImageIcon getIcon() {
            return icon;
        }

        /**
         * @param icon the icon to set
         */
        public void setIcon(ImageIcon icon) {
            this.icon = icon;
        }

        /**
         * @return the sprava
         */
        public String getSprava() {
            return sprava;
        }

        /**
         * @param sprava the sprava to set
         */
        public void setSprava(String sprava) {
            this.sprava = sprava;
        }
    }
}
