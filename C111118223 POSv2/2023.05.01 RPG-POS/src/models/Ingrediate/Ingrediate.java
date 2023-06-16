/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.Ingrediate;

import java.util.Comparator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author 99027
 */
public class Ingrediate {
    private String id;
    private String name;
    private String image;
    private int cost;
    private int nowStock;
    private String describe;
    private String origin;
    
    public Ingrediate(String id, String name, String image, int cost, int nowStock, String describe, String origin){
        this.id = id;
        this.name = name;
        this.image = image;
        this.cost = cost;
        this.nowStock = nowStock;
        this.describe = describe;
        this.origin = origin;
    }
    
    public void SetNowStock(int nowStock){
        this.nowStock += nowStock;
    }
    
    public String[] getAllDatas(){
        
        return  new String[]{id, name, image, String.format("%d",cost),String.format("%d",nowStock), describe, origin};
    }
    
    public String getAllDatasForSave(){
        return String.format("%s, %s, %s, %s, %s, %s, %s;", id, name, image, cost, nowStock, describe, origin);
    }
    
    public String getID(){
        return id;
    }
    
    public int getNowStock(){
        return nowStock;
    }
}