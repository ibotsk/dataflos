/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.SkupRev;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Matus
 */
public class RevisionsTableModel extends AbstractTableModel {

        private List<SkupRev> revisions;

        public RevisionsTableModel() {
            this.revisions = new ArrayList<>();
        }

        public RevisionsTableModel(List<SkupRev> revisions) {
            this.revisions = revisions;
        }

        public List<SkupRev> getRevisions() {
            return revisions;
        }

        public void setRevisions(List<SkupRev> revisions) {
            this.revisions = revisions;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            //return rf.getRevisions().size();
            return this.revisions.size();
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
            //SkupRev rev = rf.getRevisions().get(row);
            SkupRev rev = this.revisions.get(row);
            switch (col) {
                case 0:
                    return rev;
                case 1:
                    return rev.getMenaTaxonov().getListOfSpecies();
                case 2:
                    return StringUtils.join(rev.getSkupRevDets().iterator(), ", ");
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "Dátum";
                case 1:
                    return "Revidované meno";
                case 2:
                    return "Revidovatelia";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return SkupRev.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                default:
                    return null;
            }
        }
    }
