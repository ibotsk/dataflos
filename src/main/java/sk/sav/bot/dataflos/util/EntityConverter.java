/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.util;

import sk.sav.bot.dataflos.entity.interf.Entity;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Matus
 */
public class EntityConverter extends Converter {
    
    private Entity entity;
    
    public EntityConverter(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Object convertForward(Object value) {
        return value.toString();
    }

    @Override
    public Object convertReverse(Object value) {
        return this.entity;
    }
    
}
