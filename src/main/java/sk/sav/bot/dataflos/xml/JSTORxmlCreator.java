/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.xml;

import sk.sav.bot.dataflos.entity.Brumit3;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.Udaj;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matus
 */
public class JSTORxmlCreator {

    private static JSTORxmlCreator instance;

    private JSTORxmlCreator() {
    }

    public static JSTORxmlCreator getInstance() {
        if (instance == null) {
            instance = new JSTORxmlCreator();
        }
        return instance;
    }

    public void createJSTORxml(List<Udaj> units) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        //root element
        Element dataSet = doc.createElement("DataSet");
        doc.appendChild(dataSet);
        dataSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        dataSet.setAttribute("xsi:noNamespaceSchemaLocation", "http://plants.jstor.org/XSD/AfricanTypesv2.xsd");

        //constant elements
        Element institutionCode = doc.createElement("InstitutionCode");
        institutionCode.setTextContent("SAV");
        dataSet.appendChild(institutionCode);
        Element institutionName = doc.createElement("InstitutionName");
        institutionName.setTextContent("Institute of Botany of Slovak Academy of Sciences");
        dataSet.appendChild(institutionName);
        Element dateSupplied = doc.createElement("DateSupplied");
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateSupplied.setTextContent(df.format(cal.getTime()));
        dataSet.appendChild(dateSupplied);
        Element personName = doc.createElement("PersonName");
        personName.setTextContent("Matúš Kempa");
        dataSet.appendChild(personName);

        //unit
        for (Udaj udaj : units) {
            Element unit = doc.createElement("Unit");
            dataSet.appendChild(unit);

            Element unitId = doc.createElement("UnitID");
            unitId.setTextContent(udaj.getHerbarPolozky().getCisloCkFull());
            unit.appendChild(unitId);

            Element dateLastModified = doc.createElement("DateLastModified");
            unit.appendChild(dateLastModified);

            writeIdentification(doc, unit, udaj.getUrcenie());

            for (SkupRev skupRev : udaj.getRevisions()) {
                writeIdentification(doc, unit, skupRev);
            }

            Element collectors = doc.createElement("Collectors");
            String colls = StringUtils.join(udaj.getUdajZberAsocs(), "; ");
            collectors.setTextContent(colls);
            unit.appendChild(collectors);

            Element collNumber = doc.createElement("CollectorNumber");
            String number = udaj.getHerbarPolozky().getCisloPol();
            collNumber.setTextContent(number == null ? "s.n." : number);
            unit.appendChild(collNumber);

            Element collDate = doc.createElement("CollectionDate");
            String date = udaj.getDatumZberu();
            if (date == null) {
                date = "00000000";
            }
            writeDMElement(doc, collDate, date.substring(6), true);
            writeDMElement(doc, collDate, date.substring(4, 6), false);
            Element collYear = doc.createElement("StartYear");
            collYear.setTextContent(date.substring(0, 4));
            collDate.appendChild(collYear);
            
            Brumit3 br = udaj.getLokality().getBrumit3();
            Element country = doc.createElement("Country");
            country.setTextContent(br == null ? "n/a" : br.getMeno());
            unit.appendChild(country);
           
            Element isoCode = doc.createElement("Iso2Letter");
            isoCode.setTextContent(br == null ? "n/a" : br.getIso());
            unit.appendChild(isoCode);
            
            Element locality = doc.createElement("Locality");
            locality.setTextContent(udaj.getLokality().getOpisLokality());
            unit.appendChild(locality);
            
            if (udaj.getLokality().getAltOd() != null) {
                Element altitude = doc.createElement("Altitude");
                altitude.setTextContent(udaj.getLokality().getAltOd().toString());
                unit.appendChild(altitude);
            }
            //notes
            if (udaj.getLokality().getPoznamkaLok() != null) {
                Element notes = doc.createElement("Notes");
                notes.setTextContent(udaj.getLokality().getPoznamkaLok());
                unit.appendChild(notes);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        //StreamResult result = new StreamResult(new File("C:\\Users\\Matus\\Documents\\NetBeansProjects\\DataflosImportJ\\excel_templ\\result.xml"));

        // Output to console for testing
        StreamResult result = new StreamResult(System.out);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, result);

        System.out.println("File saved!");
    }

    private void writeIdentification(Document doc, Element parent, SkupRev skupRev) {
        Element identification = doc.createElement("Identification");
        parent.appendChild(identification);

        Element family = doc.createElement("Family");
        family.setTextContent(skupRev.getMenaTaxonov().getSpecies().getGenus().getFamily().getMeno());
        identification.appendChild(family);

        Element genus = doc.createElement("Genus");
        genus.setTextContent(skupRev.getMenaTaxonov().getSpecies().getGenus().getMeno());
        identification.appendChild(genus);

        Element species = doc.createElement("Species");
        String[] speciesMeno = skupRev.getMenaTaxonov().getSpecies().getMeno().split(" ");
        species.setTextContent(speciesMeno[1]);
        identification.appendChild(species);

        Element author = doc.createElement("Author");
        author.setTextContent(skupRev.getMenaTaxonov().getSpecies().getAutori());
        identification.appendChild(author);
        if (speciesMeno.length > 3 && (speciesMeno[2].equals("var.") || speciesMeno[2].equals("subsp."))) {
            Element infraSpecRank = doc.createElement("Infra-specificRank");
            infraSpecRank.setTextContent(speciesMeno[3]);
            identification.appendChild(infraSpecRank);
        }

        if (!skupRev.getSkupRevDets().isEmpty()) {
            String identifSt = StringUtils.join(skupRev.getSkupRevDets().iterator(), "; ");
            Element identifier = doc.createElement("Identifier");
            identifier.setTextContent(identifSt);
            identification.appendChild(identifier);
        }

        Element identDate = doc.createElement("IdentificationDate");
        identification.appendChild(identDate);
        String date = skupRev.getDatum();
        if (date == null || date.isEmpty()) {
            date = "00000000";
        }
        String day = date.substring(6);
        String month = date.substring(4, 6);
        String year = date.substring(0, 4);
        writeDMElement(doc, identDate, day, true);
        writeDMElement(doc, identDate, month, false);

        Element startYear = doc.createElement("StartYear");
        startYear.setTextContent(year);
        identDate.appendChild(startYear);

        Element typeStatus = doc.createElement("TypeStatus");
        identification.appendChild(typeStatus);

    }

    private void writeDMElement(Document doc, Element parent, String string, boolean isDay) {
        Element element;
        if (!string.equals("00")) {
            if (isDay) {
                element = doc.createElement("StartDay");
            } else {
                element = doc.createElement("StartMonth");
            }
            element.setTextContent(string);
            parent.appendChild(element);
        }
    }
}
