package models.Commodity;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import models.Ingrediate.IngrediateList;

public class Commodity {
    private String id;
    private String name;
    private String image;
    private int cost;
    private int price;
    private List<String> ingrediates;
    private String describe;
//    private int sellNum;
//    private int customerNum;
    
    public Commodity(String id, String name, String image, int cost, int price, String describe, String ingrediates){
        this.id = id;
        this.name = name;
        this.image = image;
        this.cost = cost;
        this.price = price;
        this.describe = describe;
        this.ingrediates = new ArrayList<String>();
        for(String i : ingrediates.split("-")){
            this.ingrediates.add(i);
        }
    }
    
    public String[] getAllDatas(){
        String ingrediates = "";
        for(String i : this.ingrediates){
            ingrediates += String.format("%s-", i);
        }
        
        return  new String[]{id, name, image, String.format("%d",cost), String.format("%d",price), describe, ingrediates};
    }
    
    public String getAllDatasForSave(){
        String ingrediates = "";
        for(String i : this.ingrediates){
            ingrediates += String.format("%s-", i);
        }
        return String.format("%s, %s, %s, %s, %s, %s, %s;", id, name, image, cost, price, describe, ingrediates);
    }
    
    public String getID(){
        return id;
    }
    public int getIncome(){
        return price;
    }
    public boolean Check(List<String> ingrediates){
        for(String i : ingrediates){
            if(!this.ingrediates.contains(i)){
                return false;
            }
        }
        return true;
    }
}
