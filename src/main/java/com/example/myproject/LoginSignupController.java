package com.example.myproject;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ResourceBundle;
import java.io.*;

import javafx.fxml.Initializable;
import java.net.URL;

import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class LoginSignupController implements Initializable {

    public String shopName;
    public String shop_id;

    // Create a instance of the class so that we can pass on the number of arguments we want to pass
    public static LoginSignupController instance;

    public LoginSignupController(){
        instance = this;
    }

    public static LoginSignupController getInstance(){
        return instance;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShopName() {
        return this.shopName;
    }

    public String getShopId() {
        return this.shop_id;
    }

    // Till here


    // ******************************************************************************************

    @FXML
    private Label LoginLabel;

    @FXML
    private Label SignUpLabel;

    @FXML
    private ImageView Back_img;

    @FXML
    private VBox Login_Box;

    @FXML
    private VBox Login_to_SignIn_Box;

    @FXML
    private VBox SignUp_Box;

    @FXML
    private Label a1Label;

    @FXML
    private Button a1_LogInConvert;

    @FXML
    private Button a1_SignUp;

    @FXML
    private TextField a1_addtb;

    @FXML
    private PasswordField a1_confpass;

    @FXML
    private TextField a1_contact;

    @FXML
    private TextField a1_emailtb;

    @FXML
    private Label a1_label;

    @FXML
    private TextField a1_nametb;

    @FXML
    private PasswordField a1_pass;

    @FXML
    private Button a2_SignUp;

    @FXML
    private TextField a2_email;

    @FXML
    private Label a2_label;

    @FXML
    private Button a2_logIn;

    @FXML
    private PasswordField a2_pass;

    @FXML
    private Label a2_welcome;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private AnchorPane anchor_pane1;

    @FXML
    private VBox signIn_to_Login_BOX;

    // ***************************************************************************************

    @Override
    public void initialize(URL url, ResourceBundle rb){
        Login_Box.setVisible(false);
        SignUp_Box.setVisible(true);
        signIn_to_Login_BOX.setVisible(true);
        Login_to_SignIn_Box.setVisible(false);
    }

    @FXML
    void convert_to_logIn_page(ActionEvent event) {
        clear_textFields();
        Login_Box.setVisible(true);
        SignUp_Box.setVisible(false);
        signIn_to_Login_BOX.setVisible(false);
        Login_to_SignIn_Box.setVisible(true);

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(anchor_pane);
        slide.setToX(465);
        slide.play();
        anchor_pane1.setTranslateX(-345);
    }

    @FXML
    void convert_to_signUp_page(ActionEvent event) {
        clear_textFields();
        Login_Box.setVisible(false);
        SignUp_Box.setVisible(true);
        signIn_to_Login_BOX.setVisible(true);
        Login_to_SignIn_Box.setVisible(false);

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(anchor_pane);
        slide.setFromX(anchor_pane.getWidth());
        slide.setToX(0);
        anchor_pane1.setTranslateX(0);
        slide.play();
    }

    @FXML
    void create_account_process(ActionEvent event) throws SQLException {
        String name = a1_nametb.getText().toString();
        String email = a1_emailtb.getText().toString();
        String address = a1_addtb.getText().toString();
        String contact = a1_contact.getText().toString();
        String pass = a1_pass.getText().toString();
        String confpass = a1_confpass.getText().toString();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || contact.isEmpty() || pass.isEmpty() || confpass.isEmpty()) {
            SignUpLabel.setText("Please fill all the fields.");
            return;
        }

        ValidationProcess vp = new ValidationProcess();
        if(!vp.isValidEmail(email) && !vp.isValidPassword(pass)){
            LoginLabel.setText("Invalid Email and Password");
            return;
        }
        else if(!vp.isValidEmail(email)){
            SignUpLabel.setText("Invalid Email");
            return;
        }
        else if(!vp.isValidPassword(pass)){
            SignUpLabel.setText("Invalid Password.");
            return;
        }
        else if(!pass.equals(confpass)){
            SignUpLabel.setText("Password not matching.");
            return;
        }
        String hash_pass = vp.hashPassword(pass);

        DatabaseConnection dbc = new DatabaseConnection();
        Connection connectDB = dbc.getConnection();

        String cnt = "SELECT COUNT(*) FROM Shopkeeper";
        int count = 0;
        try {

            Statement st = connectDB.createStatement();
            ResultSet res = st.executeQuery(cnt);
            while(res.next()){
                count = res.getInt(1);
            }
            String create = "INSERT INTO Shopkeeper VALUES ('shop_" + (count+1) + "','" + name + "', '" + email + "', '" + hash_pass + "', '"+ address + "', '" + contact + "')";
            int rs = -1;
            if(pass.equals(confpass)) {
                rs = st.executeUpdate(create);
            }
            if(rs > 0){
                clear_textFields();
                SignUpLabel.setText("Signup Successfull.");
            }else{
                SignUpLabel.setText("Not Created Account SuccessFully");
            }
            st.close();

        }catch(Exception e) {
            System.out.println("Wasnt able to login");
        }
        connectDB.close();
    }

    @FXML
    void login_account(ActionEvent event) throws SQLException {
        String email2 = a2_email.getText().toString();
        String pass2 = a2_pass.getText().toString();

        if(email2.isBlank() || pass2.isBlank()){
            LoginLabel.setText("Please fill all the fields.");
            return;
        }

        ValidationProcess vp = new ValidationProcess();
        if(!vp.isValidEmail(email2) && !vp.isValidPassword(pass2)){
            LoginLabel.setText("Invalid Email and Password");
            return;
        }
        else if(!vp.isValidEmail(email2)){
            LoginLabel.setText("Invalid Email");
            return;
        }
        else if(!vp.isValidPassword(pass2)){
            LoginLabel.setText("Invalid Password");
            return;
        }

        System.out.println(email2 + " " + pass2);
        String hash_pass = vp.hashPassword(pass2);

        DatabaseConnection dbc = new DatabaseConnection();
        Connection connectDB = dbc.getConnection();

        boolean success = false;
        try {
            String verify_login = "SELECT * FROM Shopkeeper WHERE email = '" + email2 + "' AND password = '" + hash_pass + "' ";
            Statement st = connectDB.createStatement();
            ResultSet rs = st.executeQuery(verify_login);

            while (rs.next()) {
                  System.out.println(rs.getString(1) + rs.getString(2));
                  shopName = rs.getString(2);
                  shop_id = rs.getString(1);
                  int n = write_in_file(shopName, shop_id);
                  if(n == 1){
                      success = true;
                  }
                  clear_textFields();
            }
            rs.close();
            st.close();

        } catch (Exception e){
            System.out.println("Wasnt able to login");
        }
        connectDB.close();

        if(success){
            try{
                System.out.println(shop_id + shopName);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shopkeeper_entry_pg.fxml"));
                Parent root = fxmlLoader.load();
                shopkeeperController sc = fxmlLoader.getController();
                Stage stage = (Stage) a2_logIn.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{

            System.out.println("NO SuccessFully Login!! Try Again");
        }
    }

    public int write_in_file(String name, String id) throws IOException {
        FileOutputStream fos = new FileOutputStream("shop_acc.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(name);
        dos.writeUTF(id);
        return 1;
    }

    public void BackBtnProcess(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopOrCustomer.fxml"));
        Parent root = fxmlLoader.load();
        ShopOrCustomerController sc = fxmlLoader.getController();
        Stage stage = (Stage) Back_img.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void clear_textFields(){
        a2_email.clear();
        a2_pass.clear();
        a1_nametb.clear();
        a1_emailtb.clear();
        a1_addtb.clear();
        a1_contact.clear();
        a1_pass.clear();
        a1_confpass.clear();
    }
}

