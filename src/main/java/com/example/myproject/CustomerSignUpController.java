package com.example.myproject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerSignUpController {

    @FXML
    private Button BackBtn;

    @FXML
    private TextField addresstext;

    @FXML
    private Hyperlink alreadymember;

    @FXML
    private PasswordField confirmpasswordtext;

    @FXML
    private TextField contacttext;

    @FXML
    private TextField emailtext;

    @FXML
    private TextField nametext;

    @FXML
    private Label passwordnotmatching;

    @FXML
    private PasswordField passwordtext;

    @FXML
    private Button registerbutton;

    @FXML
    void Onclickregister(ActionEvent event) {
        String name = nametext.getText().toString();
        String email = emailtext.getText().toString();
        String address = addresstext.getText().toString();
        String contact = contacttext.getText().toString();
        String password = passwordtext.getText().toString();
        String confirm_pass = confirmpasswordtext.getText().toString();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || contact.isEmpty() || password.isEmpty() || confirm_pass.isEmpty()) {
            passwordnotmatching.setText("Please fill all the fields.");
            return;
        }
        ValidationProcess vp = new ValidationProcess();
        if(!vp.isValidEmail(email) && !vp.isValidPassword(password)){
            passwordnotmatching.setText("Invalid Email and Password");
            return;
        }
        else if(!vp.isValidEmail(email)){
            passwordnotmatching.setText("Invalid Email");
            return;
        }
        else if(!vp.isValidPassword(password)){
            passwordnotmatching.setText("Invalid Password.");
            return;
        }
        if(password != confirm_pass){
            passwordnotmatching.setText("Password not matching.");
            return;
        }

        String hash_pass = vp.hashPassword(password);
        String st = "";
        int a = 0;
        String verifylogin = "SELECT COUNT(*) FROM Customer";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifylogin);
            if (queryResult.next()) {
                a = queryResult.getInt(1) + 1;
                st = "cust_" + a;
                String sql1 = "INSERT INTO Customer VALUES ('" + st + "', '" + name + "', '" + email + "', '" + hash_pass + "', '" + address + "', '" + contact + "')";
                int rowsAffected = statement.executeUpdate(sql1);
                if (rowsAffected > 0) {
                    connectDB.commit(); // Commit the changes
                    passwordnotmatching.setText("Success!");
                } else {
                    passwordnotmatching.setText("Not updated!");
                }
            }
            queryResult.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void Onhyperlink(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CustomerLogin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.show();
    }


    @FXML
    void BackBtnProcess(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopOrCustomer.fxml"));
        Parent root = fxmlLoader.load();
        ShopOrCustomerController sc = fxmlLoader.getController();
        Stage stage = (Stage) BackBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
}
