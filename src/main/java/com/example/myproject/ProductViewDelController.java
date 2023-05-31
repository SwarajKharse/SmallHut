package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProductViewDelController implements Initializable {

    @FXML
    private Label best_before;

    @FXML
    private Button delete_button;

    @FXML
    private HBox product;

    @FXML
    private Label product_mfg_date;

    @FXML
    private Label product_name;

    @FXML
    private Label product_price;

    @FXML
    private Label product_quantity;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(ProductInventory product){
        product_name.setText(product.getProduct_name());
        product_price.setText(product.getPrice());
        product_quantity.setText(product.getQuantity());
        product_mfg_date.setText(product.getDate());
        best_before.setText("Best Before " +product.getBest_bef() + " Days");
    }

    @FXML
    void delete_product(ActionEvent event) throws FileNotFoundException, SQLException {
        String name = product_name.getText();

        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();
        String query;

        try{
            query = "SELECT * FROM Product WHERE name = '" + name + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            String prod_id = "";
            while(rs.next()){
                prod_id = rs.getString(1);
            }

            query = "DELETE FROM Inventory WHERE prod_id = '" + prod_id + "'";
            int row = st.executeUpdate(query);
            if(row > 0){
                alert_fun(name);
            }
            st.close();
            con.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        shopkeeperController sc = new shopkeeperController();
        sc.del_prod();
    }

    void hide_del_btn(){
        if (product.getChildren().contains(delete_button)) {
            product.getChildren().remove(delete_button);
        }
    }

    void unhide_del_btn(){
        if (!product.getChildren().contains(delete_button)) {
            product.getChildren().add(delete_button);
        }
    }

    public void alert_fun(String name){
        Image image = new Image(getClass().getResourceAsStream("/img/icons8-complete.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60); // Set the desired width
        imageView.setFitHeight(60); // Set the desired height
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(imageView);
        alert.setHeaderText(null);
        alert.setContentText(name + " is removed from your Inventory!");
        alert.showAndWait();
    }

}
