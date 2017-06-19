/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.excelimport;

import sk.sav.bot.dataflos.entity.Brumit4;
import sk.sav.bot.dataflos.entity.Casopisy;
import sk.sav.bot.dataflos.entity.Exsikaty;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Herbar;
import sk.sav.bot.dataflos.entity.HerbarPolozky;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.MenaTaxonov;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.Ngc;
import sk.sav.bot.dataflos.entity.Obec;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.Entity;
import sk.sav.bot.dataflos.factory.ListAddRemoveUtils;
import sk.sav.bot.dataflos.models.ImportMonitorModel;
import sk.sav.bot.dataflos.util.HandyUtils;
import sk.sav.bot.dataflos.util.HibernateQuery;
import sk.sav.bot.dataflos.verify.DataVerifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import javax.swing.SwingWorker;
import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Jakub
 */
public class ExcelPrepareImport extends SwingWorker<Integer, String>{
    
    // kolekcia entit (zaznamov do tabuliek), ktore sa doteraz nevyskytovali v DB a je ich potrebne vlozit pred dalsou manipulaciou s nimi
    Map<String, Entity> unsavedEntities = new HashMap<>();
    
    List<Udaj> importUdaje = new ArrayList<>();
    List<Udaj> updateUdaje = new ArrayList<>();
    private boolean importovat = true;

    // referencia na monitor importu
    private final ExcelImportMonitor im;

    // cesta k importovanemu suboru
    private final File excelFile;
    
    private final HibernateQuery hq;
    private Udaj impUdaj;
    
    private static Logger log = Logger.getLogger(ExcelPrepareImport.class.getName());
    
    private int progress;
    
    List<Entity> listLos;
    List<Entity> listLokalita;
    List<Entity> listBrumit4;
    List<Entity> listFtgOkres;
    List<Entity> listNgc;
    List<Entity> listObec;
    List<Entity> listKvadrant;
    List<Entity> listAutorZberu;
    List<Entity> listHerb;
    List<Entity> listHerbPolozka;
    List<Entity> listExsikat;
    List<Entity> listCasopis;
    
    String nazovTaxonu = "";
    String acceptMeno = "";
    String autSkratka = "";
    ListOfSpecies los = null;
    Brumit4 brummit4 = null;
    List<Ftgokres> ftgokresy = new ArrayList();
    Ngc ngc = null;
    Obec obec = null;
    String opisLok = "";
    boolean cca = false;
    int nadmOd = 0;
    int nadmDo = 0;
    List<Kvadrant> kvadranty = new ArrayList();
    Double zemepisSirka = 0.0;
    Double zemepisDlzka = 0.0;
    List<MenaZberRev> autoriZberu = new ArrayList<>();
    List<MenaZberRev> autoriUrcenia = new ArrayList<>();
    List<MenaZberRev> autoriRevizie = new ArrayList<>();
    List<MenaZberRev> autoriPublikacie = new ArrayList<>();
    List<MenaZberRev> editoriPublikacie = new ArrayList<>();
    String datumZberu = "";
    String datumZberuSlovom = "";
    Herbar herbar = null;
    String cisloPolozky = "";
    Exsikaty exsikat = null;
    String cisloCiarKodu = "";
    String cisloZberu = "";
    String revSchedaMeno = "";
    String acceptMenoRev = "";
    String autSkratkaRev = "";
    ListOfSpecies losRev = null;
    String datumRevizie = "";
    String rokVydania = "";
    Casopisy casopis = null;
    String rocnikCasopisu = "";
    String cisloCasopisu = "";
    String nazovClanku = "";
    String pramen = "";
    String nazovPublikacie = "";
    String nazovKapitoly = "";
    String vydavatel = "";
    String stranyPublikacie = "";
    String stranaUdaj = "";
    String poznamkaLok = "";
    String poznamkaLit = "";
    String pristup = "";
    
    public ExcelPrepareImport(final ExcelImportMonitor im, final File excelFile, final HibernateQuery hq) {
        this.im = im;
        this.excelFile = excelFile;
        this.hq = hq;
    }

    @Override
    protected Integer doInBackground() {
        
        setProgress(progress = 0);
        
        // najprv nacitame data z tabuliek  pre potrebu overovania ci data v importovanom subore uz existuju v databaze (zamedzenie duplicitam)
        final Map<String, Integer> tableTimes = new HashMap<>();
        tableTimes.put("ListOfSpecies", 12);
        tableTimes.put("Lokality", 26);
        tableTimes.put("Brumit4", 7);
        tableTimes.put("Ftgokres", 5);
        tableTimes.put("Ngc", 5);
        tableTimes.put("Obec", 6);
        tableTimes.put("Kvadrant", 5);
        tableTimes.put("MenaZberRev", 5);
        tableTimes.put("Herbar", 5);
        tableTimes.put("HerbarPolozky", 9);
        tableTimes.put("Exsikaty", 7);
        tableTimes.put("Casopisy", 8);

        for (String loadTable : tableTimes.keySet()){
            List<Entity> data = loadData(loadTable, tableTimes.get(loadTable));
            switch (loadTable){
                case "ListOfSpecies":
                    listLos = data;
                    break;
                case "Lokality":
                    listLokalita = data;
                    break;                
                case "Brumit4":
                    listBrumit4 = data;
                    break;                
                case "Ftgokres":
                    listFtgOkres = data;
                    break;
                case "Ngc":
                    listNgc = data;
                    break;
                case "Obec":
                    listObec = data;
                    break;
                case "Kvadrant":
                    listKvadrant = data;
                    break;
                case "MenaZberRev":
                    listAutorZberu = data;
                    break;
                case "Herbar":
                    listHerb = data;
                    break;
                case "HerbarPolozky":
                    listHerbPolozka = data;
                    break;
                case "Exsikaty":
                    listExsikat = data;
                    break;
                case "Casopisy":
                    listCasopis = data;
                    break;
            }
            // ak by sme chceli zrusit import, kontrolujeme ci sa tak nestalo
            if (isCancelled()){
                return 0;
            }
        }

        // pre istotu nastavime progres na 100, aby s urcitostou zmizol
        setProgress(progress = 100);
        
        
        // pripravime si referencne premenne pre pracu s importovanym suborom
        
        InputStream inputStreamExcelTemplate = null;
        FileInputStream inputStreamExcelImportFile = null;
        
        Sheet importHerbSheet = null;
        Sheet importLitSheet = null;
        Sheet importUnpublSheet = null;
        
        Sheet templateHerbSheet = null;
        Sheet templateLitSheet = null;
        Sheet templateUnpublSheet = null;
        
        try {
            // nacitanie templatu pre korektne zobrazovanie informacii o stlpcoch (pri pripadnych chybach/upozorneniach)
            inputStreamExcelTemplate = this.getClass().getResourceAsStream("/excelTemplate.xls");
            //Workbook templateWorkbook = new HSSFWorkbook(ISExcelTemplate);
            Workbook templateWorkbook = WorkbookFactory.create(inputStreamExcelTemplate);
            templateHerbSheet = templateWorkbook.getSheet("Herbárové_položky");
            templateLitSheet = templateWorkbook.getSheet("Literárne_údaje");
            templateUnpublSheet = templateWorkbook.getSheet("Terénne_údaje");
        
        } catch (InvalidFormatException e) {
            log.warn("Import problem: Template file has invalid format", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Problém s formátom šablóny."));
        } catch (FileNotFoundException e) {
            log.warn("Import problem: Template file not found", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Súbor so šablónou importu nenájdený."));
        } catch (IOException e) {
            log.warn("Import problem: Cannot be read from template file", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Nedá sa načítať súbor so šablónou importu."));
        } catch (Exception e){
            log.warn("Import problem: Exception during import process", e);
            JOptionPane.showMessageDialog(im, " Nastala chyba v importovacom procese! \n" + e.getLocalizedMessage());
        }
        
        if (templateLitSheet == null || templateHerbSheet == null || templateUnpublSheet == null){
            log.warn("Import problem: Cannot read template file or some of its sheets");
            JOptionPane.showMessageDialog(null, String.format("Nastal problém s načítaním vzorového súbora resp. niektorej z jeho záložiek. \nProsím skontroluje ich názvy."), "Upozornenie", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        try {
            // nacitanie excelovskeho suboru a jeho jednotlivych zaloziek
            inputStreamExcelImportFile = new FileInputStream(excelFile.getPath());
            //Workbook workbookImportRead = new HSSFWorkbook(FISExcelImportFile);
            Workbook workbookImportRead = WorkbookFactory.create(inputStreamExcelImportFile);
            importHerbSheet = workbookImportRead.getSheet("Herbárové_položky");
            importLitSheet = workbookImportRead.getSheet("Literárne_údaje");
            importUnpublSheet = workbookImportRead.getSheet("Terénne_údaje");
            
        } catch (InvalidFormatException e) {
            log.warn("Import problem: Input file has invalid format", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Problém s formátom vstupného súboru."));
        } catch (FileNotFoundException e) {
            log.warn("Import problem: Input file not found", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Zadaný vstupný súbor nebol nájdený."));
        } catch (IOException e) {
            log.warn("Import problem: Cannot be read from input file", e);
            JOptionPane.showMessageDialog(im, String.format(" Nastala chyba v importovacom procese! \n Zo zadaného vstupného súboru sa nedá čítať."));
        } catch (Exception e){
            log.warn("Import problem: Exception during import process", e);
            JOptionPane.showMessageDialog(im, " Nastala chyba v importovacom procese! \n" + e.getLocalizedMessage());
        }
        
        if (importLitSheet == null || importHerbSheet == null || importUnpublSheet == null){
            log.warn("Import problem: Cannot read template file or some of its sheets");
            JOptionPane.showMessageDialog(null, String.format("Nastal problém s načítaním niektorej zo záložiek. \nProsím skontroluje si ich názvy."), "Upozornenie", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        // pokial nastal pokus o prerusenie behu importu, v tomto mieste je jeden z checkpointov na ukoncenie metody
        if (isCancelled()){
            return 0;
        }

        String[] colNames = null;
        int rowLength = 0;
        
        try {
            
            // HERBAROVE POLOZKY
            for (int i = 0; i <= importHerbSheet.getLastRowNum(); i++) {

                Row rowImportHerbSheet = importHerbSheet.getRow(i);                                 

                // nacitaj nazvy jednotlivych stlpcov z templatu (uvedene v prvom riadku)
                if (i == 0){
                    Row rowTemplateReadH = templateHerbSheet.getRow(i);
                    rowLength = rowTemplateReadH.getLastCellNum();
                    colNames = new String[rowLength];
                    for (int j = 0; j < rowLength; j++) {
                        colNames[j] = rowTemplateReadH.getCell(j).getStringCellValue();
                    }
                    
                // data zacinaju az od 4. riadka (vratane 0)    
                } else if(i > 2){

                    boolean correctRow = true;
                    String chyba = "";

                    // klucove stlpce
                    boolean newRec, newRev, newLit;

                    // klucove cisla stlpcov ohranicujuce sekcie importovanych dat
                    int newRecMaxCell = 22;
                    int newRevMaxCell = 27;
                    int newLitMaxCell = 39;

                    // kontrola sekcii
                    // cast, ktora ked ma niektoru z buniek vyplnenu, tak sa bude importovat novy importUdaj (v opacnom pripade nie)
                    newRec = checkIfSectionNotEmpty(rowImportHerbSheet, 0, newRecMaxCell);
                    // cast, ktora ked ma niektoru z buniek vyplnenu, tak sa bude importovat nova revizia (v opacnom pripade nie)
                    newRev = checkIfSectionNotEmpty(rowImportHerbSheet, newRecMaxCell+1, newRevMaxCell);
                    // cast, ktora ked ma niektoru z buniek vyplnenu, tak je uvedeny novy lit. zdroj (v opacnom pripade nie)
                    newLit = checkIfSectionNotEmpty(rowImportHerbSheet, newRevMaxCell+1, newLitMaxCell);

                    // kontrola ci aspon jedna z casti je neprazdna ( = dany riadok nie je prazdny )
                    //  => import na danom sheete bude prebiehat dalej
                    // v opacnom pripade ukonci import daneho sheet-u (break)
                    if (newRec != false || newRev != false || newLit != false){
                        // if not empty row
                        for (int cellNum = 0; cellNum < rowLength; cellNum++) {
                            
                            String value = retrieveValue(rowImportHerbSheet.getCell(cellNum));

                            switch (cellNum) {
                                case 0:
                                    // Nazov taxonu (nie je nutne vyzadovane)
                                    nazovTaxonu = value;
                                    break;
                                case 1:
                                    // Štandardizované znenie mena (povinny atribut)
                                    acceptMeno = value;
                                    //kontrola ci existuje v DB sa urobi v kontexte s autorskymi skratkami
                                    break;
                                case 2:
                                    // Autorské skratky (!nie je nutne vyzadovane)
                                    chyba = importAkcMeno(acceptMeno,  value, true);
                                    break;
                                case 3:
                                    // Brummitt 4
                                    chyba = importBrumit4(value,  colNames[cellNum]);
                                    break;
                                case 4:
                                    // Fytogeografický okres
                                    chyba = importFtgokres(value,  colNames[cellNum]);
                                    break;
                                case 5:
                                    // Nizsi geograficky celok
                                    chyba = importNgc(value,  colNames[cellNum]);
                                    break;
                                case 6:
                                    // Najbližšia obec
                                    chyba = importObec(value,  colNames[cellNum]);
                                    break;
                                case 7:
                                    // Opis lokality
                                    chyba = importOpisLokality(value,  colNames[cellNum]);
                                    break;
                                case 8:
                                    // Cca.
                                    chyba = importCca(value,  colNames[cellNum]);
                                    break;
                                case 9:
                                    // Nadm. výška OD
                                    chyba = importAltOd(value,  colNames[cellNum]);
                                    break;
                                case 10:
                                    // Nadm. výška DO
                                    chyba = importAltDo(value,  colNames[cellNum]);
                                    break;
                                case 11:
                                    // Kvadrant
                                    chyba = importKvadranty(value,  colNames[cellNum]);
                                    break;
                                case 12:
                                    // Zemepisná šírka
                                    chyba = importZemepisSirka(value,  colNames[cellNum]);
                                    break;
                                case 13:
                                    // Zemepisná dĺžka
                                    chyba = importZemepisDlzka(value,  colNames[cellNum]);
                                    break;
                                case 14:
                                    // Autor zberu / záznamu
                                    chyba = importAutorZberu(value, colNames[cellNum]);
                                    break;
                                case 15:
                                    // Dátum zberu
                                    chyba = importZberDatum(value, colNames[cellNum]);
                                    break;
                                case 16:
                                    // Dátum zberu slovom
                                    datumZberuSlovom = value;
                                    break;
                                case 17:
                                    // Autori urcenia
                                    chyba = importAutorUrcenia(value, colNames[cellNum]);
                                    break;
                                case 18:
                                    // Herbar (nepovinne)
                                    chyba = importHerbar(value, colNames[cellNum]);
                                    break;
                                case 19:
                                    // Cislo polozky
                                    cisloPolozky = value;
                                    break;
                                case 20:
                                    // Exsikat
                                    chyba = importExsikat(value, colNames[cellNum]);
                                    break;
                                case 21:
                                    // Cislo ciaroveho kodu
                                    cisloCiarKodu = value;
                                    break;
                                case 22:
                                    // Cislo zberu
                                    cisloZberu = value;
                                    break;
                                case 23:
                                    // Revidovane meno
                                    revSchedaMeno = value;
                                    break;
                                case 24:
                                    // Standardizovane meno
                                    acceptMenoRev = value;
                                    break;
                                case 25:
                                    // Autorske skratky k stand. menu
                                    chyba = importAkcMeno(acceptMenoRev,  value, false);
                                    break;
                                case 26:
                                    // Autori revizie
                                    chyba = importAutoriRev(value, colNames[cellNum]);
                                    break;
                                case 27:
                                    // Datum revizie
                                    chyba = importDatumRevizie(value, colNames[cellNum]);
                                    break;
                                case 28:
                                    // Autori publikacie
                                    chyba = importAutoriPubl(value, colNames[cellNum]);
                                    break;
                                case 29:
                                    // Rok vydania
                                    chyba = importRokVydania(value, colNames[cellNum]);
                                    break;
                                case 30:
                                    // Casopis
                                    chyba = importCasopis(value, colNames[cellNum]);
                                    break;
                                case 31:
                                    // Rocnik casopisu
                                    rocnikCasopisu = value;
                                    break;
                                case 32:
                                    // Cislo casopisu
                                    cisloCasopisu = value;
                                    break;
                                case 33:
                                    // Nazov clanku
                                    nazovClanku = value;
                                    break;
                                case 34:
                                    // Pramen
                                    pramen = value;
                                    break;
                                case 35:
                                    // Editori
                                    chyba = importEditoriPubl(value, colNames[cellNum]);
                                    break;
                                case 36:
                                    // Nazov publikacie / knihy
                                    nazovPublikacie = value;
                                    break;
                                case 37:
                                    // Nazov kapitoly
                                    nazovKapitoly = value;
                                    break;
                                case 38:
                                    // Vydavatel
                                    vydavatel = value;
                                    break;
                                case 39:
                                    // Strany publikacie
                                    stranyPublikacie = value;
                                    break;
                                case 40:
                                    // Strana s udajom
                                    stranaUdaj = value;
                                    break;
                                case 41:
                                    // Poznamka k lokalite
                                    poznamkaLok = value;
                                    break;
                                case 42:
                                    // Poznamka k lokalite
                                    poznamkaLit = value;
                                    break;
                                case 43:
                                    // Pristup k udaju
                                    chyba = importPristup(value, colNames[cellNum]);
                                    break;
                                default:
                            }

                            if (!chyba.equals("")){
                                
                                // aby chybove hlasky sa vypisovali iba pre bunky v sekciach, ktore su neprazdne
                                if ((0 <= cellNum && cellNum <= newRecMaxCell && newRec) || (newRecMaxCell < cellNum && cellNum <= newRevMaxCell && newRev) || (newRevMaxCell < cellNum && cellNum <= newLitMaxCell && newLit)){
                                   
                                    correctRow = false;
                                    if (chyba.startsWith("|ERROR|")){
                                        importovat = false;
                                    }
                                    publish("Herbárové položky|" + String.valueOf(i) + chyba);
                                    //displayDelay(500000);
                                    chyba = "";
                                }
                            }
                        }                        
                    } else {
                        break;
                    }
                    
                    if (correctRow) {
                        publish("Herbárové položky|" + String.valueOf(i) + "|OK| ");
                    }

                    if (importovat){

                        if (newRec != false) { //je zadany novy importUdaj na import

                            impUdaj = new Udaj();
                            
                            MenaTaxonov mt = new MenaTaxonov(los, nazovTaxonu, null, null);
                            SkupRev urcenie = new SkupRev(null, mt, null, Boolean.FALSE, null);
                            ListAddRemoveUtils.setRevisorsAndIdentificators(urcenie, autoriUrcenia);

                            Lokality lokalita = new Lokality();
                            lokalita.setAltOd(nadmOd);
                            lokalita.setAltDo(nadmDo);
                            lokalita.setAltOdCca(cca);
                            lokalita.setBrumit4(brummit4);
                            lokalita.setLatitude(zemepisSirka);
                            lokalita.setLongitude(zemepisDlzka);
                            lokalita.setNgc(ngc);
                            lokalita.setObec(obec);
                            lokalita.setOpisLokality(opisLok);
                            lokalita.setPoznamkaLok(poznamkaLok);
                            lokalita = ListAddRemoveUtils.setFtgokresy(lokalita, ftgokresy);
                            lokalita = ListAddRemoveUtils.setQuadrants(lokalita, kvadranty);
                            
                            HerbarPolozky herbPolozka = new HerbarPolozky();
                            herbPolozka.setHerbar(herbar);
                            herbPolozka.setExsikaty(exsikat);
                            herbPolozka.setCisloPol(cisloPolozky);
                            herbPolozka.setCisloCkFull(cisloCiarKodu);
                            herbPolozka.setCisloZberu(cisloZberu);

                            impUdaj.getSkupRevs().add(urcenie);
                            impUdaj.setLokality(lokalita);
                            impUdaj.setHerbarPolozky(herbPolozka);
                            impUdaj.setTyp('H');
                            impUdaj.setUdajZberAsocs(ListAddRemoveUtils.setCollectors(impUdaj, autoriZberu));
                            impUdaj.setDatumZberu(datumZberu);
                            impUdaj.setDatumZberuSlovom(datumZberuSlovom);
                            impUdaj.setStranaUdaja(stranaUdaj);
                            impUdaj.setVerejnePristupny(isPristupny());
                            impUdaj.setVerejnePristupnyOd((!isPristupny() && !pristup.equals("N")) ? pristup : null);
                        }

                        if (newLit != false) { //literatura

                            List<LitZdrojRev> revs = new ArrayList<>(impUdaj.getLitZdrojRevs().size());
                            LitZdrojRev lzr = new LitZdrojRev();

                            LitZdroj lz = new LitZdroj();
                            lz.setCasopis(casopis);
                            lz.setRocnik(rocnikCasopisu);
                            lz.setCislo(cisloCasopisu);
                            lz.setNazovClanku(nazovClanku);
                            lz.setPramen(pramen);
                            lz.setNazovKnihy(nazovPublikacie);
                            lz.setNazovKapitoly(nazovKapitoly);
                            lz.setRok(rokVydania);
                            lz.setVydavatel(vydavatel);
                            lz.setStrany(stranyPublikacie);
                            lz.setPoznamka(poznamkaLit);

                            lz = ListAddRemoveUtils.setLitAuthors(lz, autoriPublikacie);
                            lz = ListAddRemoveUtils.setLitEditors(lz, editoriPublikacie);

                            lzr.setLitZdroj(lz);
                            revs.add(lzr);

                            impUdaj.setLitZdrojRevs(new HashSet<>(revs));
                        }

                        if (newRev != false) { //bude mat importUdaj reviziu
                            SkupRev revizia;
                            if (losRev != null) {
                                MenaTaxonov mt = new MenaTaxonov(losRev, revSchedaMeno, null, null);
                                revizia = new SkupRev(null, mt, datumRevizie, true, null);
                                revizia = ListAddRemoveUtils.setRevisorsAndIdentificators(revizia, autoriRevizie);
                                impUdaj.getSkupRevs().add(revizia);
                            }
                        }
                        
                        if (newRec){
                            importUdaje.add(impUdaj);
                        } else {
                            // vymaz predchadzajuci(posledny) udaj bez revizii(lit./urc.) a pridaj novy s reviziami
                            importUdaje.remove(importUdaje.size()-1);
                            importUdaje.add(impUdaj);
                        }
                    }
                    //displayDelay(2000000);
                }
                
                resetImportVariables();

                if (isCancelled()){
                    return 0;
                }
            }
            
            /* LITERARNE POLOZKY */
            
            for (int i = 0; i <= importLitSheet.getLastRowNum(); i++) { 

                Row rowImportLitSheet = importLitSheet.getRow(i);

                // nacitaj nazvy jednotlivych stlpcov z templatu (uvedene v prvom riadku)
                if (i == 0){
                    Row rowTemplateReadL = templateLitSheet.getRow(i);
                    rowLength = rowTemplateReadL.getLastCellNum();
                    colNames = new String[rowLength];
                    for (int j = 0; j < rowLength; j++) {
                        colNames[j] = rowTemplateReadL.getCell(j).getStringCellValue();
                    }
                } else if(i > 2){

                    boolean correctRow = true;
                    String chyba = "";

                    boolean newRec, newLit;

                    // klucove cisla stlpcov ohranicujucich sekcie importovanych dat (novy importUdaj resp. lit. zdroj)
                    int newRecMaxCell = 18;
                    int newLitMaxCell = 30;

                    // kontrola sekcii
                    // cast, ktora ked ma niektoru z buniek vyplnenu, tak sa bude importovat novy importUdaj (v opacnom pripade nie)
                    newRec = checkIfSectionNotEmpty(rowImportLitSheet, 0, newRecMaxCell);
                    // cast, ktora ked ma niektoru z buniek vyplnenu, tak je uvedeny novy lit. zdroj (v opacnom pripade nie)
                    newLit = checkIfSectionNotEmpty(rowImportLitSheet, newRecMaxCell+1, newLitMaxCell);

                    // kontrola ci aspon jedna z casti je neprazdna ( = dany riadok nie je prazdny )
                    //  => import na danom sheete bude prebiehat dalej
                    // v opacnom pripade ukonci import daneho sheet-u (break)                   
                    int startCell = 0; // normalne sa zacina s kontrolou dat od prvej bunky v riadku, ak vsak ide cisto o reviziu (ta je priradena k predoslemu udaju), zacneme az na mieste kde zacina (startCell)
                    if (newRec != false || newLit != false){
                        if (newRec == false){
                            startCell = newRecMaxCell;
                        }
                        for (int cellNum = startCell; cellNum < rowLength; cellNum++) {

                            String value = retrieveValue(rowImportLitSheet.getCell(cellNum));

                            // na zaklade cisla stlpcu vykonaj prislusny obsluhujuci kod
                            switch (cellNum) {
                                case 0:
                                    // Nazov taxonu (nie je nutne vyzadovane)
                                    nazovTaxonu = value;
                                    break;
                                case 1:
                                    // Štandardizované znenie mena (povinny atribut)
                                    acceptMeno = value;
                                    //kontrola ci existuje v DB sa urobi v kontexte s autorskymi skratkami
                                    break;
                                case 2:
                                    // Autorské skratky (!nie je nutne vyzadovane)
                                    chyba = importAkcMeno(acceptMeno,  value, true);
                                    break;
                                case 3:
                                    // Brummitt 4
                                    chyba = importBrumit4(value,  colNames[cellNum]);
                                    break;
                                case 4:
                                    // Fytogeografický okres
                                    chyba = importFtgokres(value,  colNames[cellNum]);
                                    break;
                                case 5:
                                    // Nizsi geograficky celok
                                    chyba = importNgc(value,  colNames[cellNum]);
                                    break;
                                case 6:
                                    // Najbližšia obec
                                    chyba = importObec(value,  colNames[cellNum]);
                                    break;
                                case 7:
                                    // Opis lokality
                                    chyba = importOpisLokality(value,  colNames[cellNum]);
                                    break;
                                case 8:
                                    // Cca.
                                    chyba = importCca(value,  colNames[cellNum]);
                                    break;
                                case 9:
                                    // Nadm. výška OD
                                    chyba = importAltOd(value,  colNames[cellNum]);
                                    break;
                                case 10:
                                    // Nadm. výška DO
                                    chyba = importAltDo(value,  colNames[cellNum]);
                                    break;
                                case 11:
                                    // Kvadrant
                                    chyba = importKvadranty(value,  colNames[cellNum]);
                                    break;
                                case 12:
                                    // Zemepisná šírka
                                    chyba = importZemepisSirka(value,  colNames[cellNum]);
                                    break;
                                case 13:
                                    // Zemepisná dĺžka
                                    chyba = importZemepisDlzka(value,  colNames[cellNum]);
                                    break;
                                case 14:
                                    // Autor zberu / záznamu
                                    chyba = importAutorZberu(value, colNames[cellNum]);
                                    break;
                                case 15:
                                    // Dátum zberu
                                    chyba = importZberDatum(value, colNames[cellNum]);
                                    break;
                                case 16:
                                    // Dátum zberu slovom
                                    datumZberuSlovom = value;
                                    break;
                                case 17:
                                    // Autori urcenia
                                    chyba = importAutorUrcenia(value, colNames[cellNum]);
                                    break;
                                case 18:
                                    // Herbar (nepovinne)
                                    chyba = importHerbar(value, colNames[cellNum]);
                                    break;
                                case 19:
                                    // Autori publikácie
                                    chyba = importAutoriPubl(value, colNames[cellNum]);
                                    break;
                                case 20:
                                    // Rok vydania
                                    chyba = importRokVydania(value, colNames[cellNum]);
                                    break;
                                case 21:
                                    // Casopis
                                    chyba = importCasopis(value, colNames[cellNum]);
                                    break;
                                case 22:
                                    // Rocnik casopisu
                                    rocnikCasopisu = value;
                                    break;
                                case 23:
                                    // Cislo casopisu
                                    cisloCasopisu = value;
                                    break;
                                case 24:
                                    // Nazov clanku
                                    nazovClanku = value;
                                    break;
                                case 25:
                                    // Pramen
                                    pramen = value;
                                    break;
                                case 26:
                                    // Editori
                                    chyba = importEditoriPubl(value, colNames[cellNum]);
                                    break;
                                case 27:
                                    // Nazov publikacie / knihy
                                    nazovPublikacie = value;
                                    break;
                                case 28:
                                    // Nazov kapitoly
                                    nazovKapitoly = value;
                                    break;
                                case 29:
                                    // Vydavatel
                                    vydavatel = value;
                                    break;
                                case 30:
                                    // Strany publikacie
                                    stranyPublikacie = value;
                                    break;
                                case 31:
                                    // Strana s udajom
                                    stranaUdaj = value;
                                    break;
                                case 32:
                                    // Poznamka k lokalite
                                    poznamkaLok = value;
                                    break;
                                case 33:
                                    // Poznamka k literature
                                    poznamkaLit = value;
                                    break;
                                case 34:
                                    // Pristup k udaju
                                    chyba = importPristup(value, colNames[cellNum]);
                                    break;
                                default:
                            }

                            // ak v danej bunke bolo vyvolane nejake upozornenie alebo chyba, vypiseme ho
                            // v pripade chyby znemoznime nasledne importovanie
                            if (!chyba.equals("")){
                                
                                // vypisy chyb, pre sekcie ktore su neprazdne
                                if ((0 <= cellNum && cellNum <= newRecMaxCell && newRec) || (newRecMaxCell < cellNum && cellNum <= newLitMaxCell && newLit)){
                                    correctRow = false;
                                    if (chyba.startsWith("|ERROR|")) {
                                        importovat = false;
                                    }
                                    publish("Literárne údaje|" + String.valueOf(i) + chyba);
                                    //displayDelay(500000);
                                    chyba = "";
                                }
                            }
                        }
                    } else {
                        break;
                    }

                    if (correctRow) {
                        publish("Literárne údaje|" + String.valueOf(i) + "|OK| ");
                    }

                    // correct row or row only with warnings
                    if (importovat){

                        if (newRec != false) { //je zadany novy importUdaj na import

                            impUdaj = new Udaj();
                            
                            MenaTaxonov mt = new MenaTaxonov(los, nazovTaxonu, null, null);
                            SkupRev urcenie = new SkupRev(null, mt, null, Boolean.FALSE, null);
                            urcenie = ListAddRemoveUtils.setRevisorsAndIdentificators(urcenie, autoriUrcenia);

                            Lokality lokalita = new Lokality();
                            lokalita.setAltOd(nadmOd);
                            lokalita.setAltDo(nadmDo);
                            lokalita.setAltOdCca(cca);
                            lokalita.setBrumit4(brummit4);
                            lokalita.setLatitude(zemepisSirka);
                            lokalita.setLongitude(zemepisDlzka);
                            lokalita.setNgc(ngc);
                            lokalita.setObec(obec);
                            lokalita.setOpisLokality(opisLok);
                            lokalita.setPoznamkaLok(poznamkaLok);
                            lokalita = ListAddRemoveUtils.setFtgokresy(lokalita, ftgokresy);
                            lokalita = ListAddRemoveUtils.setQuadrants(lokalita, kvadranty);

                            HerbarPolozky herbPolozka = new HerbarPolozky();
                            herbPolozka.setHerbar(herbar);

                            impUdaj.getSkupRevs().add(urcenie);
                            impUdaj.setLokality(lokalita);
                            impUdaj.setHerbarPolozky(herbPolozka);
                            impUdaj.setTyp('L');
                            impUdaj.setUdajZberAsocs(ListAddRemoveUtils.setCollectors(impUdaj, autoriZberu));
                            impUdaj.setDatumZberu(datumZberu);
                            impUdaj.setDatumZberuSlovom(datumZberuSlovom);
                            impUdaj.setStranaUdaja(stranaUdaj);
                            impUdaj.setVerejnePristupny(isPristupny());
                            impUdaj.setVerejnePristupnyOd((!isPristupny() && !pristup.equals("N")) ? pristup : null);
                        }

                        if (newLit != false) { //NOVA zaznam o literature
                            
                            if (impUdaj != null){
                            
                                //List<LitZdrojRev> revs = new ArrayList<>(importUdaj.getLitZdrojRevs().size());
                                LitZdrojRev lzr = new LitZdrojRev();

                                LitZdroj lz = new LitZdroj();
                                lz.setCasopis(casopis);
                                lz.setRocnik(rocnikCasopisu);
                                lz.setCislo(cisloCasopisu);
                                lz.setNazovClanku(nazovClanku);
                                lz.setPramen(pramen);
                                lz.setNazovKnihy(nazovPublikacie);
                                lz.setNazovKapitoly(nazovKapitoly);
                                lz.setRok(rokVydania);
                                lz.setVydavatel(vydavatel);
                                lz.setStrany(stranyPublikacie);
                                lz.setPoznamka(poznamkaLit);

                                lz = ListAddRemoveUtils.setLitAuthors(lz, autoriPublikacie);
                                lz = ListAddRemoveUtils.setLitEditors(lz, editoriPublikacie);

                                lzr.setLitZdroj(lz);
                                //revs.add(lzr);

                                impUdaj.getLitZdrojRevs().add(lzr);
                            
                            } else {
                                log.error("Importovanie dat: Chyba! Revizia lit. zdroja nebola priradena, pretoze ju nepredchadzal ziaden zadany udaj", null);
                                throw new Exception();
                            }
                        }
                        if (newRec){
                            importUdaje.add(impUdaj);
                        } else {
                            // vymaz predchadzajuci(posledny) udaj bez revizii(lit./urc.) a pridaj novy s reviziami
                            importUdaje.remove(importUdaje.size()-1);
                            importUdaje.add(impUdaj);
                        }
                    }
                    //displayDelay(2000000);
                }
                
                resetImportVariables();
                
                if (isCancelled()){
                    return 0;
                }
            }

            // TERENNE POLOZKY
            for (int i = 0; i <= importUnpublSheet.getLastRowNum(); i++) { 

                Row rowImportUnpublSheet = importUnpublSheet.getRow(i);

                // nacitaj nazvy jednotlivych stlpcov z templatu (uvedene v prvom riadku)
                if (i == 0){
                    Row rowTemplateReadU = templateUnpublSheet.getRow(i);
                    rowLength = rowTemplateReadU.getLastCellNum();
                    colNames = new String[rowLength];
                    for (int j = 0; j < rowLength; j++) {
                        colNames[j] = rowTemplateReadU.getCell(j).getStringCellValue();
                    }
                } else if(i > 2){

                    boolean correctRow = true;
                    String chyba = "";

                    // klucove stlpce
                    boolean newRec;

                    // klucove cisla stlpcov ohranicujuce sekcie importovanych dat
                    int newRecMaxCell = 17;

                    // kontrola sekcie, ktora ked ma niektoru z buniek vyplnenu, tak sa bude importovat novy importUdaj (v opacnom pripade nie)
                    newRec = checkIfSectionNotEmpty(rowImportUnpublSheet, 0, newRecMaxCell);

                    // kontrola ci aspon jedna z casti je neprazdna ( = dany riadok nie je prazdny )
                    //  => import na danom sheete bude prebiehat dalej
                    // v opacnom pripade ukonci import daneho sheet-u (break)
                    if (newRec != false){
                        for (int cellNum = 0; cellNum < rowLength; cellNum++) {

                            String value = retrieveValue(rowImportUnpublSheet.getCell(cellNum));

                            switch (cellNum) {
                                case 0:
                                    // Nazov taxonu (nie je nutne vyzadovane)
                                    nazovTaxonu = value;
                                    break;
                                case 1:
                                    // Štandardizované znenie mena (povinny atribut)
                                    acceptMeno = value;
                                    //kontrola ci existuje v DB sa urobi v kontexte s autorskymi skratkami
                                    break;
                                case 2:
                                    // Autorské skratky (!nie je nutne vyzadovane)
                                    chyba = importAkcMeno(acceptMeno,  value, true);
                                    break;
                                case 3:
                                    // Brummitt 4
                                    chyba = importBrumit4(value,  colNames[cellNum]);
                                    break;
                                case 4:
                                    // Fytogeografický okres
                                    chyba = importFtgokres(value,  colNames[cellNum]);
                                    break;
                                case 5:
                                    // Nizsi geograficky celok
                                    chyba = importNgc(value,  colNames[cellNum]);
                                    break;
                                case 6:
                                    // Najbližšia obec
                                    chyba = importObec(value,  colNames[cellNum]);
                                    break;
                                case 7:
                                    // Opis lokality
                                    chyba = importOpisLokality(value,  colNames[cellNum]);
                                    break;
                                case 8:
                                    // Cca.
                                    chyba = importCca(value,  colNames[cellNum]);
                                    break;
                                case 9:
                                    // Nadm. výška OD
                                    chyba = importAltOd(value,  colNames[cellNum]);
                                    break;
                                case 10:
                                    // Nadm. výška DO
                                    chyba = importAltDo(value,  colNames[cellNum]);
                                    break;
                                case 11:
                                    // Kvadrant
                                    chyba = importKvadranty(value,  colNames[cellNum]);
                                    break;
                                case 12:
                                    // Zemepisná šírka
                                    chyba = importZemepisSirka(value,  colNames[cellNum]);
                                    break;
                                case 13:
                                    // Zemepisná dĺžka
                                    chyba = importZemepisDlzka(value,  colNames[cellNum]);
                                    break;
                                case 14:
                                    // Autor zberu / záznamu
                                    chyba = importAutorZberu(value, colNames[cellNum]);
                                    break;
                                case 15:
                                    // Dátum zberu
                                    chyba = importZberDatum(value, colNames[cellNum]);
                                    break;
                                case 16:
                                    // Dátum zberu slovom
                                    datumZberuSlovom = value;
                                    break;
                                case 17:
                                    // Autori urcenia
                                    chyba = importAutorUrcenia(value, colNames[cellNum]);
                                    break;
                                case 18:
                                    // Poznamka k lokalite
                                    poznamkaLok = value;
                                    break;
                                case 19:
                                    // Pristup k udaju
                                    chyba = importPristup(value, colNames[cellNum]);
                                    break;
                                default:
                            }

                            if (!chyba.equals("")){
                                correctRow = false;
                                if (chyba.startsWith("|ERROR|")) {
                                    importovat = false;
                                }
                                publish("Terénne údaje|" + String.valueOf(i) + chyba);
                                //displayDelay(500000);
                                chyba = "";
                            }
                        }
                    } else {
                        break;
                    }

                    if (correctRow) {
                        publish("Terénne údaje|" + String.valueOf(i) + "|OK| ");
                    }

                    if (importovat){

                        if (newRec != false) { //je zadany novy importUdaj na import

                            impUdaj = new Udaj();
                            
                            MenaTaxonov mt = new MenaTaxonov(los, nazovTaxonu, null, null);
                            SkupRev urcenie = new SkupRev(null, mt, null, Boolean.FALSE, null);
                            ListAddRemoveUtils.setRevisorsAndIdentificators(urcenie, autoriUrcenia);

                            Lokality lokalita = new Lokality();
                            lokalita.setAltOd(nadmOd);
                            lokalita.setAltDo(nadmDo);
                            lokalita.setAltOdCca(cca);
                            lokalita.setBrumit4(brummit4);
                            lokalita.setLatitude(zemepisSirka);
                            lokalita.setLongitude(zemepisDlzka);
                            lokalita.setNgc(ngc);
                            lokalita.setObec(obec);
                            lokalita.setOpisLokality(opisLok);
                            lokalita.setPoznamkaLok(poznamkaLok);
                            lokalita = ListAddRemoveUtils.setFtgokresy(lokalita, ftgokresy);
                            lokalita = ListAddRemoveUtils.setQuadrants(lokalita, kvadranty);

                            impUdaj.getSkupRevs().add(urcenie);
                            impUdaj.setLokality(lokalita);
                            impUdaj.setTyp('T');
                            impUdaj.setUdajZberAsocs(ListAddRemoveUtils.setCollectors(impUdaj, autoriZberu));
                            impUdaj.setDatumZberu(datumZberu);
                            impUdaj.setDatumZberuSlovom(datumZberuSlovom);
                            impUdaj.setStranaUdaja(stranaUdaj);
                            impUdaj.setVerejnePristupny(isPristupny());
                            impUdaj.setVerejnePristupnyOd((!isPristupny() && !pristup.equals("N")) ? pristup : null);
                        }
                        importUdaje.add(impUdaj);
                    }
                    //displayDelay(2000000);
                }
                
                resetImportVariables();
                
                if (isCancelled()){
                    return 0;
                }
            }

            if (inputStreamExcelTemplate != null){
                inputStreamExcelTemplate.close();
            }
            if (inputStreamExcelImportFile != null){
                inputStreamExcelImportFile.close();
            }

            if (importovat){
                log.info("Import file checking ended without errors.");
                JOptionPane.showMessageDialog(null, String.format("Načítavanie súboru skončilo. \nMôžte pristúpiť k samotnému importovaniu dát."), "Potvrdzujúca informácia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                log.info("Import file checking ended with errors. It should be replaced.");
                JOptionPane.showMessageDialog(null, String.format("Načítavanie súboru skončilo. \nProsím odstráňte zadané chyby."), "Upozornenie", JOptionPane.ERROR_MESSAGE);
            }
            im.setImportEnabled(importovat);
        
        } catch (Exception e){
            log.warn("Import problem: Exception occured while reading import data.", e);
            JOptionPane.showMessageDialog(im, "Import dát sa nepodaril. Vyskytla sa neošetrená výnimka v kóde.");
        }

        return 1;
    }  
    
  @Override
  protected void process(final List<String> chunks) {
    // Updates the messages text area
        for (final String string : chunks) {

            ImportMonitorModel model = (ImportMonitorModel) im.getImportMonitorTable().getModel();

            String[] messageParts = string.split("\\|");
            String typ = messageParts[0];
            int rowNum = Integer.parseInt(messageParts[1]);
            String status = messageParts[2];
            String chyba = messageParts[3];
            
            switch (status) {
                case "OK":
                    model.addRow(typ, rowNum+1, "", new ImageIcon(getClass().getResource("/ok.png")));
                    break;
                case "WARNING":
                    model.addRow(typ, rowNum+1, chyba, new ImageIcon(getClass().getResource("/warning.png")));
                    break;
                case "ERROR":
                    model.addRow(typ, rowNum+1, chyba, new ImageIcon(getClass().getResource("/error.png")));
                    break;
            }
        }
    }

    //na odsimulovanie zdrzania pocas spracovania udajov
    private void displayDelay(int delayTime) {
        
        ImportMonitorModel model = (ImportMonitorModel) im.getImportMonitorTable().getModel();
        model.fireTableDataChanged();
        for (int k = 0; k < delayTime; k++){
            System.out.print("");
        }
    }
    
    // sekcia s gettermi, ktore v pripade existujucej entity v prislusnej tabulke vratia referenciu na danu entitu, v opacnom pripade vratia null

    private ListOfSpecies getLOSByName(String acceptMeno, String autSkratka) {
        for (Entity ent : listLos)
        {
            ListOfSpecies lSpecies = (ListOfSpecies) ent;
            if (lSpecies.getMeno() != null && lSpecies.getMeno().equals(acceptMeno)){
                if (lSpecies.getAutori() != null && lSpecies.getAutori().equals(autSkratka)){
                    return lSpecies;
                }
            }
        }
        return null;
    }
    
    private Brumit4 getBrummit4ByName(String b4) {
        for (Entity ent : listBrumit4)
        {
            Brumit4 br4 = (Brumit4) ent;
            if (br4.getMeno() != null && br4.getMeno().equals(b4))return br4;
        }
        return null;
    }
    
    private Ftgokres getFtgOkresByNum(String ftgCislo){
        for (Entity ent : listFtgOkres)
        {
            Ftgokres ftg = (Ftgokres) ent;
            if (ftg.getCislo().equals(ftgCislo))return ftg;
        }
        return null;
    }
    
    private Ngc getNgcByName(String ngcNazov){
        for (Entity ent : listNgc)
        {
            Ngc ng = (Ngc) ent;
            if (ng.getMeno() != null && ng.getMeno().equals(ngcNazov))return ng;
        }
        return null;
    }
    
    private Obec getObecByName(String obecNazov){
        for (Entity ent : listObec)
        {
            Obec ob = (Obec) ent;
            if (ob.getMeno() != null && ob.getMeno().equals(obecNazov))return ob;
        }
        return null;
    }
    
    private Kvadrant getKvadrantByName(String kvadNazov){
        for (Entity ent : listKvadrant)
        {
            Kvadrant kvadrant = (Kvadrant) ent;
            if (kvadrant.getMeno() != null && kvadrant.getMeno().equals(kvadNazov))return kvadrant;
        }
        return null;
    }
    
    private MenaZberRev getAutorByName(String menoAutora){
        for (Entity ent : listAutorZberu)
        {
            MenaZberRev autor = (MenaZberRev) ent;
            if (autor.getMeno() != null && autor.getMeno().equals(menoAutora))return autor;
        }
        return null;
        
    }

    private Herbar getHerbarBySkratka(String herbSkr) {
        for (Entity ent : listHerb)
        {
            Herbar herb = (Herbar) ent;
            if (herb.getSkratkaHerb() != null && herb.getSkratkaHerb().equals(herbSkr))return herb;
        }
        return null;
    }

    private Exsikaty getExsikatByMeno(String exsikatNazov) {
        for (Entity ent : listExsikat)
        {
            Exsikaty exs = (Exsikaty) ent;
            if (exs.getMeno() != null && exs.getMeno().equals(exsikatNazov))return exs;
        }
        return null;
    }
    
    private Casopisy getCasopisByMeno(String casopisNazov) {
        for (Entity ent : listCasopis)
        {
            Casopisy cas = (Casopisy) ent;
            if (cas.getMeno() != null && cas.getMeno().equals(casopisNazov))return cas;
        }
        return null;
    }

    
    // return loadTable data and set progress of loading
    private List<Entity> loadData(String loadTable, int progress) {
        List<Entity> data = hq.getAllRecords(loadTable);
        this.progress += progress;
        setProgress(Math.min(this.progress, 100));
        return data;
    }

    // kontrola casti riadku, ci nie je prazdny
    // cast je ohranicena hodnotami startCell a endCell
    private boolean checkIfSectionNotEmpty(Row row, int startCell, int endCell) {
        for (int cellNum = startCell; cellNum <= endCell; cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null){
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        if (!String.valueOf(cell.getNumericCellValue()).trim().equals("")){
                            return true;
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        if (!cell.getStringCellValue().trim().equals("")){
                            return true;
                        }
                        break;
                    default:    //CELL_TYPE_BOOLEAN, CELL_TYPE_BLANK, CELL_TYPE_ERROR, CELL_TYPE_FORMULA ...
                        break; 
                }
            }
        }
        return false;
    }
    
    private String retrieveValue(Cell cell) {

        String value = "";
        // nacitanie hodnoty bunky, podla jej typu (ak nie je prazdna)
        if (cell != null){
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue()).trim();
                    if (value.contains(".")){
                        value = value.substring(0, value.indexOf("."));
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue().trim();
                    break;
                default:    //CELL_TYPE_BOOLEAN, CELL_TYPE_BLANK, CELL_TYPE_ERROR, CELL_TYPE_FORMULA ...
                    break;
            }
        }
        return value;
    }
    
    // metody obsluhujuce import jednotlivych stlpcov
       
    private String importBrumit4(String value, String colName) {
        if (!value.equals("")){
            String b4 = value;
            brummit4 = (Brumit4) getBrummit4ByName(b4);
            if (brummit4 == null){
                return "|ERROR|Stĺpec " + colName + " nie je evidovaný v databáze";
            }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importFtgokres(String value, String colName) {
        if (!value.equals("")){
            String[] ftgNames = value.split("/");
            for (String ftgName : ftgNames){
                ftgName = ftgName.trim();
                Ftgokres okres = (Ftgokres) getFtgOkresByNum(ftgName);
                if (okres != null){
                    ftgokresy.add(okres);
                } else {
                    return "|ERROR|Niektoré z údajov v stĺpci " + colName + " nie sú evidované v databáze";
                } 
            } 
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importNgc(String value, String colName) {
        String ngcMeno = value;
        ngc = (Ngc) getNgcByName(ngcMeno);
        if (!value.equals("") && ngc == null){
            //ak sa potvrdi ze sa chce importovat, bude treba vytvorit novy Ngc a hq.insertOrUpdateEntity(newEnt);
            ngc = new Ngc();
            ngc.setMeno(ngcMeno);
            ngc.setSchvalene(false);
            unsavedEntities.put("Ngc", ngc);
            return "|WARNING|Stĺpec " + colName + " nie je evidovaný v databáze";
        }
        return "";
    }

    private String importObec(String value, String colName) {
        if (!value.equals("")){
            String obecMeno = value;
            obec = (Obec) getObecByName(obecMeno);
            if (obec != null){

            } else {
                //ak sa potvrdi ze sa chce importovat, bude treba vytvorit novu obec
                obec = new Obec();
                obec.setMeno(obecMeno);
                obec.setSchvalene(false);
                unsavedEntities.put("Obec", obec);
                return "|WARNING|Stĺpec " + colName + " nie je evidovaný v databáze";
            } 
        } else {
            return "|ERROR|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }
    
    private String importOpisLokality(String value, String colName) {
        if (!value.equals("")){
            opisLok = value; 
         } else {
             return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
         }
        return "";
    }
    
    private String importCca(String value, String colName) {
        if (value.equals("cca")){
            cca = true;
        } else if (!value.equals("")){
            return "|ERROR|Stĺpec " + colName+ " nie je vyplnený korektne";
        } else {
            cca = false;
        }
        return "";
    }
    
    private String importKvadranty(String value, String colName) {
        if (!value.equals("")){
                String[] kvadNames = value.trim().split("/");
                for (String kvadName : kvadNames){
                    kvadName = kvadName.trim();
                    if (DataVerifier.isKvadrant(kvadName)){
                        Kvadrant kv = (Kvadrant) getKvadrantByName(kvadName);
                        if (kv != null){
                            kvadranty.add(kv);
                        } else {
                            //ak sa potvrdi ze sa chce importovat, bude treba vytvorit novy kvadrant
                            Kvadrant kvad = new Kvadrant();
                            kvad.setMeno(kvadName);
                            kvad.setSchvalene(false);
                            //kvad.setLokalityKvadrantAsocs(kvads);
                            kvadranty.add(kvad);
                            unsavedEntities.put("Kvadrant", kvad);
                            return "|WARNING|Niektoré z údajov v stĺpci " + colName + " nie sú evidované v databáze";
                        }
                    } else {
                        return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
                    }
                }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importAltOd(String value, String colName) {
        if (!value.equals("")){
            if (DataVerifier.isNadmorskaVyska(value)){
                nadmOd = Integer.parseInt(value);
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        }
        return "";
    }
    
    private String importAltDo(String value, String colName) {
        if (!value.equals("")){
            if (DataVerifier.isNadmorskaVyska(value)){
                nadmDo = Integer.parseInt(value);
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        }
        return "";
    }

    private String importZemepisSirka(String value, String colName) {
        if (!value.equals("")){
            if (DataVerifier.isZemepisnaPoloha(value)){
                zemepisSirka = HandyUtils.coordinatesToDoule(value);
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        }
        return "";
    }
    
    private String importZemepisDlzka(String value, String colName) {
        if (!value.equals("")){
            if (DataVerifier.isZemepisnaPoloha(value)){
                zemepisDlzka = HandyUtils.coordinatesToDoule(value);
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        }
        return "";
    }

    private String importAutorZberu(String value, String colName) {
        if (!value.equals("")){
            String[] zberNames = value.split(";");
            for (String zberName : zberNames){
                zberName = zberName.trim();
                MenaZberRev meno = (MenaZberRev) getAutorByName(zberName);
                if (meno != null){
                    autoriZberu.add(meno);
                } else {
                    
                    //ak sa potvrdi ze sa chce importovat, bude treba vytvorit nove meno
                    MenaZberRev autor = new MenaZberRev();
                    autor.setMeno(zberName);
                    autor.setSchvalene(false);
                    //autor.setUdajZberAsocs(zberatelia);
                    autoriZberu.add(autor);
                    unsavedEntities.put("AutorZberu", autor);
                    
                    return "|WARNING|Niektoré z údajov v stĺpci " + colName + " nie sú evidované v databáze";
                } 
            }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importAutorUrcenia(String value, String colName) {
        if (!value.equals("")) {
            String[] names = value.split(";");
            for (String name : names) {
                name = name.trim();
                MenaZberRev mzr = (MenaZberRev) getAutorByName(name);
                if (mzr != null){
                    autoriUrcenia.add(mzr);
                } else {
                    //ak sa potvrdi ze sa chce importovat, bude treba vytvorit nove meno
                    MenaZberRev autor = new MenaZberRev();
                    autor.setMeno(name);
                    autor.setSchvalene(false);
                    //autor.setSkupRevDets(urcovatelia);
                    autoriUrcenia.add(autor);
                    unsavedEntities.put("AutorUrcenia", autor);
                    
                    return "|WARNING|Niektoré z údajov v stĺpci " + colName + " nie sú evidované v databáze";
                }
            }
        }
        return "";
    }

    private String importZberDatum(String value, String colName) {
        if (!value.equals("")){
            if (DataVerifier.isCorrectDateStamp(value, true)){
                datumZberu = value;
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }
    
    private String importHerbar(String value, String colName) {
        if (!value.equals("")){
            String herbSkratka = value;
            herbar = getHerbarBySkratka(herbSkratka);
            if (herbar == null){
                return "|ERROR|Stĺpec " + colName + " nie je evidovaný v databáze";
            } 
        }
        return "";
    }

    private boolean isPristupny() {
        switch (pristup) {
            case "V":
                return true;
            case "N":
                return false;
            default:
                return false;
        }
    }

    private String importPristup(String value, String colName) {
        if (!value.isEmpty()){
            if (value.equals("V") || value.equals("N") || DataVerifier.isCorrectDateStamp(value, false)){
                pristup = value;
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importAutoriPubl(String value, String colName) {
        if (!value.equals("")) {
            String[] names = value.split(";");
            for (String name : names) {
                name = name.trim();
                MenaZberRev mzr = (MenaZberRev) getAutorByName(name);
                if (mzr != null){
                    autoriPublikacie.add(mzr);
                } else {
                    // bude treba pridat tohto noveho autora
                    MenaZberRev autor = new MenaZberRev();
                    autor.setMeno(name);
                    autor.setSchvalene(false);
                    //autor.setLzdrojAutoriAsocs(autori);
                    autoriPublikacie.add(autor);
                    unsavedEntities.put("AutorPublikacie", autor);
                    return "|WARNING|Niektorý z údajov v stĺpci " + colName+ " doposiaľ nebol evidovaný v databáze";
                }
            }
        }
        return "";
    }

    private String importEditoriPubl(String value, String colName) {
        if (!value.equals("")) {
            String[] names = value.split(";");
            for (String name : names) {
                name = name.trim();
                MenaZberRev mzr = (MenaZberRev) getAutorByName(name);
                if (mzr != null){
                    editoriPublikacie.add(mzr);
                } else {
                    //ak sa potvrdi ze sa chce importovat, bude treba vytvorit nove meno
                    MenaZberRev editor = new MenaZberRev();
                    editor.setMeno(name);
                    editor.setSchvalene(false);
                    //editor.setLzdrojEditoriAsocs(editori);
                    editoriPublikacie.add(editor);
                    unsavedEntities.put("EditorPublikacie", editor);
                    return "|WARNING|Niektorý z údajov v stĺpci " + colName + " doposiaľ nebol evidovaný v databáze";
                }
            }
        }
        return "";
    }

    private String importCasopis(String value, String colName) {
        String nazovCasopisu = value;
        casopis = getCasopisByMeno(nazovCasopisu);
        if (casopis != null){
            //v poriadku
        } else {
            return "|ERROR|Stĺpec " + colName + " doposiaľ nebol evidovaný v databáze";
        }
        return "";
    }

    private String importAkcMeno(String meno, String autori, boolean isUrcenie) {
        autSkratka = autori;
        ListOfSpecies losTemp = getLOSByName(meno, autori);
        if (losTemp == null){
            if (isUrcenie){
                    return "|ERROR|Štandardizované meno taxónu s danými autormi nie je evidované v databáze";
            } else { // je revizia
                if (meno.equals("") && autori.equals("")){
                    return "|WARNING|Revidované štandardizované meno taxónu s autormi nie je vyplnené";
                } else {
                    return "|ERROR|Revidované štandardizované meno taxónu s autormi nie je evidované v databáze";
                }
            } 
        } else {
            if (isUrcenie){
                los = losTemp;
            } else {
                losRev = losTemp;
            }
        }
        return "";
    }

    private String importExsikat(String value, String colName) {
        if (!value.isEmpty()){
            String exsikatNazov = value;
            exsikat = getExsikatByMeno(exsikatNazov);
            if (exsikat != null){
                //v poriadku
            } else {
                //bude treba vytvorit novy zaznam v tabulke
                exsikat = new Exsikaty();
                exsikat.setMeno(exsikatNazov);
                unsavedEntities.put("Exsikat", exsikat);
                return "|WARNING|Stĺpec " + colName + " doposiaľ nebol evidovaný v databáze";
            } 
        }
        return "";
    }

    private String importRokVydania(String value, String colName) {
        if (!value.isEmpty()){
            if (DataVerifier.isYear(value)){
                rokVydania = value;
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        }
        return "";
    }

    private String importDatumRevizie(String value, String colName) {
        if (!value.isEmpty()){
            if (value.equals("V") || value.equals("N") || DataVerifier.isCorrectDateStamp(value, true)){
                datumRevizie = value;
            } else {
                return "|ERROR|Stĺpec " + colName + " nie je vyplnený korektne";
            }
        } else {
            return "|WARNING|Stĺpec " + colName + " nie je vyplnený";
        }
        return "";
    }

    private String importAutoriRev(String value, String colName) {
        if (!value.equals("")) {
            String namesStr = value;
            String[] names = namesStr.split(";");
            for (String name : names) {
                name = name.trim();
                MenaZberRev mzr = (MenaZberRev) getAutorByName(name);
                if (mzr != null){
                    autoriRevizie.add(mzr);
                } else {
                    // bude treba pridat tohto noveho autora
                    MenaZberRev autor = new MenaZberRev();
                    autor.setMeno(name);
                    autor.setSchvalene(false);
                    //autor.setSkupRevDets(autori);
                    autoriRevizie.add(autor);
                    unsavedEntities.put("AutorRevizie", autor);
                    return "|WARNING|Niektorý z údajov v stĺpci " + colName + " doposiaľ nebol evidovaný v databáze";
                }
            }
        }
        return "";
    }

    private void resetImportVariables() {
        nazovTaxonu = "";
        acceptMeno = "";
        autSkratka = "";
        los = null;
        brummit4 = null;
        ftgokresy = new ArrayList();
        ngc = null;
        obec = null;
        opisLok = "";
        cca = false;
        nadmOd = 0;
        nadmDo = 0;
        kvadranty = new ArrayList();
        zemepisSirka = 0.0;
        zemepisDlzka = 0.0;
        autoriZberu = new ArrayList<>();
        autoriUrcenia = new ArrayList<>();
        autoriRevizie = new ArrayList<>();
        autoriPublikacie = new ArrayList<>();
        editoriPublikacie = new ArrayList<>();
        datumZberu = "";
        datumZberuSlovom = "";
        herbar = null;
        cisloPolozky = "";
        exsikat = null;
        cisloCiarKodu = "";
        cisloZberu = "";
        revSchedaMeno = "";
        acceptMenoRev = "";
        autSkratkaRev = "";
        losRev = null;
        datumRevizie = "";
        rokVydania = "";
        casopis = null;
        rocnikCasopisu = "";
        cisloCasopisu = "";
        nazovClanku = "";
        pramen = "";
        nazovPublikacie = "";
        nazovKapitoly = "";
        vydavatel = "";
        stranyPublikacie = "";
        stranaUdaj = "";
        poznamkaLok = "";
        poznamkaLit = "";
        pristup = "";
    }

    Map<String, Entity> getUnsavedEntities() {
        return this.unsavedEntities;
    }

    List<Udaj> getImportUdaje() {
        return this.importUdaje;
    }
          
}

