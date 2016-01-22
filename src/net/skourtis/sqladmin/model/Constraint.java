/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.model;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Stavros
 */
public class Constraint {
    
    private HashMap<String,String> map;
 
    public Constraint() {
        map = new HashMap<>();
    }
    
    public String get(String key){
        return map.get(key);
    }
    
    public void set(String key,String data){
        map.put(key, data);        
    }
    
    public Set<String> getKeys(){
        return map.keySet();
    }
}
