/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.Ingrediate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import models.Commodity.Commodity;
import models.DB.DBConnection;

/**
 *
 * @author 99027
 */
public class IngrediateList {
    public Map<String, Ingrediate> dict;
    private List<Ingrediate> iList;
    
    public IngrediateList(){
        iList = new ArrayList<Ingrediate>();
        dict = DBConnection.GetIngrediateAllByDict();
        
        for(Ingrediate i : dict.values()){
            iList.add(i);
        }
    }
    
    public void add(Ingrediate i){
        dict.put(i.getID(), i);
        iList.add(i);
        System.out.println(dict.get(i.getID()).getAllDatasForSave());
    }
    
    public Ingrediate GetIngrediateByID(String id){
        return (Ingrediate)dict.get(id);
    }
    public List<Ingrediate> GetList(){
        return iList;
    }
}
