/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.exception;

/**
 *
 * @author Matus
 */
public class EntityException extends Exception {

    public EntityException(Throwable cause) {
        super(cause);
    }

    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityException(String message) {
        super(message);
    }

    public EntityException() {
    }
    
}
