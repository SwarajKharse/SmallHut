package com.example.myproject;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class CustomerLoginController {

    @FXML
    private Button BackBtn;

    @FXML
    private Hyperlink createaccount;

    @FXML
    private Label emailnotfound;

    @FXML
    private TextField emailtext;

    @FXML
    private Button loginbutton;

    @FXML
    private PasswordField passwordtext;

    @FXML
    void Onclickhyper(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CustomerSignUp.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }

    @FXML
    void Onclicklogin(ActionEvent event) throws IOException, SQLException {
            validatelogin();
    }

    void validatelogin() throws IOException, SQLException {

        String email = emailtext.getText();
        String password = passwordtext.getText();

        boolean success = false;
        String name = null;
        String id = null;
        String address = null;

        ValidationProcess vp = new ValidationProcess();
        if(email.isEmpty() || password.isEmpty()){
            emailnotfound.setText("Input field empty!!");
            return;
        }
        if(!vp.isValidEmail(email) && !vp.isValidPassword(password)){
            emailnotfound.setText("Invalid Email and Password");
            return;
        }
        else if(!vp.isValidEmail(email)){
            emailnotfound.setText("Invalid Email");
            return;
        }
        else if(!vp.isValidPassword(password)){
            emailnotfound.setText("Invalid Password.");
            return;
        }
        String hash_pass = vp.hashPassword(password);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifylogin = "Select * From Customer WHERE email = '" + email + "' AND password = '" + hash_pass  + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifylogin);
            while(queryResult.next()) {
                success = true;
                name = queryResult.getString(2);
                id = queryResult.getString(1);
                address = queryResult.getString(5);
            }
            queryResult.close();
            statement.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(success) {
            emailnotfound.setText("Welcome!");
            write_in_file(name, id, address);
            truncate_cart();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopListPage.fxml"));
            Parent root = fxmlLoader.load();
            ShopListController sc = fxmlLoader.getController();
            Stage stage = (Stage) loginbutton.getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
        }
        else {
            emailnotfound.setText("Incorrect email or password!");
        }
    }

    public void write_in_file(String name, String id, String address) throws IOException {
        FileOutputStream fos = new FileOutputStream("cust_acc_name.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(name);
        dos.writeUTF(id);
        dos.writeUTF(address);
        fos.close();
        dos.close();
    }

    @FXML
    void BackBtnProcess(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopOrCustomer.fxml"));
        Parent root = fxmlLoader.load();
        ShopOrCustomerController sc = fxmlLoader.getController();
        Stage stage = (Stage) BackBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void truncate_cart() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        String truncate_query = "TRUNCATE TABLE Cart";
        Statement st = con.createStatement();
        int r = st.executeUpdate(truncate_query);
        if(r > 0){
            System.out.println("Successfully Truncated!!!");
        }
        st.close();
        con.close();
    }
}
