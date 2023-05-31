package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ShopOrCustomerController {

    @FXML
    private Button BackBtn;

    @FXML
    private Button Cust_Btn;

    @FXML
    private Button ShopBtn;

    @FXML
    void Cust_Login_Process(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerLogin.fxml"));
            Parent root = fxmlLoader.load();
            CustomerLoginController hc = fxmlLoader.getController();

            Stage stage = (Stage) ShopBtn.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ShopLoginProcess(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn_SignUp.fxml"));
            Parent root = fxmlLoader.load();
            LoginSignupController hc = fxmlLoader.getController();
            Stage stage = (Stage) ShopBtn.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void BackBtnProcess(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = fxmlLoader.load();
        HomePageController hc = fxmlLoader.getController();
        Stage stage = (Stage) BackBtn.getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(root));
    }

}
