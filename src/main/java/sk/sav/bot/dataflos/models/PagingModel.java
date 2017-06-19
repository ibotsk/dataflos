/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.interf.Entity;
import sk.sav.bot.dataflos.gui.PagingTableInterf;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matus
 */
public class PagingModel extends AbstractTableModel implements PagingTableInterf {

    protected int pageSize;
    protected int pageOffset;
    protected int schvaleneColumn;
    protected List<Entity> data;

    public PagingModel(List<Entity> data) {
        this(data == null ? 0 : data.size(), 100000, data);
    }

    public PagingModel(int numRows, int size, List<Entity> data) {
        this.data = data;
        this.pageSize = size;
        this.pageOffset = 0;
        
        if(data != null){
            schvaleneColumn = data.get(0).namesAndValues().size()-1;
        } else {
            schvaleneColumn = 0;
        }
    }

    // Return values appropriate for the visible table part.
    @Override
    public int getRowCount() {
        if (this.pageOffset == getPageCount() - 1) {
            return getRealRowCount() - this.pageOffset * this.pageSize;
        }
        return Math.min(this.pageSize, this.data == null ? 0 : this.data.size());
    }

    @Override
    public int getColumnCount() {
        if (this.data == null || this.data.isEmpty()) {
            return 0;
        }
        Entity ent = this.data.get(0);
        if (ent == null) {
            return 0;
        }
        return (ent.namesAndValues() == null ? 0 : ent.namesAndValues().size()+1);
    }

    // Work only on the visible part of the table.
    @Override
    public Object getValueAt(int row, int col) {
        if (row < 0) {
            throw new IllegalArgumentException("row");
        }
        if (col < 0) {
            throw new IllegalArgumentException("col");
        }
        if (this.data == null) {
            return null;
        }
        int realRow = row + (pageOffset * pageSize);
        Entity ent = this.data.get(realRow);
        if (ent != null && ent.namesAndValues() != null && col < ent.namesAndValues().size()) {
            // v pripade ak entita ma posledny stlpec s informaciou ci uz bola schválena/neschválena, zobraz k tomu prislusnu ikonku
            if(data != null && (col == schvaleneColumn) && (ent.namesAndValues().get(schvaleneColumn)[0].equals("Schválené"))){
                //ak bola polozka schvalena, "odfajkni" ju
                if (ent.namesAndValues().get(col)[1].equals("ano")){
                    return new ImageIcon(getClass().getResource("/checkmark.png")); 
                } else {
                    return new ImageIcon();
                }
            }
            return ent.namesAndValues().get(col)[1];
        }
        return ent;
    }

    @Override
    public String getColumnName(int col) {
        if (this.data == null) {
            return null;
        }
        Entity ent = this.data.get(0);
        if (ent != null && col < ent.namesAndValues().size()) {
            return ent.namesAndValues().get(col)[0];
        }
        return "Chyba";
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // v pripade ak entita ma posledny stlpec s informaciou ci uz bola potvrdena, vrati sa typ triedy obrazok (pre obrazok "odfajknute")
        if(data != null && (columnIndex == schvaleneColumn) && (data.get(0).namesAndValues().get(schvaleneColumn)[0].equals("Schválené"))){
            return ImageIcon.class;
        }
        return String.class;
    }

    // Use this method to figure out which page you are on.
    @Override
    public int getPageOffset() {
        return pageOffset;
    }

    @Override
    public int getPageCount() {
        if (this.data == null) {
            return 0;
        }
        return (int) Math.ceil((double) this.data.size() / pageSize);
    }
    
    public void addRow(Entity entity) {
        data.add(entity);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public List<Entity> getData() {
        return data;
    }

    public void setData(List<Entity> data) {
        this.data = data;
        fireTableDataChanged();
    }
    
    public void refresh(){
        //make the changes to the table
        fireTableDataChanged();
    }
    
    public void addAndRefresh(Entity newEntity){
        //add row and make the changes to the table
        getData().add(newEntity);
        fireTableDataChanged();
    }

    // Use this method if you want to know how big the real table is . . . we
    // could also write "getRealValueAt()" if needed.
    public int getRealRowCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    public int getPageSize() {
        return this.pageSize;
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

    @Override
    public void goToPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page");
        }
        pageOffset = (int) Math.min(page, getPageCount());
        fireTableDataChanged();
    }

    // Update the page offset and fire a data changed (all rows).
    @Override
    public void pageDown() {
        if (pageOffset > 0) {
            pageOffset--;
            fireTableDataChanged();
        }
    }

    // Update the page offset and fire a data changed (all rows).
    @Override
    public void pageUp() {
        if (pageOffset < getPageCount() - 1) {
            pageOffset++;
            fireTableDataChanged();
        }
    }
}
