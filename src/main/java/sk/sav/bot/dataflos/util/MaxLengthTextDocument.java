/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Matus
 */
public class MaxLengthTextDocument extends PlainDocument {
    //Store maximum characters permitted
    private int maxChars;

    public MaxLengthTextDocument() {
        
    }
    
    public MaxLengthTextDocument(int maxChars) {
        this.maxChars = maxChars;
    }
 
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && (getLength() + str.length() < this.maxChars + 1)) {
            super.insertString(offs, str, a);
        }
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }
 
}

