/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 *
 * @author Matus
 */
public class HandyUtils {

    private static Logger log = Logger.getLogger(HandyUtils.class.getName());

    /**
     * eToN = empty to null
     *
     * @param str
     * @return null if str is empty, otherwise str
     */
    public static String eToN(String str) {

        if (str == null) {
            log.error("In eToN method is argument NULL");
            throw new NullPointerException("eToN");
        }

        if (str.isEmpty()) {
            return null;
        }
        return str;
    }

    /**
     * Is null
     *
     * @param string
     * @return
     */
    public static boolean isNE(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Create standardized date format if year is empty or contain
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public static String createDate(String day, String month, String year) {

        if (day == null || month == null || year == null) {
            log.error("In createDate method is at least one argument NULL");
            throw new NullPointerException("createDate util");
        }

        if (year.trim().isEmpty()) {
            return "";
        } else {
            switch (year.length()) {
                case 1:
                    year = "0" + year;
                case 2:
                    year = "0" + year;
                case 3:
                    year = "0" + year;
            }
        }
        return year + (month.isEmpty() ? "00" : month.length() == 1 ? "0" + month : month) + (day.isEmpty() ? "00" : day.length() == 1 ? "0" + day : day);
    }

    public static String convertStringToDate(String dateString) {

        String date = "";
        if (dateString != null) {
            // should be of format YYYYMMDD
            if (dateString.equals("") || dateString.length() != 8) {
                log.error("Inappropriate string date format");
                return "";
            }
            String year = dateString.substring(0, 4);
            if (!year.equals("0000")) {
                String month = dateString.substring(4, 6);
                String day = dateString.substring(6, 8);
                if (day.equals("00") || month.equals("00")) {
                    date = year;
                } else {
                    date = day + ". " + month + ". " + year;
                }
            }
        }
        return date;
    }

    // kodovanie obrazka do pola byte-ov, pre nasledne ulozenie do databazy
    public static byte[] imageToByteArray(BufferedImage image) {
        if (image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                return imageInByte;
            } catch (IOException ex) {
                log.error("error while converting to byte array", ex);
            }
            WritableRaster raster = image.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            return data.getData();
        }
        return new byte[0];
    }

    public static String doubleToCoordinates(Double value) {

        double integralPart = Math.floor(value);
        double fractionalPart = Math.abs(value - integralPart);

        String deg = String.valueOf((int) integralPart);
        if (deg.length() < 2) {
            deg = "0" + deg;
        }

        double newMinuteValue = fractionalPart * 60;

        fractionalPart = newMinuteValue % 1;
        integralPart = newMinuteValue - fractionalPart;

        String min = String.valueOf((int) integralPart);
        if (min.length() < 2) {
            min = "0" + min;
        }

        //pri sekundach nas zaujimaju 3 desatinne miesta
        DecimalFormat df = new DecimalFormat("##.####");
        DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
        decimalSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalSymbols);

        double secondValue = fractionalPart * 60;
        String sec = String.valueOf(df.format(secondValue));
        if ((!sec.contains(".") && sec.length() < 2) || (sec.contains(".") && sec.substring(0, sec.indexOf(".")).length() < 2)) {
            sec = "0" + sec;
        }

        return deg + "°" + min + "'" + sec + "''";
    }

    public static double coordinatesToDoule(String value) {
        double deg = Double.parseDouble(value.substring(0, value.indexOf('°')));
        double min = Double.parseDouble(value.substring(value.indexOf('°') + 1, value.indexOf('\'')));
        double sec = Double.parseDouble(value.substring(value.indexOf('\'') + 1, value.indexOf("\'\'")));

        return deg + min / 60 + sec / 3600;
    }

    /**
     * Properly creates ImageIcon.
     * @param cls Class to get resource path from
     * @param path path to the image
     * @return ImageIcon if image exists, null otherwise
     */
    public static ImageIcon createImageIcon(Class cls, String path) {
        java.net.URL imgURL = cls.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
