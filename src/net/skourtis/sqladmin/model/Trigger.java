/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.model;

/**
 *
 * @author Stavros
 */
public class Trigger extends SqlProcedure{

    public Trigger(){}
    public Trigger(String code,String name){
        super(code,name);
    }

    
}
