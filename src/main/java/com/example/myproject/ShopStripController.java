package com.example.myproject;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ShopStripController implements Initializable {

    public String id;

    @FXML
    private Label contacttext;

    @FXML
    private Button nextshoppage;

    @FXML
    private Label shopnametext;

    //
    @FXML
    void Onpressshoppage(ActionEvent event) throws IOException {
        write_in_file(id);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerEntryPage.fxml"));
        Parent root = fxmlLoader.load();
        CustomerEntryController sc = fxmlLoader.getController();
        Stage stage = (Stage) nextshoppage.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void setData(Shop sh) {
        shopnametext.setText(sh.getName());
        contacttext.setText(sh.getContact());
        id = sh.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    // From this function we write the ShopId in the file so that we can use this ShopId to get all the Products from the Inventory
    public void write_in_file(String shop_id) throws IOException {
        FileOutputStream fos = new FileOutputStream("acc_id.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(shop_id);
        dos.writeUTF(shopnametext.getText());
        return;
    }

}