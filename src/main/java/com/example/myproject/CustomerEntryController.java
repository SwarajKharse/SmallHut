package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerEntryController implements Initializable {

    public String text_pressed = null;
    public String selectedCategory = null;
    public RadioButton selectedRadioButton = null;
    public String func = "view";
    public String Shop_id;
    public String cust_name;

    public String getShop_id() {
        return Shop_id;
    }

    public void setShop_id(String shop_id) {
        Shop_id = shop_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String shop_name) {
        cust_name = shop_name;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            read_cust_name();
            read_shop_id();
            start();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void read_cust_name() throws IOException {
        FileInputStream fis = new FileInputStream("cust_acc_name.txt");
        DataInputStream dis = new DataInputStream(fis);
        cust_name = dis.readUTF();
        System.out.println( "in CustomerEntryPage " +  cust_name);
    }

    public void read_shop_id() throws IOException {
        FileInputStream fis1 = new FileInputStream("acc_id.txt");
        DataInputStream dis1 = new DataInputStream(fis1);
        Shop_id =  dis1.readUTF();
        System.out.println(  "in CustomerEntryPage " + Shop_id);
    }

    void start() throws Exception {
        view_prod();
        my_account.setText(getCust_name());
        rad_fill();
    }

    @FXML
    private Button ViewCartBtn;

    @FXML
    private VBox category_box;

    @FXML
    private Button logout_btn;

    @FXML
    private Label my_account;

    @FXML
    private AnchorPane nav_top;

    @FXML
    private VBox prod_box;

    @FXML
    private ScrollPane scroller;

    @FXML
    private TextField search_bar;

    // For getting all the categories from the Table
    private void rad_fill() {
        ToggleGroup tg = new ToggleGroup();

        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();
        String query = "SELECT DISTINCT(category) FROM Product";

        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            create_radio(tg, "All");
            while(rs.next()){
                create_radio(tg, rs.getString(1));
            }
            rs.close();
            st.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void create_radio(ToggleGroup tg, String name){
        RadioButton r = new RadioButton(String.valueOf(name));
        r.setToggleGroup(tg);
        r.getStyleClass().addAll("@shopkeeper.css","radio-button");
        category_box.getChildren().add(r);

        r.setOnAction(e -> {
            selectedRadioButton = (RadioButton) e.getSource();
            selectedCategory = selectedRadioButton.getText();
            try {
                view_prod();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    void ViewCartProcess(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Cart.fxml"));
        Parent root = fxmlLoader.load();
        CartController cc = fxmlLoader.getController();
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void logout_process(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerLogin.fxml"));
        Parent root = fxmlLoader.load();
        CustomerLoginController sc = fxmlLoader.getController();
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void search_dynamic(KeyEvent event) throws SQLException {
        text_pressed = "%" + search_bar.getText() + "%";
        view_prod();
    }

    private void view_prod() throws SQLException{
        prod_box.getChildren().clear();

        List<CustomerProdList> ls = new ArrayList<>(prod_list());
        System.out.println(ls.size());

        for(int j = 0;j < ls.size(); j++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("CustomerProductList.fxml"));
            try {
                HBox hbox = fxmlloader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(hbox);
                CustomerProductListController cplc = fxmlloader.getController();
                cplc.setData(ls.get(j));
                prod_box.getChildren().add(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<CustomerProdList> prod_list() {
        List<CustomerProdList> ls;
        ls = new ArrayList<>();
        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();
        String query;

        if(text_pressed == null && (selectedCategory == null || selectedCategory == "All")){
            query = "SELECT * FROM Inventory i INNER JOIN Product p ON i.prod_id = p.prod_id where i.shop_id = '" + this.Shop_id + "'";
        }else if((selectedCategory == null || selectedCategory == "All") && text_pressed != null){
            query = "SELECT * FROM Inventory i INNER JOIN Product p ON i.prod_id = p.prod_id WHERE i.shop_id = '" + this.Shop_id + "' AND p.name LIKE '" + text_pressed + "'";
        }else if(selectedCategory != null && text_pressed == null) {
            query = "SELECT * FROM Inventory i INNER JOIN Product p ON i.prod_id = p.prod_id WHERE i.shop_id = '" + this.Shop_id + "' AND p.category = '" + selectedCategory + "'";
        }else{
            query = "SELECT * FROM Inventory i INNER JOIN Product p ON i.prod_id = p.prod_id WHERE i.shop_id = '" + this.Shop_id + "' AND p.category = '" + selectedCategory + "' AND p.name LIKE '" + text_pressed+ "'";
        }

        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                CustomerProdList prod = new CustomerProdList();
                prod.setProd_id(rs.getString(2));
                prod.setName((rs.getString(6)));
                prod.setPrice(rs.getFloat(10));
                prod.setDate(String.valueOf(rs.getDate(4)));
                prod.setTotal_quantity(rs.getInt(3));
                ls.add(prod);
            }
            rs.close();
            st.close();
            con.close();
            return ls;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ls;
    }

}
