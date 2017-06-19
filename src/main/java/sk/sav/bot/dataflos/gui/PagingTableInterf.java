/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

/**
 * Each table that implements this interface is able to use Paginator
 * @author Matus
 */
public interface PagingTableInterf {
    
    void pageUp();
    void pageDown();
    void goToPage(int page);
    int getPageOffset();
    int getPageCount();
    
}
