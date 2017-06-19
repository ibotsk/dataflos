/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.util.HandyUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matus
 */
public class LiteraturesTableModel extends AbstractTableModel {

    private List<LitZdrojRev> literatures;

    public LiteraturesTableModel() {
        this.literatures = new ArrayList<>();
    }

    public LiteraturesTableModel(List<LitZdrojRev> literatures) {
        this.literatures = literatures;
    }

    public List<LitZdrojRev> getLiteratures() {
        return literatures;
    }

    public void setLiteratures(List<LitZdrojRev> literatures) {
        this.literatures = literatures;
    }

    @Override
    public int getRowCount() {
        return this.literatures.size(); //+1 kvoli tomu, ze primarny literarny zdroj bude v tabulke tiez
    }

    @Override
    public int getColumnCount() {
        return 4; //objekt litZdrojrev, autori, nazov, rok 
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < 0) {
            return null;
        }
        LitZdrojRev lzr = this.literatures.get(row);
        if (lzr != null && lzr.getLitZdroj() != null){
            LitZdroj lz = lzr.getLitZdroj();
            switch (column) {
                case 0:
                    return lzr;
                case 1:
                    StringBuilder autori = new StringBuilder();
                    for (Object object : lz.getLzdrojAutoriAsocs()) {
                        if (object instanceof LzdrojAutoriAsoc) {

                            LzdrojAutoriAsoc lzaa = (LzdrojAutoriAsoc) object;
                            //pre lepsiu citatelnost dame ciarku medzi menami a ak bola ciarka po priezvisku, tak tu odstranime
                            autori.append(lzaa.getMenaZberRev().getMeno().replace(",", "")).append(", ");
                        }
                    }
                    if (!autori.toString().equals("")){
                        autori.delete(autori.length()-2, autori.length()-1);
                    }
                    return autori.toString();
                case 2:
                    if (!HandyUtils.isNE(lz.getNazovClanku())) {
                        return lz.getNazovClanku();
                    } else if (lz.getCasopis() != null) {
                        return lz.getCasopis().getMeno();
                    } else if (!HandyUtils.isNE(lz.getNazovKnihy())) {
                        return lz.getNazovKnihy();
                    } else {
                        return !HandyUtils.isNE(lz.getPramen()) ? lz.getPramen() : lz.getPoznamka();
                    }
                case 3:
                    return lz.getRok();
                default:
                    return null;
            }
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Autori";
            case 2:
                return "Názov zdroja";
            case 3:
                return "Rok";
            default:
                return "";
        }
    }

    public void addRow(LitZdrojRev lzr) {
        literatures.add(lzr);
    }
}
