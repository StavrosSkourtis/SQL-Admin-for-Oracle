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
public class Procedure extends SqlProcedure{   
    public Procedure(){}
    public Procedure(String code,String name){
        super(code, name);
    }
}
