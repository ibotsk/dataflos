/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import sk.sav.bot.dataflos.models.RecordsInsertedModel;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Jakub
 */
public class TableColumnsWidthCounter {
    
    // dynamicky nastavi sirku jednotlivych stlpcov podla sirky nadpisu a dat v stlpci
    public static void setWidthOfColumns(JTable table) {

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // pre narocnost vypoctoveho casu zistovania optimalnych sirok (prilis velke mnozstvo udajov) pri tabulke s prehladom udajov (= tableModel je instancia RecordsInsertedModel) prednastavime vopred vypocitane (priblizne optimalne) sirky pre jednotlive stlpce
        if (table.getModel() instanceof RecordsInsertedModel){
            
            table.getColumnModel().getColumn(0).setPreferredWidth(65);
            table.getColumnModel().getColumn(1).setPreferredWidth(75);
            table.getColumnModel().getColumn(2).setPreferredWidth(65);
            table.getColumnModel().getColumn(3).setPreferredWidth(29);
            table.getColumnModel().getColumn(4).setPreferredWidth(230);
            table.getColumnModel().getColumn(5).setPreferredWidth(230);
            table.getColumnModel().getColumn(6).setPreferredWidth(100);
            table.getColumnModel().getColumn(7).setPreferredWidth(130);
            table.getColumnModel().getColumn(8).setPreferredWidth(150);
            table.getColumnModel().getColumn(9).setPreferredWidth(163);
            table.getColumnModel().getColumn(10).setPreferredWidth(148);
            table.getColumnModel().getColumn(11).setPreferredWidth(400);
            table.getColumnModel().getColumn(12).setPreferredWidth(75);
            table.getColumnModel().getColumn(13).setPreferredWidth(75);
            table.getColumnModel().getColumn(14).setPreferredWidth(75);
            table.getColumnModel().getColumn(15).setPreferredWidth(200);
            table.getColumnModel().getColumn(16).setPreferredWidth(150);
            table.getColumnModel().getColumn(17).setPreferredWidth(91);
            
        } else {
        
            int tableWidth = table.getWidth();
            int columnsTotalWidth = 0;
            int[] columnsWidth = new int[20];

            for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {

                int columnHeaderWidth = getColumnHeaderWidth(table, columnIndex);
                int columnDataWidth = getColumnDataWidth(table, columnIndex);

                columnsWidth[columnIndex] = Math.max(columnHeaderWidth, columnDataWidth);
                columnsTotalWidth += columnsWidth[columnIndex];
            }

            //ak je suma siriek stlpcov vacsia ako stanovena tabulka, ponechame tieto stlpce s vypocitanou sirkou (automaticky sa na tabulku prida horizontalny scroller)
            if (columnsTotalWidth >= tableWidth){
                for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                    table.getColumnModel().getColumn(columnIndex).setPreferredWidth(columnsWidth[columnIndex]);
                }
            } else { //v opacnom pripade si proporcionalne rozdelime sirku stlpcov a pripadne chybajucu sirku doplnim do posledneho stlpca
                for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                    int percentWidth = (int) Math.floor(columnsWidth[columnIndex]*tableWidth/columnsTotalWidth);
                    table.getColumnModel().getColumn(columnIndex).setPreferredWidth(percentWidth);
                }
                table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            }
        }
    }

    //zisti sirku nadpisu stlpca
    private static int getColumnHeaderWidth(JTable table, int column){
        
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        Object value = tableColumn.getHeaderValue();
        TableCellRenderer renderer = tableColumn.getHeaderRenderer();

        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
        return c.getPreferredSize().width;
    }
    
    // zisti priemernu sirku dat v stlpci s vynimkou prazdnych udajov (v prvej tretine, pre zrychlenie)
    // odkomentovane casti v metode povodne pocitali na zaklade maximalnej moznej sirky
    private static int getColumnDataWidth(JTable table, int column){
        
        long sum = 0;
        int rows = 0;
        
        //int preferredWidth = 0;
        //int maxWidth = table.getColumnModel().getColumn(column).getMaxWidth();
        
        int rowsToCheck = (table.getRowCount() > 10) ? table.getRowCount()/3 : table.getRowCount();
        
        for (int row = 0; row < rowsToCheck; row++) {
            
            int cellWidth = getCellDataWidth(table, row, column); 
            if (cellWidth != 0){
                sum = sum + getCellDataWidth(table, row, column);
                rows++;
            }
            
            //preferredWidth = Math.max(preferredWidth, getCellDataWidth(table, row, column));
            //if (preferredWidth >= maxWidth) break;
        }
        
        double avg;
        if (rows > 0){
            avg = (double) sum / rows;
        } else {
            avg = 0;
        }
        return (int) Math.ceil(avg);
    }

    //cez renderer zisti sirku dat v konkretnej bunke tabulky
    private static int getCellDataWidth(JTable table, int row, int column){
        
        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
        Component c = table.prepareRenderer(cellRenderer, row, column);
        int width = c.getPreferredSize().width + table.getIntercellSpacing().width;

        return width;
    }
    
}
