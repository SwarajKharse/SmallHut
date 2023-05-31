package com.example.myproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    @FXML
    private Button getstarted;

    @FXML
    void Onclickstarted(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopOrCustomer.fxml"));
            Parent root = fxmlLoader.load();
            ShopOrCustomerController sc = fxmlLoader.getController();
            Stage stage = (Stage) getstarted.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
