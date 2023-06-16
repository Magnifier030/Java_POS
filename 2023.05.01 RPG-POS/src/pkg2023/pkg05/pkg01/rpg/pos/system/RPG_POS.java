/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2023.pkg05.pkg01.rpg.pos.system;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.util.converter.IntegerStringConverter;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.scene.text.Font;
import models.Manager.GameManager;
import models.Manager.PaneManager;

public class RPG_POS extends Application {
    @Override
    public void start(Stage primaryStage) {     
        //root
        Pane root = new Pane();
        root.getStylesheets().add("css/bootstrap3.css");
        
        //create PaneManager to deal with all pane change
        PaneManager pm = new PaneManager(root);
        
        //create GameManager to deal with entire game
        GameManager gm = new GameManager(pm);
        
        //set the gm into pm
        pm.SetGameManager(gm);
        
          
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("To be a seller");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Game starts
        gm.Start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
