package com.yy.ent.mvc.ioc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public interface EventListener {


    public enum EventType {
        BEFORE_CHERRICE
    }

    public abstract void beforeCherrice() throws Exception;
}
