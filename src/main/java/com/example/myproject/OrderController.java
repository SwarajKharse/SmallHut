package com.example.myproject;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


public class OrderController implements Initializable {

    @FXML
    private Label custname;

    @FXML
    private Button logoutbutton;

    @FXML
    private Label shopname;

    @FXML
    private Label totcost;

    @FXML
    private Label transdate;

    @FXML
    private Label transid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FileInputStream fis = null;
        String trans_id;
        try {
            fis = new FileInputStream("trans-id.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        DataInputStream dis = new DataInputStream(fis);

        try {
            trans_id = dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        String query = "SELECT * FROM Buy WHERE transactionid = '" + trans_id + "'" ;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                custname.setText(rs.getString(7));
                shopname.setText(rs.getString(6));
                totcost.setText(rs.getString(3));
                transdate.setText(rs.getString(4));
                transid.setText(rs.getString(5));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Onclicklogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerLogin.fxml"));
        Parent root = fxmlLoader.load();
        CustomerLoginController cc = fxmlLoader.getController();
        Stage stage = (Stage) logoutbutton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

}
