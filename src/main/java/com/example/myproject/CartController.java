package com.example.myproject;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


//import javax.xml.crypto.Data;

public class CartController implements Initializable {

    public String prod_id;

    @FXML
    private Button BackBtn;

    @FXML
    private VBox cartinfolayout;

    @FXML
    private Label totalcosttext;

    @FXML
    private Button makepayment;

    @FXML
    void MakePaymentProcess(ActionEvent event) throws IOException, SQLException {

        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        String name = null;
        int quantity = 0;
        float tot_price = 0;

        String cart_query = "SELECT * FROM Cart";

        String inv_query = "UPDATE Inventory SET quantity = quantity - ? WHERE prod_id = ?";
        PreparedStatement invUpdateStmt = con.prepareStatement(inv_query);

        int r ;
        Statement st = con.createStatement();
        Statement st1 = con.createStatement();
        ResultSet rs = st.executeQuery(cart_query);

        while(rs.next()){

            name = rs.getString(1);
            quantity = rs.getInt(3);
            tot_price = rs.getFloat(4);
            prod_id = rs.getString(5);

            invUpdateStmt.setInt(1, quantity);
            invUpdateStmt.setString(2, prod_id);

            int rowsAffected = invUpdateStmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Updated Successfully!!");
            }
        }

        // Update In table completed
        st.close();
        st1.close();
        rs.close();
        con.close();

        trasaction_table(tot_price);
        truncate_cart();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Order.fxml"));
        Parent root = fxmlLoader.load();
        OrderController cc = fxmlLoader.getController();
        Stage stage = (Stage) makepayment.getScene().getWindow();
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

    public void trasaction_table(float tot_price) throws IOException, SQLException {

        ValidationProcess vp = new ValidationProcess();
        String trans_id = vp.generateTransactionId();

        FileInputStream fis = new FileInputStream("acc_id.txt");
        DataInputStream dis = new DataInputStream(fis);
        String Shop_id = dis.readUTF();
        String Shop_name = dis.readUTF();

        FileInputStream fis1 = new FileInputStream("cust_acc_name.txt");
        DataInputStream dis1 = new DataInputStream(fis1);
        String cust_name = dis1.readUTF();
        String cust_id = dis1.readUTF();

        write_in_file(trans_id);

        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        String query = "INSERT INTO BUY VALUES ('" + Shop_id + "', '" + cust_id + "', '" + tot_price + "', TO_DATE(SYSDATE, 'DD-MM-YYYY'), '"
                + trans_id + "', '" + Shop_name + "', '" + cust_name + "')";

        Statement st = con.createStatement();
        int r = st.executeUpdate(query);
        if(r > 0){
            System.out.println("Inserted successfully!!!");
        }
        st.close();
        con.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Cart_Fill();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Cart_Fill() throws IOException {
        List<Cart> sl = new ArrayList<>(carts());
        System.out.println(sl.size());

        for(int i=0; i < sl.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CartStrip.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CartStripController cic = fxmlLoader.getController();
                cic.setData(sl.get(i));
                cartinfolayout.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Cart> carts() throws IOException{
        String name = "";
        float price = 0;
        int quantity = 0;
        String prod_id;
        String total_price = null;

        List<Cart> ls = new ArrayList<>();
        Cart sh = new Cart();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        
        String sql1 = "Select name, price, quantity, prod_id From Cart";
        String sql2 = "Select SUM(totprice) From Cart";
        
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sql1);
            while(queryResult.next()) {
                name = queryResult.getString(1);
                price = queryResult.getFloat(2);
                quantity = queryResult.getInt(3);
                sh.setName(name);
                sh.setPrice(price);
                sh.setQuantity(quantity);
                sh.setProd_id(queryResult.getString(4));
                ls.add(sh);
                sh = new Cart();
            }

            ResultSet qr = statement.executeQuery(sql2);
            while(qr.next()) {
                System.out.println(qr.getString(1));
                total_price = qr.getString(1);
            }
            qr.close();
            queryResult.close();
            statement.close();
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
        if(total_price != null){
            totalcosttext.setText(total_price);
        }
        return ls;
    }

    public void BackBtnProcess(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerEntryPage.fxml"));
        Parent root = fxmlLoader.load();
        CustomerEntryController cc = fxmlLoader.getController();
        Stage stage = (Stage) makepayment.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public int write_in_file(String trans) throws IOException {
        FileOutputStream fos = new FileOutputStream("trans-id.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(trans);
        return 1;
    }

}