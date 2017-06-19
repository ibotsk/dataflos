/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.UdajObrazky;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Jakub
 */
public class ImagesTableModel extends AbstractTableModel {

        private List<UdajObrazky> images;

        public ImagesTableModel() {
            this.images = new ArrayList<>();
        }

        public ImagesTableModel(List images) {
            this.images = images;
        }

        public List<UdajObrazky> getImages() {
            return images;
        }

        public void setImages(List<UdajObrazky> setOfImages) {
            this.images = setOfImages;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return this.images.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (row < 0) {
                return null;
            }
            UdajObrazky uo = this.images.get(row);
            switch (col) {
                case 0:
                    return uo;
                case 1:
                    return uo.getPopis();
                case 2:
                    return uo.getUrl();
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "Id";
                case 1:
                    return "Popis";
                case 2:
                    return "URL";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return UdajObrazky.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                default:
                    return null;
            }
        }
        
        public void addRow(UdajObrazky uo) {
            images.add(uo);
    }
    
        public void setEmpty() {
            images.clear();
        }
    }
