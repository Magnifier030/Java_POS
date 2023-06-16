package models.Manager;

import static com.oracle.jrockit.jfr.ContentType.None;
import com.sun.deploy.util.StringUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Commodity.Commodity;
import models.Commodity.CommodityList;
import models.Ingrediate.*;
import models.DB.*;

public class GameManager {
    
    private DBConnection db;
    private PaneManager pm;
    private String day;
    private String money;
    public IngrediateList iList;
    public CommodityList cList;
    public CommodityList mcList;
    String[][] datas = new String[10][5];
    private Connection conn;
    
    public GameManager(PaneManager pm) {
//        System.out.println((int)(Math.random()*10));
        //db process
        db = new DBConnection();
        
        //datas
        iList = new IngrediateList();
        cList = new CommodityList("y");
        mcList = new CommodityList("n");
        
        //create PaneManager
        this.pm = pm;
        initial();
        pm.updateShopPane(UpdateShopMenu(0), day, money);
        
        
        
        //pm set db
        pm.SetDataBase(db);
        //inform successful creation of GameManager
//        System.out.println("GameManager create successfully!");
    }

    //initial for first progress this game
    public void initial() {

        List<Button> shopMenu = new ArrayList<Button>();
        try {
            //get text of file
            FileReader fr = new FileReader("src/others/save.txt");
            BufferedReader br = new BufferedReader(fr);

//            System.out.println("Successfully get the file of save");

            //get data
            String[] account_datas = DBConnection.GetAccount();
            day = account_datas[0];
            money = account_datas[1];

//            for (String ingrediates : br.readLine().split(";")) {
//                //store the datas each ingrediate
//                String[] datas = ingrediates.split(", ");
////                for(String s : datas){System.out.println(s);}
//
//                //create object ingrediate
//                Ingrediate i = new Ingrediate(datas[0], datas[1], datas[2], Integer.parseInt(datas[3]), Integer.parseInt(datas[4]), datas[5], datas[6]);
//                iList.add(i);
//            }

//            for (String commoditys : br.readLine().split(";")) {
//                //store the datas each ingrediate
//                String[] datas = commoditys.split(", ");
////                for(String s : datas){System.out.println(s);}
//
//                //create object ingrediate
//                Commodity c = new Commodity(datas[0], datas[1], datas[2], Integer.parseInt(datas[3]), Integer.parseInt(datas[4]), datas[5], datas[6]);
//                cList.add(c);
//            }
                
//            for (String commoditys : br.readLine().split(";")) {
//                //store the datas each ingrediate
//                String[] datas = commoditys.split(", ");
////                for(String s : datas){System.out.println(s);}
//
//                //create object ingrediate
//                Commodity c = new Commodity(datas[0], datas[1], datas[2], Integer.parseInt(datas[3]), Integer.parseInt(datas[4]), datas[5], datas[6]);
//                mcList.add(c);
//            }
            
            br.close();
            fr.close();
            
            FileReader fr1 = new FileReader("src/others/records_sys.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            
            int i = 0;
            while(br1.ready()){
                String[] text = br1.readLine().split(",");
                datas[i][0] = String.format("0000%d", i);
                datas[i][1] = text[0];//order
                datas[i][2] = text[1];//purchaseNum
                datas[i][3] = text[2];//totalCost
                datas[i][4] = text[3];//name
//                System.out.println(i);
                i += 1;
            }
//            System.out.println("initial completed.");
            br1.close();
            fr1.close();

        } catch (Exception e) {
//            System.out.printf("Saving File has wrong: %s\n", e);
        }
    }

    public String getRecordData() {
        String last = "";

        for (String[] data : datas) {
            if (data[4].equals("0")) {
                continue;
            }
            last += String.format("==========================================\n");
            last += String.format("ID:%s\n", data[0]);
            last += String.format("name:%s\n", data[4]);
            last += String.format("orderNum:%s\n", data[1]);
            last += String.format("purchaseNum:%s\n", data[2]);
            last += String.format("totalCost:%s\n", data[3]);
        }

        return last;
    }

    public String getALLRecord() {
        String last = "";

        try {
            //get text of file
            FileReader fr = new FileReader("src/others/records.txt");
            BufferedReader br = new BufferedReader(fr);

            while (br.ready()) {
                last += String.format("%s\n", br.readLine());
            }
        } catch (Exception e) {

        }

        return last;
    }

    public void record(String new_record, String[] data) {
        try {
            //get text of file
            FileReader fr = new FileReader("src/others/records.txt");
            BufferedReader br = new BufferedReader(fr);

            String last = "";
            while (br.ready()) {
                last += String.format("%s\n", br.readLine());
            }

            for (int i = 0; i < 10; i++) {
//                System.out.println(datas[i][0] + " .vs. " + data[0]);
                if (datas[i][0].equals(data[0])) {
                    datas[i][1] = String.format("%s", Integer.parseInt(datas[i][1]) + 1);//order
                    datas[i][2] = String.format("%s", Integer.parseInt(datas[i][2]) + Integer.parseInt(data[1]));//num
                    datas[i][3] = String.format("%s", Integer.parseInt(datas[i][3]) + Integer.parseInt(data[2]));//cost
                    datas[i][4] = data[3];//name
                    break;
                }
            }

            last += new_record;

            fr.close();
            br.close();
            FileWriter fw = new FileWriter("src/others/records.txt");
            fw.write(last);
            fw.flush();
            fw.close();
        } catch (Exception e) {
//            System.out.printf("wrong: %s\n", e);
        }
    }

    //get the all data of file of save
    public List<Button> UpdateShopMenu(int mode) {
        List<Button> shopMenu = new ArrayList<Button>();

        String folder = "";
        List list;

        if (mode == 0) {
            folder = "ingrediates";
            list = iList.GetList();
        } else if (mode == 1) {
            folder = "commoditys";
            list = cList.GetList();
        } else {
            folder = "commoditys";
            list = mcList.GetList();
        }

        try {
            for (Object i : list) {
                String[] datas;
                if (mode == 0) {
                    datas = ((Ingrediate) i).getAllDatas();
                } else if (mode == 1) {
                    datas = ((Commodity) i).getAllDatas();
                } else {
                    datas = ((Commodity) i).getAllDatas();
                }

                String url = String.format("images/%s/%s", folder, datas[2]);
                Image img = new Image(url); //讀出圖片
                ImageView imgview = new ImageView(img);//圖片顯示物件
                imgview.setFitHeight(50); //設定圖片高度，你要自行調整，讓它美觀
                imgview.setPreserveRatio(true); //圖片的寬高比維持

                Button b = new Button();
                b.setGraphic(imgview);
                b.setOnAction(new EventHandler<ActionEvent>() { //add switch the Pane
                    @Override
                    public void handle(ActionEvent e) {
                        if (mode == 0) {
                            pm.showIngrediateData(iList.GetIngrediateByID(datas[0]).getAllDatas());
                        } else if (mode == 1) {
                            pm.showCommodityData(cList.GetCommoidtyByID(datas[0]).getAllDatas());
                        } else {
                            pm.showCommodity(mcList.GetCommoidtyByID(datas[0]).getAllDatas());
                        }
                    }
                });

                //add button into list
                shopMenu.add(b);
//                System.out.println("get a new Button");
            }
        } catch (Exception e) {

        }

        return shopMenu;
    }

    public void Start() {
//        System.out.println("Game starts");
//       pm.shopPane(menu, day);
    }

    public void PurchaseCookBook(String id) {
        mcList.add(cList.delete(id));
        db.SaveCookBook(id);
    }
    
    //only for testing
    public void test(Stage primaryStage) {
        Pane p = new Pane();
        p.getChildren().add(new Button("hi"));
        Scene s = new Scene(p, 1000, 1000);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    //Timer
    public Boolean timer(int sleepSecond) {

        try {
            //set the date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);

            //get the current time
            Date lastTime = sdf.parse(sdf.format(new Date()));

            //change the sleepSecond from second into millisecond
            sleepSecond *= 1000;

            //confirm time arrives
            while (true) {
                Date nowDate = sdf.parse(sdf.format(new Date()));
                if (nowDate.getTime() - lastTime.getTime() >= sleepSecond) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.print(e);
        }

        return true;
    }

    public void PlusDay() {
        day = String.format("%d", Integer.parseInt(day) + 1);
        db.SaveAccount(Integer.parseInt(money), Integer.parseInt(day));
    }

    //get day
    public String GetDay() {
        return day;
    }

    //get money
    public String GetMoney() {
        return money;
    }

    public void PlusMoney(int num) {
        this.money = String.format("%d", Integer.parseInt(this.money) - num);
        DBConnection.SaveAccount(Integer.parseInt(money), Integer.parseInt(day));
    }
    
    public void NextDay(){
        day = String.format("%s", Integer.parseInt(day) + 1);
        DBConnection.SaveAccount(Integer.parseInt(money), Integer.parseInt(day));
    }
}
