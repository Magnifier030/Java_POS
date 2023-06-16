package models.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import models.Commodity.Commodity;
import models.Ingrediate.Ingrediate;
import models.Ingrediate.IngrediateList;

public class DBConnection {

    private static Connection conn = null;
    private static final String URL = "jdbc:mariadb://localhost:3307/db_pos";
    private static final String USER = "mis";
    private static final String PWD = "1234";

    //Get functions
    public DBConnection(){
        try {
            if (conn != null && !conn.isClosed()) {
                System.out.println("取得已連線靜態物件connection");
            } else {
                conn = DriverManager.getConnection(URL, USER, PWD);
                System.out.println("使用帳號與密碼連線到資料庫...");
            }
        } catch (SQLException ex) {
            System.out.println("連線錯誤!");
            System.out.println(ex.toString());
            //ex.printStackTrace(); 
        }
    }
    
    public static Map<String, Ingrediate> GetIngrediateAllByDict(){
        String query = "select * from `ingrediate`";
        Map<String, Ingrediate> dict = new Hashtable();
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();

            while (rset.next()) {       
//                System.out.println(rset.getString("commodity_id"));
                    
                Ingrediate i = new Ingrediate(
                        rset.getString("ingrediate_id"),
                        rset.getString("name"),
                        rset.getString("image"),
                        Integer.parseInt(rset.getString("cost")),
                        Integer.parseInt(rset.getString("nowStock")),
                        rset.getString("describe"),
                        rset.getString("origin")
                );
                
                dict.put(i.getID(), i);
//                System.out.println(i.getAllDatasForSave());
                
                //不要斷線，一直會用到，使用持續連線的方式
               //conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("getAllcommoditys異常:" + ex.toString());
        }
        System.out.println("ingrediates 資料庫取得成功");
        return dict;
    }
    
    public static Map<String, Commodity> GetCommodityAllByDict(String type){
        String query = "select * from `commodity`";
        Map<String, Commodity> dict = new Hashtable();
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();

            while (rset.next()) {
                if(rset.getString("purchased").equals(type)){
                    continue;
                }
                String is = "";
//                System.out.println(rset.getString("commodity_id"));
                query = String.format("select * from commodity_textbook_detail WHERE commodity_id = '%s' ", rset.getString("commodity_id"));
                ps = conn.prepareStatement(query);
                ResultSet rset2 = ps.executeQuery();
                while (rset2.next()){
                    query = String.format("select * from `ingrediate` WHERE `ingrediate_id` = '%s' ", rset2.getString("ingrediate_id"));
                    ps = conn.prepareStatement(query);
                    ResultSet rset3 = ps.executeQuery();
                    while (rset3.next()){
                        is += String.format("%s * %s-",rset3.getString("name"), rset2.getString("num"));
                    }
                }
                
                Commodity c = new Commodity(
                        rset.getString("commodity_id"),
                        rset.getString("commodity_name"),
                        rset.getString("image"),
                        Integer.parseInt(rset.getString("total_cost")),
                        Integer.parseInt(rset.getString("price")),
                        rset.getString("describe"),
                        is
                );
//                System.out.println(c.getID());
                dict.put(c.getID(), c);
//                System.out.println(c.getAllDatasForSave());
                
                //不要斷線，一直會用到，使用持續連線的方式
               //conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("getAllcommoditys異常:" + ex.toString());
        }
        
        return dict;
    }
    
    public static String GetCustomerRandomId(){
        String customer_id = "";
        
        //get customer_id
        try{
            String query = "SELECT `customer_id` FROM `customer` ORDER BY `customer_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                customer_id = rset.getString("customer_id");
            }
            int temp_id = (int)(Math.random()*(Integer.parseInt(customer_id.substring(3)))) + 1;
//            System.out.println(temp_id);
            customer_id = "cu-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                customer_id += "0";
            }
            
            customer_id += String.format("%s", temp_id);
        } catch(SQLException ex){
            System.out.println("GetCustomerRandomID_Get_customer_customerID異常:" + ex.toString()); 
        }
        
        return customer_id;
    }
    
    public static String GetCommodityRandomId(){
        String commodity_id = "";
        
        //get customer_id
        try{
            String query = "SELECT `commodity_id` FROM `commodity` " +
                           "ORDER BY `commodity_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                commodity_id = rset.getString("commodity_id");
            }
            int temp_id = (int)(Math.random()*(Integer.parseInt(commodity_id.substring(3)))) + 1;
            commodity_id = "c-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                commodity_id += "0";
            }
            commodity_id += String.format("%s", temp_id);
            
        } catch(SQLException ex){
            System.out.println("GetCommodityRandomID_Get_customer_customerID異常:" + ex.toString()); 
        }
//        System.out.println(commodity_id);
        
        return commodity_id;
    }
    
    public static String[] GetAccount(){
        String[] datas = {"0", "0"};
        try{
            String query = 
                           "SELECT * " +
                           "FROM   `account`";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                datas[0] = rset.getString("day");
                datas[1] = rset.getString("money");
            }
            
        } catch(SQLException ex){
            System.out.println("SaveAccount異常:" + ex.toString()); 
        }
        
        return datas;
    }
    
    public static String GetSaleOrderNewestId(){
        String order_id = "";
        try{
            String query = "SELECT `order_id` FROM `sale_order` " +
                           "ORDER BY `order_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                order_id = rset.getString("order_id");
            }
            int temp_id = Integer.parseInt(order_id.substring(3)) +1;
            order_id = "so-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                order_id += "0";
            }
            order_id += String.format("%s", temp_id);
//            System.out.println(order_id);
            
        } catch(SQLException ex){
            System.out.println("SaveSaleOrder_Get_saleOrder_orderID異常:" + ex.toString()); 
        }
        
        return order_id;
    }
    //Save functions
    public static String SaveCustomer(String n, String a, String p){
        String customer_id = "";
        String customer_name = n;
        String customer_address = a;
        String customer_phone = p;
         
        //get customer_id
        try{
            String query = "SELECT `customer_id` FROM `customer` " +
                           "ORDER BY `customer_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                customer_id = rset.getString("customer_id");
            }
            int temp_id = Integer.parseInt(customer_id.substring(3)) + 1;
            customer_id = "cu-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                customer_id += "0";
            }
            customer_id += String.format("%s", temp_id);
//            System.out.println(order_id);
            
        } catch(SQLException ex){
            System.out.println("SaveCustomer_Get_customer_customerID異常:" + ex.toString()); 
        }
        
        //save customer
        try{
            String query = String.format(
                            "INSERT INTO `customer`" + 
                            "(`customer_id`, `customer_name`, `customer_address`, `customer_phone`) VALUES" +
                            "('%s', '%s', '%s', %s)"
                            , customer_id, customer_name, customer_address, customer_phone);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
        } catch(SQLException ex){
            System.out.println("SaveCustomer_Insert_customer異常:" + ex.toString()); 
        }
        
        return customer_id;
    }
    public static void SaveSaleOrder(String oID, String customer_id, String[] cID, String[] q, String[] ti){
        String order_id = "";
        Date ndate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String date = ft.format(ndate);
        String[] commodity_id = cID;
        String[] quantity = q;
        String[] total_income = ti;
        
        //get order_id
        try{
            String query = "SELECT `order_id` FROM `sale_order` " +
                           "ORDER BY `order_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                order_id = rset.getString("order_id");
            }
            int temp_id = Integer.parseInt(order_id.substring(3)) + 1;
            order_id = "so-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                order_id += "0";
            }
            order_id += String.format("%s", temp_id);
//            System.out.println(order_id);
            
        } catch(SQLException ex){
            System.out.println("SaveSaleOrder_Get_saleOrder_orderID異常:" + ex.toString()); 
        }
        
        //order
        try{
            int total_revenue = 0;
            for(String n : total_income){
                if(n.equals("")){
                    continue;
                }
                total_revenue += Integer.parseInt(n);
            }
            String query = String.format(
                            "INSERT INTO `sale_order`" + 
                            "(`order_id`, `date`, `total_income`) VALUES" +
                            "('%s', '%s', '%s')"
                            , order_id, date, total_revenue);
           
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
        } catch(SQLException ex){
            System.out.println("SaveSaleOrder_Insert__SaleOrder異常:" + ex.toString()); 
        }
        
        //orderDetail
        try{
            for(int i =0; i < cID.length; i++){
                System.out.println(String.format("%s %s %s %s %s", order_id, customer_id, commodity_id[i], q[i], total_income[i]));
                String query = String.format(
                            "INSERT INTO `sale_order_detail` (`order_id`, `customer_id`, `commodity_id`, `quantity`, `total_income`) VALUES ('%s', '%s', '%s', %s, %s);"
                            , order_id, customer_id, commodity_id[i], q[i], total_income[i]);
           
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rset = ps.executeQuery();
            }
        } catch(SQLException ex){
            System.out.println("SaveSaleOrder_Insert__SaleOrderDetail異常:" + ex.toString()); 
        }
    }
    
    public static void SavePurchaseOrder(int q, int c, String iID){
        String order_id = "";
        Date ndate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String date = ft.format(ndate);
        String ingrediate_id = iID;
        int quantity = q;
        int total_cost = c;
        
        try{
            String query = "SELECT `order_id` FROM `purchase_order` " +
                           "ORDER BY `order_id` DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            if(rset.first()){
                order_id = rset.getString("order_id");
            }
            int temp_id = Integer.parseInt(order_id.substring(3)) + 1;
            order_id = "po-";
            for(int i = 0; i<(2 - ((int)Math.log10(temp_id))); i++){
                order_id += "0";
            }
            order_id += String.format("%s", temp_id);
//            System.out.println(order_id);
            
        } catch(SQLException ex){
            System.out.println("SaveOrder_Get_purchaseOrder_orderID異常:" + ex.toString()); 
        }
        
        try{
            String query = String.format(
                            "INSERT INTO `purchase_order`" + 
                            "(`order_id`, `date`, `ingrediate_id`, `quantity`, `total_cost`) VALUES" +
                            "('%s', '%s', '%s', %s, %s)"
                            , order_id, date, ingrediate_id, quantity, total_cost);
           
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
        } catch(SQLException ex){
            System.out.println("SaveOrder_Insert__purchaseOrder異常:" + ex.toString()); 
        }
    }
    
    public static void SaveIngrediate(String id, IngrediateList iList){
        try{
            String query = String.format(
                           "UPDATE `ingrediate` " +
                           "SET    `nowStock` = %s " +
                           "WHERE  `ingrediate_id` = '%s'"
                           ,iList.GetIngrediateByID(id).getNowStock(), id);
//            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            
        } catch(SQLException ex){
            System.out.println("SaveCookBook異常:" + ex.toString()); 
        }
    }
    
    public static void SaveCookBook(String id){
        try{
            String query = String.format(
                           "UPDATE `commodity`\n" +
                           "SET    `purchased` = 'y' " +
                           "WHERE  `commodity_id` = '%s'"
                           , id);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            
        } catch(SQLException ex){
            System.out.println("SaveCookBook異常:" + ex.toString()); 
        }
    }
    
    public static void SaveAccount(int money, int day){
        try{
            String query = String.format(
                           "UPDATE `account`\n" +
                           "SET    `money` = %s, `day` = %s \n" +
                           "WHERE  `id` = 'a-001'"
                           , money, day);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rset = ps.executeQuery();
            
        } catch(SQLException ex){
            System.out.println("SaveAccount異常:" + ex.toString()); 
        }
    }

}
