package models.Commodity;

import com.sun.deploy.util.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import models.Commodity.Commodity;
import models.DB.DBConnection;
import models.Ingrediate.Ingrediate;


public class CommodityList {
    public Map<String, Commodity> dict;
    private List<Commodity> cList;
    private int num = 0;
    
    public CommodityList(String type){
        dict = DBConnection.GetCommodityAllByDict(type);
        cList = new ArrayList<Commodity>();
        
        for(Commodity c : dict.values()){
            cList.add(c);
            num += 1;
        }     
        
    }
    
    public int GetLength(){
        return num;
    }
    
    public Commodity delete(String id){
        Commodity c = (Commodity)dict.get(id);
        
        cList.remove(c);
        dict.remove(c);
        
        return c;
    }
    
    public void add(Commodity c){
        dict.put(c.getID(), c);
        cList.add(c);
    }
    public Commodity GetCommoidtyByID(String id){
        return (Commodity)dict.get(id);
    }
    
    public List<Commodity> GetList(){
        return cList;
    }
}
