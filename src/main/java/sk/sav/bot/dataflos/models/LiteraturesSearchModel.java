/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.models;

import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Matus
 */
public class LiteraturesSearchModel extends AbstractTableModel {

    private List<Entity> literatures;

    public LiteraturesSearchModel() {
        this.literatures = new ArrayList<>();
    }

    public LiteraturesSearchModel(List<Entity> literatures) {
        this.literatures = literatures;
    }

    public List<Entity> getLiteratures() {
        return literatures;
    }

    public void setLiteratures(List<Entity> literatures) {
        this.literatures = literatures;
    }

    @Override
    public int getRowCount() {
        return this.literatures.size(); //+1 kvoli tomu, ze primarny literarny zdroj bude v tabulke tiez
    }

    @Override
    public int getColumnCount() {
        return 5; //id, autori, rok, publikacia, casopis 
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < 0) {
            return null;
        }
        LitZdroj lz =  (LitZdroj) this.literatures.get(row);
        if (lz != null){
            switch (column) {
                case 0:
                    return lz;
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
                    return lz.getRok();
                case 3:
                    if (lz.getNazovClanku()  != null && !lz.getNazovClanku().isEmpty()) {
                        return lz.getNazovClanku();
                    } else if (lz.getNazovKnihy() != null && !lz.getNazovKnihy().isEmpty()) {
                        String kniha = lz.getNazovKnihy();
                        String kapitola = "";
                        if (lz.getNazovKapitoly() != null && !lz.getNazovKapitoly().isEmpty()){
                            kapitola = ". Kapitola: " + lz.getNazovKapitoly();
                        }
                        return kniha + kapitola;
                    } else {
                        return "";
                    }
                 case 4:
                    if (lz.getCasopis() != null) {
                        return lz.getCasopis().getMeno();
                    } else if (lz.getPramen() != null){
                        return lz.getPramen();
                    }
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
                return "Autor";
            case 2:
                return "Rok vydania";
            case 3:
                return "Názov publikácie (článku / knihy / rukopisu)";
            case 4:
                return "Časopis";
            default:
                return "";
        }
    }
    
    public void addRow(Entity ent) {
        literatures.add(ent);
    }
    
    public void setEmpty() {
        literatures.clear();
    }
    
    private List<Entity> cloneList(List<Entity> container) {
        List<Entity> list = new ArrayList();
        for (Entity entity : container) {
            LitZdroj lz = (LitZdroj) entity;
            LitZdroj litZdroj = new LitZdroj(lz.getCasopis(), lz.getKod(), lz.getTyp(), lz.getNazovClanku(), lz.getNazovClankuPreklad(), lz.getPramen(), /*Integer idCasopis,*/ lz.getRocnik(), lz.getCislo(), lz.getVydavatel(), lz.getRok(), lz.getStrany(), lz.getPoznamka(), lz.getPocetZaznamov(), lz.isKomplet(), lz.isFotka(), lz.getNazovKnihy(), lz.isMapaRozsirenia(), lz.getNazovKapitoly(), lz.getReferencia(), lz.getUdajs());
            //litZdroj.setId(lz.getId());
            litZdroj.setLzdrojAutoriAsocs(lz.getLzdrojAutoriAsocs());
            Entity copy = litZdroj;
            
            list.add(copy);
        }
        return list;
    }
}
