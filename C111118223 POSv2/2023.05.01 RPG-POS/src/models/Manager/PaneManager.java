/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.Manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.DB.DBConnection;
import models.Ingrediate.Ingrediate;

/**
 *
 * @author 99027
 */
public class PaneManager {
    private GameManager gm;                           //gamemanager
    private Pane root;                               //root
    private Pane allPane = new Pane();              //the main Pane
    private VBox mainPane;                         //shop pane
    private VBox gamePane;                        //analyze pane
    private TilePane shopMenu;                   //commodity and ingrediate and purchased commodity pane
    private Label nowDay;                       //now day
    private Label nowMoney;                    //now money
    private StackPane vicePane;               //detail of commodity or ingrediate pane
    private DBConnection db;                 //database
    private VBox nextDayPane  = new VBox(); //next day game pane
    
    public PaneManager(Pane root){
        
        //get root pane
        this.root = root;
        TextArea news = new TextArea();
        
        //create all pane and initial the vicePane
        StartBorderPane();
        MainPane(news);
        GamePane(); 
        allPane.getChildren().add(nextDayPane);
        nextDayPane.setVisible(false);
        
        nextDayPane.getChildren().add(news);
        Button leave = new Button("leave");
        leave.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){      
                nextDayPane.setVisible(false);
                mainPane.setVisible(true);
                updateShopPane(gm.UpdateShopMenu(0), gm.GetDay(), gm.GetMoney());
            }
        });
        nextDayPane.getChildren().add(leave);
        
        vicePane = new StackPane();
        vicePane.setPrefSize(500, 420);
        vicePane.setVisible(false);
        
        //set panes into root
        this.root.getChildren().add(allPane);
        this.root.getChildren().add(vicePane);
        
        //inform successful creation of PaneManager
//        System.out.println("PaneManager create successfully!");
    }
    
    public void GamePane(){
        //base pane
        VBox basePane = new VBox();
        basePane.setPadding(new Insets(10, 10, 10, 10));
        basePane.setPrefSize(500, 350); 
        basePane.setSpacing(15);
        basePane.setAlignment(Pos.CENTER);
        
        //menu
        HBox menus = new HBox();
        menus.setAlignment(Pos.CENTER);
        menus.setSpacing(10);
        basePane.getChildren().add(menus);
        
        //Main Pane
        TextArea main = new TextArea();
        main.setPrefSize(400, 400);
        basePane.getChildren().add(main);
        
        //buttons
        Button AllDetail = new Button("all record");
        AllDetail.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                main.setText(gm.getALLRecord());
            }
        });
        menus.getChildren().add(AllDetail);

        Button datas = new Button("datas");
        datas.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                main.setText(gm.getRecordData());
            }
        });
        menus.getChildren().add(datas);   
        
        //leave
        Button leave= new Button("leave");    
        Button submit = new Button("purchase");
        leave.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){      
                gamePane.setVisible(false);
                mainPane.setVisible(true);
            }
        });
        basePane.getChildren().add(leave);

        //store pane
        allPane.getChildren().add(basePane);
        basePane.setVisible(false);
        gamePane = basePane;
    }
    
    //create shopPane
    public void updateShopPane(List<Button> menu, String day, String money){
        //update the date and property
        nowDay.setText(String.format("Day %s",day));
        nowMoney.setText(String.format("Propertys: %s",money));
        
        //update the menu
        shopMenu.getChildren().clear();
        for(Button b : menu){
            shopMenu.getChildren().add(b);
//            System.out.println("create a new button");
        }   
    }
    
    //public Pane MainPane
    private void MainPane(TextArea news){
        
        //set the VBox mainPane
        VBox mainPane = new VBox();
//        System.out.println("create new VBox mainPane");
        mainPane.setPrefSize(500, 500);
        mainPane.setPadding(new Insets(10, 10, 10, 10));//內部上下左右縮各10公分
        
        //set the BorderPane title on the floor of space
        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.setSpacing(50);
//        System.out.println("create a new BorderPane title");
        title.setPrefSize(500, 80);
        Label titleDayLabel = new Label("Day 1");
        Label titleMoneyLabel = new Label("Property: 1000");
        titleDayLabel.setFont(new Font("Arial", 30));
        titleMoneyLabel.setFont(new Font("Arial", 30));
        title.getChildren().add(titleDayLabel);
        title.getChildren().add(titleMoneyLabel);
        mainPane.getChildren().add(title); //add the borderPane into mainPane
//        System.out.println("set title into mainPane");
        
        //set the typeMenu
        HBox typeMenu = new HBox();
        typeMenu.setAlignment(Pos.CENTER);
        typeMenu.setSpacing(25);
        Button ingrediate = new Button("Ingrediate");  
        Button CookBook = new Button("CookBook");
        Button MyCommodity = new Button("MyCommodity");
        typeMenu.getChildren().add(ingrediate);
        typeMenu.getChildren().add(CookBook);
        typeMenu.getChildren().add(MyCommodity);
        typeMenu.setPadding(new Insets(25, 0, 10, 0));
        mainPane.getChildren().add(typeMenu);
        
        //set switch menu
        ingrediate.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                //delete all allPane's children
                shopMenu.getChildren().clear();
//                System.out.println("allPane'children cleared");
                
                //add mode 1 menu
                for(Button b : gm.UpdateShopMenu(0)){
                    shopMenu.getChildren().add(b);
                }
            }
        });
        CookBook.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                //delete all allPane's children
                shopMenu.getChildren().clear();
//                System.out.println("allPane'children cleared");
                
                //add mode 2 menu
                for(Button b : gm.UpdateShopMenu(1)){
                    shopMenu.getChildren().add(b);
                }
                
            }
        });
        MyCommodity.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                //delete all allPane's children
                shopMenu.getChildren().clear();
//                System.out.println("allPane'children cleared");
                
                //add mode 2 menu
                for(Button b : gm.UpdateShopMenu(2)){
                    shopMenu.getChildren().add(b);
                }
                
            }
        });
        
        //set the TilePane shopMenu on the botton of space
        TilePane shopMenu = new TilePane();
//        System.out.println("create a new TilePane shopMenu");
        shopMenu.setPrefSize(500, 420);
        shopMenu.setVgap(10);//垂直間隙
        shopMenu.setHgap(10);//水平間隙
        shopMenu.setPrefColumns(4);//每行四格
        mainPane.getChildren().add(shopMenu);
//        System.out.println("set shopMenu into mainPane");
        
        //set button for change to next day
        Button analyze = new Button("analyze");
        analyze.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
//                gm.PlusDay();
                mainPane.setVisible(false);
                gamePane.setVisible(true);
            }
        });
        
        Button nextDay = new Button("next day");
        nextDay.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
//                gm.PlusDay();
                mainPane.setVisible(false);
                nextDayPane(news);           
            }
        });
        
        VBox nextCotainer = new VBox();
        nextCotainer.setAlignment(Pos.CENTER_RIGHT);
        nextCotainer.getChildren().add(analyze);
        nextCotainer.getChildren().add(nextDay);
        mainPane.getChildren().add(nextCotainer);
        
//        //Check exist of file of save
//        if(CheckSave()){
//            titleLabel.setText(String.format("Day %s",));
//        }
        
        //return VBox mainPane
        this.mainPane = mainPane;
        this.nowDay = titleDayLabel;
        this.shopMenu = shopMenu;
        this.nowMoney = titleMoneyLabel;
    }
    
    //set DataBase
    public void SetDataBase(DBConnection db){
        this.db = db;
    }
    
    //set GameManager
    public void SetGameManager(GameManager gm){
        this.gm = gm;
    }
    
    //choose mode
    private void StartBorderPane(){
        //BorderPane startBorderPane
        BorderPane startBorderPane = new BorderPane();
//        System.out.println("create new Pane startBorderPane");
        startBorderPane.setPrefSize(500, 350);
        
        
        //Button mode 1 : seller
        Button seller = new Button();
        startBorderPane.setCenter(seller);//set Button in center of startBorderPane
        seller.setText("To be a seller");
        seller.setPrefSize(150, 100);
        seller.setOnAction(new EventHandler<ActionEvent>(){ //add switch the Pane
            @Override
            public void handle(ActionEvent e){      
                //delete all allPane's children
                allPane.getChildren().remove(0);
//                System.out.println("allPane'children cleared");
                
                //add mode 1 Pane
                allPane.getChildren().add(mainPane); 
//                System.out.println("set startBorderPane into mainPane");
            }
        });
        
        //let startBorderPane into allPane
        allPane.getChildren().add(startBorderPane);
//        System.out.println("set startBorderPane into allPane");
    }
    
    //get the ingrediate data
    public void showIngrediateData(String[] datas){
        //set a new VBox pane 
        VBox pane = new VBox();
        //pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setPrefSize(250, 250);
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(15);
        
        //vicePane setting
        vicePane.getChildren().clear();
        vicePane.getChildren().add(pane);
        vicePane.setVisible(true);
        allPane.setVisible(false);  
        
        //set image
        String url = String.format("images/ingrediates/%s",datas[2]);
        Image img = new Image(url); //讀出圖片
        ImageView imgview = new ImageView(img);//圖片顯示物件
        imgview.setFitHeight(100); //設定圖片高度，你要自行調整，讓它美觀
        imgview.setPreserveRatio(true); //圖片的寬高比維持
        
        Label origin = new Label(datas[6]);
        origin.setAlignment(Pos.CENTER);
        
        //all datas
        pane.getChildren().add(imgview);                                        //image
        
        pane.getChildren().add(new Label("place of origin:"));
        
        pane.getChildren().add(origin);
        
        pane.getChildren().add(new Label("description:"));
        
        pane.getChildren().add(new Label(datas[5]));                            //describe
        
        pane.getChildren().add(new Label(String.format("you have : %s",datas[4])));//nowStock
        
        
        HBox purchase = new HBox();
        purchase.setAlignment(Pos.CENTER);
        Label purchaseMessage = new Label("puchase : ");
        Label purchaseNum = new Label(String.format("0"));                       //num of wanna buy
        purchase.getChildren().add(purchaseMessage);
        purchase.getChildren().add(purchaseNum);
        pane.getChildren().add(purchase);
        
        HBox buttons = new HBox();                                              //for the button to purchase
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        Button plus = new Button("+1");                                         //buy 1 more
        Button minus = new Button("-1");                                        //buy 1 less
        plus.setOnAction(new EventHandler<ActionEvent>(){ //action : plus 1
            @Override
            public void handle(ActionEvent e){      
                updateIngrediateBuyingNum(purchaseNum, Integer.parseInt(purchaseNum.getText()) + 1);
            }
        });
        minus.setOnAction(new EventHandler<ActionEvent>(){ //action : plus 1
            @Override
            public void handle(ActionEvent e){      
                updateIngrediateBuyingNum(purchaseNum, Integer.parseInt(purchaseNum.getText()) - 1);
            }
        });
        buttons.getChildren().add(plus);
        buttons.getChildren().add(minus);
        pane.getChildren().add(buttons);
        
        
        HBox buttons2 = new HBox();                                              //for the button to purchase
        buttons2.setSpacing(20);
        buttons2.setAlignment(Pos.CENTER);
        Button leave= new Button("leave");    
        Button submit = new Button("purchase");
        leave.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){      
                vicePane.setVisible(false);
                allPane.setVisible(true);
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                int quantity = Integer.parseInt(purchaseNum.getText());
                int cost = Integer.parseInt(datas[3]) * quantity;
                if(Integer.parseInt(gm.GetMoney()) - cost >= 0){
                    ((Ingrediate)gm.iList.dict.get(datas[0])).SetNowStock(Integer.parseInt(purchaseNum.getText()));
                    gm.PlusMoney(cost);
                    updateShopPane(gm.UpdateShopMenu(0), gm.GetDay(), gm.GetMoney());
                    String id = ((Ingrediate)gm.iList.dict.get(datas[0])).getID();
                    
                    
                    //Database storage
                    db.SavePurchaseOrder(quantity, cost, id);
                    db.SaveAccount(Integer.parseInt(gm.GetMoney()), Integer.parseInt(gm.GetDay()));
                    db.SaveIngrediate(id, gm.iList);
                    
                    vicePane.setVisible(false);
                    allPane.setVisible(true);
                    
//                    System.out.println(String.format("Buy ", ));
                } else{
//                    System.out.println("no enough money to buy");
                }
            }
        });
        buttons2.getChildren().add(leave);
        buttons2.getChildren().add(submit);
        
        pane.getChildren().add(buttons2);
    }
    
    //get the ingrediate data
    public void showCommodityData(String[] datas){
        //set a new VBox pane 
        VBox pane = new VBox();
        //pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setPrefSize(250, 250);
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(15);
        
        //vicePane setting
        vicePane.getChildren().clear();
        vicePane.getChildren().add(pane);
        vicePane.setVisible(true);
        allPane.setVisible(false);  
        
        //set image
        String url = String.format("images/commoditys/%s",datas[2]);
        Image img = new Image(url); //讀出圖片
        ImageView imgview = new ImageView(img);//圖片顯示物件
        imgview.setFitHeight(100); //設定圖片高度，你要自行調整，讓它美觀
        imgview.setPreserveRatio(true); //圖片的寬高比維持
        
        //all datas
        pane.getChildren().add(imgview);                                        //image
       
        pane.getChildren().add(new Label(datas[5]));                            //describe
        
        pane.getChildren().add(new Label(String.format("Need: %s", datas[6]))); 
        
        pane.getChildren().add(new Label(String.format("sold: %s", datas[4])));  //sold
        
        pane.getChildren().add(new Label(String.format("Price: %s", datas[3]))); //price
        
        HBox buttons = new HBox();                                              //for the button to purchase
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        Button leave= new Button("leave");                                         //buy 1 more
        Button purchase = new Button("purchase");                                        //buy 1 less
        leave.setOnAction(new EventHandler<ActionEvent>(){ //action : plus 1
            @Override
            public void handle(ActionEvent e){      
                vicePane.setVisible(false);
                allPane.setVisible(true);
            }
        });
        purchase.setOnAction(new EventHandler<ActionEvent>(){ //action : plus 1
            @Override
            public void handle(ActionEvent e){      
                gm.PurchaseCookBook(datas[0]);
                gm.PlusMoney(Integer.parseInt(datas[3]));
                
                //DB save
                db.SaveCookBook(datas[0]);
                
                vicePane.setVisible(false);
                allPane.setVisible(true);
                updateShopPane(gm.UpdateShopMenu(1), gm.GetDay(), gm.GetMoney());
            }
        });
        buttons.getChildren().add(leave);
        buttons.getChildren().add(purchase);
        pane.getChildren().add(buttons);
    }
    
    public void updateIngrediateBuyingNum(Label l, int num){
        if(num <0){
            num = 0;
        }
        l.setText(String.format("%d", num));
    }
    
    public void showCommodity(String[] datas){
        //set a new VBox pane 
        VBox pane = new VBox();
        //pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setPrefSize(250, 250);
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(15);
        
        //vicePane setting
        vicePane.getChildren().clear();
        vicePane.getChildren().add(pane);
        vicePane.setVisible(true);
        allPane.setVisible(false);  
        
        //set image
        String url = String.format("images/commoditys/%s",datas[2]);
        Image img = new Image(url); //讀出圖片
        ImageView imgview = new ImageView(img);//圖片顯示物件
        imgview.setFitHeight(45); //設定圖片高度，你要自行調整，讓它美觀
        imgview.setPreserveRatio(true); //圖片的寬高比維持
        
        //set HBox for title
        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.setSpacing(10);
        Label titleName = new Label(datas[1]);
        titleName.setFont(new Font("Arial", 30));
        title.getChildren().add(titleName);
        title.getChildren().add(imgview);
        
        //set the cookbook
        Label cookBook = new Label();
        cookBook.setPrefSize(100, 100);
        cookBook.setAlignment(Pos.TOP_CENTER);
        String text = "所需食材有:\n";
        for(String ingrediate : datas[6].split("-")){
            text += String.format("%s *1\n",ingrediate);
        }
        cookBook.setText(text);
        
        //all datas
        pane.getChildren().add(title);                                          //title
        
        pane.getChildren().add(new Label(datas[5]));                            //describe
        
        pane.getChildren().add(cookBook);                                       //cookBook
        
        
        HBox buttons = new HBox();                                              //for the button to purchase
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        Button leave= new Button("leave");
        leave.setOnAction(new EventHandler<ActionEvent>(){ //action : plus 1
            @Override
            public void handle(ActionEvent e){      
                vicePane.setVisible(false);
                allPane.setVisible(true);
            }
        }); 
        buttons.getChildren().add(leave);
        
        pane.getChildren().add(buttons);
    }
    
    public void nextDayPane(TextArea news){
        nextDayPane.setVisible(true);
        
        System.out.println("開始nextDay");
        
        //textArea
        String detailText = "";
        int detailForNewCustomer = 0;
        int detailForTotalCustomer = 0;
        int detailForTotalRevenue = 0;
        
        //random customer name
        String[] customer_first_name = {"楊", "政", "林", "吳", "董"};
        String[] customer_second_name = {"士", "宇", "藤", "雅", "据"};
        String[] customer_final_name = {"霆", "君", "幹", "馨", "慧"};
        
        //random customer address
        String[] customer_city = {"彈頭市", "高雄市", "天龍市", "糖尿市", "苗栗國"};
        String[] customer_region = {"麵包長區", "人生好崎區", "123456區"};
        String[] customer_road = {"這裡不可以偷路", "有夠不路", "靠北ㄛ我好像迷路"};
        
        //date of Today
        int customer_num = (int)(Math.random()*10) + 10;
        detailForTotalCustomer = customer_num;
        TextArea detailArea = new TextArea();
        
        
        for(int i = 0; i < customer_num; i++){
            
            String order_id = DBConnection.GetSaleOrderNewestId();
            String[] commodity_id;
            String[] quantity;
            String[] total_income;
            int commodity_type_num = (int)(Math.random() * (gm.mcList.GetLength() - 1))+1;
            String customer_id = "";
            String temp_q = "";
            String temp_ci = "";
            String temp_ti = "";
            
            //new customer
            if((int)(Math.random()*5) == 0){
                detailForNewCustomer += 1;
                //customer datas
                String customer_name = 
                        customer_first_name[(int)(Math.random()*5)] + 
                        customer_second_name[(int)(Math.random()*5)] + 
                        customer_final_name[(int)(Math.random()*5)];
                String customer_address = 
                        customer_city[(int)(Math.random()*5)] + " " +
                        customer_region[(int)(Math.random()*3)] + " " +
                        customer_road[(int)(Math.random()*3)];
               String customer_phone = "09";
               String temp_phone = "";
               for(int j =0; j< 7; j++){
                   temp_phone += String.format("%s", (int)(Math.random()*9)+1);
               }   
               customer_phone += temp_phone;
               customer_id = DBConnection.SaveCustomer(customer_name, customer_address, customer_phone);
            } else{
                customer_id = DBConnection.GetCustomerRandomId();
            }
            
            //buy
            List<String> t = new ArrayList<String>();
            for(int j = 0; j < commodity_type_num; j++){
                int commodity_buy_num = (int)(Math.random()*3) +1;
                String commodity_buy_id = "";
                while(true){
                    commodity_buy_id = gm.mcList.GetList().get((int)(Math.random()*gm.mcList.GetLength())).getID();
                    if(!t.contains(commodity_buy_id)){
                        t.add(commodity_buy_id);
                        break;
                    }
                }
                int total_revenue = commodity_buy_num * gm.mcList.GetCommoidtyByID(commodity_buy_id).getIncome();
                System.out.println(total_revenue);
                detailForTotalRevenue += total_revenue;
                System.out.println(String.format("取得商品的資訊:%s %s %s", commodity_buy_num, commodity_buy_id, total_revenue));
                //String[]
                if(temp_q.equals("")){
                    temp_q += String.format("%s", commodity_buy_num);
                    temp_ci += String.format("%s", commodity_buy_id);
                    temp_ti += String.format("%s", total_revenue);
                } else{
                    temp_q += String.format("*%s", commodity_buy_num);
                    temp_ci += String.format("*%s", commodity_buy_id);
                    temp_ti += String.format("*%s", total_revenue);
                }
            }
            
            System.out.println(String.format("總商品資訊:%s %s %s", temp_q, temp_ci, temp_ti));
            
            //save
            commodity_id = temp_ci.split("\\*");
            quantity = temp_q.split("\\*");
            total_income = temp_ti.split("\\*");
           
            DBConnection.SaveSaleOrder(order_id, customer_id, commodity_id, quantity, total_income);
        }
        
        //Set TextArea
        gm.NextDay();
        gm.PlusMoney(-detailForTotalRevenue);
        detailText = String.format("今天是第%s天\n", gm.GetDay());
        detailText += String.format("增加了%s個新客人\n", detailForNewCustomer);
        detailText += String.format("總共有%s個客人\n", detailForTotalCustomer);
        detailText += String.format("總共賺了%s\n", detailForTotalRevenue);
        
        
        news.setText(detailText);
    }
}