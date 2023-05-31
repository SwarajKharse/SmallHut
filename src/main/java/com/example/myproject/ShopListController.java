package com.example.myproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShopListController implements Initializable {

    String name = null;
    String id = null;
    String address = null;

    @FXML
    private Button BackBtn;

    @FXML
    private VBox shopinfolayout;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){

        try {
            read_from_file();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Shop> sl = new ArrayList<>(shops());
        for(int i=0; i<sl.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ShopStrip.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                ShopStripController cic = fxmlLoader.getController();
                cic.setData(sl.get(i));
                shopinfolayout.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read_from_file() throws IOException {
        FileInputStream fis = new FileInputStream("cust_acc_name.txt");
        DataInputStream dis = new DataInputStream(fis);

        name = dis.readUTF();
        id = dis.readUTF();
        address = dis.readUTF();

    }

    private List<Shop> shops() {
        String name = "";
        String contact = "";
        String id = "";

        List<Shop> ls = new ArrayList<>();
        Shop sh = new Shop();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        
        String sql1 = "Select * From Shopkeeper WHERE address = '" + address +"'";
        
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sql1);
            while(queryResult.next()) {
                id = queryResult.getString(1);
                name = queryResult.getString(2);
                contact = queryResult.getString(6);
                sh.setName(name);
                sh.setContact(contact);
                sh.setId(id);
                ls.add(sh);
                sh = new Shop();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }

    public void BackBtnProcess(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerLogin.fxml"));
        Parent root = fxmlLoader.load();
        CustomerLoginController hc = fxmlLoader.getController();
        Stage stage = (Stage) BackBtn.getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
    }

    // width= 962 and height 653

}