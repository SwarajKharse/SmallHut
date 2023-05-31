package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
//import jdk.vm.ci.meta.Local;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProductDisplayController implements Initializable {

    public void setShop_id(String shop_id) {
        Shop_id = shop_id;
    }

    public String Shop_id;
    public int best_bef;

    @FXML
    private Button add_product_btn;

    @FXML
    private DatePicker mfg_date;

    @FXML
    private HBox product;

    @FXML
    private Label product_name;

    @FXML
    private Label product_price;

    @FXML
    private TextField product_quantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("shop_acc.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        DataInputStream dis = new DataInputStream(fis);
        String str = null;
        try {
            str = dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Shop_id = dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(Product product){
        product_name.setText(product.getProduct_name());
        product_price.setText(product.getPrice());
    }

    @FXML
    void add_in_inventory(ActionEvent event) {
        String name = product_name.getText();
        String quan = product_quantity.getText();
        String date = String.valueOf(mfg_date.getValue());
        System.out.println(name + " " + quan + " " + date);

        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();
        String query, prod_id;

        try{
            query = "SELECT * FROM Product WHERE name = '" + name + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            prod_id = "";
            while(rs.next()){
                prod_id = rs.getString(1);
                best_bef = rs.getInt(5);
            }
            set_day_limit();

            query = "INSERT INTO Inventory VALUES ('" + Shop_id + "', '" + prod_id +"', '" + quan +"', DATE'" + date + "')";
            int row = st.executeUpdate(query);

            if(row > 0){
                alert_fun(quan, name);
                clear_fields();
            }

            st.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clear_fields(){
        product_quantity.clear();
        mfg_date.setValue(null);
    }

    public void alert_fun(String quan, String name){
        Image image = new Image(getClass().getResourceAsStream("/img/icons8-complete.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60); // Set the desired width
        imageView.setFitHeight(60); // Set the desired height
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(imageView);
        alert.setHeaderText(null);
        alert.setContentText(quan + " quantity of " + name + " is added in the Inventory!");
        alert.showAndWait();
    }

    public void quantity_limit(javafx.scene.input.KeyEvent keyEvent) {
        TextField textField = (TextField) keyEvent.getSource();
        String input = textField.getText();

        if (!input.matches("\\d*")) {
            textField.setText(input.replaceAll("\\D", ""));
        }
    }

    public void set_day_limit(){
//        // Calculate the validity date based on today's date and best before validity
//        LocalDate validityDate = LocalDate.now().minusDays((int)(best_bef * 0.25));
//
//        // Set the lower and upper limits for the DatePicker
//        mfg_date.setDayCellFactory(picker -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                setDisable(date.isBefore(validityDate) || date.isAfter(LocalDate.now()));
//            }
//        });

        // Set the lower and upper limits for the DatePicker
//        LocalDate lowerLimit = LocalDate.of(2023, 1, 1);
//        LocalDate lowerLimit = LocalDate.now().minusDays((int)(best_bef * 0.25));
////        LocalDate upperLimit = LocalDate.of(2023, 12, 31);
//        LocalDate upperLimit = LocalDate.now();
//
//        // Add a ChangeListener to the DatePicker's valueProperty
//        mfg_date.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                // Check if the selected date is outside the limits
//                if (newValue.isBefore(lowerLimit) || newValue.isAfter(upperLimit)) {
//                    // Update the value to be within the limits
//                    mfg_date.setValue(oldValue);
//                }
//            }
//        });
    }

}

