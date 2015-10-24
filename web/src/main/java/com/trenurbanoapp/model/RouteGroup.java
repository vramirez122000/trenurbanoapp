package com.trenurbanoapp.model;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 9/9/13
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public enum RouteGroup {

    ATI("Autoridad de Transporte Integrado"),
    TREN_URBANO("Autoridad de Transporte Integrado"),
    ACUAEXPRESO("Acuaexpreso"),
    ;

    private String name;

    private RouteGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
