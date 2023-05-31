package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerProductListController {

    public int avail_quantity;
    public String prod_id;

    @FXML
    private Button AddCartBtn;

    @FXML
    private Label Prod_name;

    @FXML
    private Label prod_mfg_date;

    @FXML
    private Label prod_price;

    @FXML
    private TextField prod_quantity;

    @FXML
    void AddToCartProcess(ActionEvent event) throws SQLException {
        String name = Prod_name.getText();
        int quantity = Integer.parseInt(prod_quantity.getText());
        float price = Float.parseFloat((prod_price.getText()));
        float tot = quantity * price;

        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();

        String check_query = "SELECT * FROM Cart WHERE name = '" + name + "'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(check_query);
        while(rs.next()){
            alert_fun(0, name, 1);
            prod_quantity.clear();
            return;
        }

        if(quantity > avail_quantity){
            alert_fun(avail_quantity, name, 0);
            return;
        }

        String query;
        try{
            query = "INSERT INTO Cart VALUES ('" + name + "','" + price + "','" + quantity + "', '" + tot + "', '" +prod_id + "')";
            st = con.createStatement();
            int r = st.executeUpdate(query);
            if(r > 0){
                alert_fun(0, name, 0);
                prod_quantity.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        st.close();
        con.close();
    }

    void setData(CustomerProdList cpl){
        Prod_name.setText(cpl.getName());
        prod_price.setText(String.valueOf(cpl.getPrice()));
        prod_mfg_date.setText(cpl.getDate());
        avail_quantity = cpl.getTotal_quantity();
        prod_id = cpl.getProd_id();
    }

    public void quantity_limit(javafx.scene.input.KeyEvent keyEvent) {
        TextField textField = (TextField) keyEvent.getSource();
        String input = textField.getText();
        if (!input.matches("\\d*")) {
            textField.setText(input.replaceAll("\\D", ""));
        }
    }

    public void alert_fun(int r, String name, int t){
        Image image = new Image(getClass().getResourceAsStream("/img/icons8-complete.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60); // Set the desired width
        imageView.setFitHeight(60); // Set the desired height
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(imageView);
        alert.setHeaderText(null);
        if(t == 1){
            alert.setContentText(name + " is ALREADY in the Cart.");
        }
        else if(r == 0){
            alert.setContentText(name + " is Added to Cart Successfully!");
        }
        else{
            alert.setContentText("We have only " + r + " number of quantity of " + name + " in our  Inventory! Please Insert values within the range. ");
        }
        alert.showAndWait();
    }

}
