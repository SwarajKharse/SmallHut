package com.example.myproject;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class shopkeeperController implements Initializable{

    public String text_pressed = null;
    public String selectedCategory = null;
    public RadioButton selectedRadioButton = null;
    public String func = "view";
    public String Shop_id;
    public String Shop_name;

    public String getShop_id() {
        return this.Shop_id;
    }

    public String getShop_name() {
        return this.Shop_name;
    }

    public void setShop_id(String id) {
        this.Shop_id = id;
    }

    public void setShop_name(String name) {
        this.Shop_name = name;
    }

    @FXML
    private Label ColProduct5th;

    @FXML
    private Label ColProduct6th;

    @FXML
    private Label ColProductDate;

    @FXML
    private Label ColProductName;

    @FXML
    private Label ColProductPrice;

    @FXML
    private Label ColProductQuantity;

    @FXML
    private HBox ColumnNameBox;

    @FXML
    private Label SideBarLabel;

    @FXML
    private ImageView add_image;

    @FXML
    private ImageView add_image1;

    @FXML
    private ImageView add_image2;

    @FXML
    private HBox add_prod_box;

    @FXML
    private ToggleGroup categories;

    @FXML
    private HBox del_prod_box;

    @FXML
    private Button logout_btn;

    @FXML
    private Label my_account;

    @FXML
    private AnchorPane nav_top;

    @FXML
    private VBox prod_box;

    @FXML
    private VBox category_box;

    @FXML
    private VBox top_box;

    @FXML
    private HBox view_prod_box;

    @FXML
    private ScrollPane scroller;

    @FXML
    private TextField search_bar;

    public shopkeeperController() throws FileNotFoundException {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            read_from_file();
            ColumnNameBox.getChildren().remove(ColProduct6th);
            ColProduct5th.setText("Best Before");
            ColProductName.setText("Product Name");
            ColProductPrice.setText("Price");
            ColProductQuantity.setText("Quantity");
            ColProductDate.setText("Date");
            start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void display_acc(String name){
        my_account.setText(name);
    }

    // For Adding Products From Product table
    void box_fill_products() throws SQLException {
        SideBarLabel.setText("Add Products");
        rad_fill();
        prod_box.getChildren().clear();

        List<Product> ls = new ArrayList<>(products());
        System.out.println(ls.size());

        for(int j = 0;j < ls.size(); j++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("product_disp.fxml"));
            try {
                HBox hbox = fxmlloader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(hbox);
                ProductDisplayController pdc = fxmlloader.getController();
                pdc.setData(ls.get(j));
                prod_box.getChildren().add(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void start() throws Exception {
        if(func.equals("view")){
            view_prod();
        }else if(func.equals("del")){
            del_prod();
        }else {
            box_fill_products();
        }
        my_account.setText(getShop_name());
    }

    // For getting all the categories from the Table
    private void rad_fill() {
        category_box.getChildren().clear();
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
//        r.getStyleClass().add("radio-button");
        category_box.getChildren().add(r);
        r.setOnAction(e -> {
            selectedRadioButton = (RadioButton) e.getSource();
            selectedCategory = selectedRadioButton.getText();
            selectedRadioButton.setSelected(true);
            r.setSelected(true);
            System.out.println(selectedCategory);
            try {
                if(func.equals("view")){
                    view_prod();
                }else if(func.equals("del")){
                    del_prod();
                }else {
                    box_fill_products();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    void logout_process(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn_SignUp.fxml"));
            Parent root = fxmlLoader.load();
            LoginSignupController sc = fxmlLoader.getController();
            Stage stage = (Stage) logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void search_dynamic(javafx.scene.input.KeyEvent keyEvent) throws SQLException {
        text_pressed = "%" + search_bar.getText() + "%";
        if(func.equals("view")){
            view_prod();
        }else if(func.equals("del")){
            del_prod();
        }else {
            box_fill_products();
        }
    }

    private List<Product> products() throws SQLException {

        List<Product> ls;
        ls = new ArrayList<>();

        DatabaseConnection dbc = new DatabaseConnection();
        Connection con = dbc.getConnection();
        String query;
        if(text_pressed == null && (selectedCategory == null || selectedCategory == "All")){
            query = "SELECT * FROM Product";
        }else if((selectedCategory == null || selectedCategory == "All") && text_pressed != null){
            query = "SELECT * FROM Product WHERE name LIKE '" + text_pressed + "'";
        }else if(selectedCategory != null && text_pressed == null) {
            query = "SELECT * FROM Product WHERE category = '" + selectedCategory + "'";
        }else{
            query = "SELECT * FROM Product WHERE category = '" + selectedCategory + "' AND name LIKE '" + text_pressed+ "'";
        }

        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                Product prod = new Product();
                prod.setProduct_name(rs.getString(2));
                prod.setPrice(String.valueOf(rs.getInt(6)));
                ls.add(prod);
                System.out.println(rs.getString(1));
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

    public void add_product(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        func = "add";
        ColumnNameBox.getChildren().remove(ColProduct6th);
        ColProduct5th.setText("Add");
        ColProductName.setText("Product Name");
        ColProductPrice.setText("Price");
        ColProductQuantity.setText("Quantity");
        ColProductDate.setText("Date");
        clear_text_radio();
        box_fill_products();
    }

    public void delete_product(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        func = "del";
        ColumnNameBox.getChildren().add(ColProduct6th);
        ColProduct5th.setText("Best Before");
        ColProductName.setText("Product Name");
        ColProductPrice.setText("Price");
        ColProductQuantity.setText("Quantity");
        ColProductDate.setText("Date");
        clear_text_radio();
        del_prod();
    }

    public void view_inventory(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        func = "view";
        ColumnNameBox.getChildren().remove(ColProduct6th);
        ColProduct5th.setText("Best Before");
        ColProductName.setText("Product Name");
        ColProductPrice.setText("Price");
        ColProductQuantity.setText("Quantity");
        ColProductDate.setText("Date");
        clear_text_radio();
        view_prod();
    }

    public void clear_text_radio(){
        prod_box.getChildren().clear();
        if (selectedRadioButton != null) {
//            selectedRadioButton.setSelected(false);
            selectedRadioButton = null;
        }
        text_pressed = null;
        search_bar.clear();
    }

    // Bring all the delete product in the VBox
    public void del_prod() throws SQLException {
        SideBarLabel.setText("Delete Products");
        rad_fill();
        prod_box.getChildren().clear();
        List<ProductInventory> ls = new ArrayList<>(prod_list());
        System.out.println(ls.size());

        for(int j = 0;j < ls.size(); j++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("product_view_del.fxml"));
            try {
                HBox hbox = fxmlloader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(hbox);
                ProductViewDelController pvdc = fxmlloader.getController();
                pvdc.unhide_del_btn();
                pvdc.setData(ls.get(j));
                prod_box.getChildren().add(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ProductInventory> prod_list() {
        List<ProductInventory> ls;
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
                ProductInventory prod = new ProductInventory();
                prod.setProduct_name(rs.getString(6));
                prod.setPrice(String.valueOf(rs.getInt(10)));
                prod.setQuantity(String.valueOf(rs.getInt(3)));
                prod.setBest_bef(rs.getInt(9));
                prod.setDate(String.valueOf(rs.getDate(4)));
                ls.add(prod);
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " +
                        rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " "
                        + rs.getString(7) + " " + rs.getString(8) + " " + rs.getString(9) + " " +
                        rs.getString(10));
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


    private void view_prod() throws SQLException{
        SideBarLabel.setText("View Products");
        rad_fill();
        prod_box.getChildren().clear();
        List<ProductInventory> ls = new ArrayList<>(prod_list());
        System.out.println(ls.size());

        for(int j = 0;j < ls.size(); j++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("product_view_del.fxml"));
            try {
                HBox hbox = fxmlloader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(hbox);
                ProductViewDelController pvdc = fxmlloader.getController();
                pvdc.hide_del_btn();
                pvdc.setData(ls.get(j));
                prod_box.getChildren().add(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read_from_file() throws IOException {
        FileInputStream fis = new FileInputStream("shop_acc.txt");
        DataInputStream dis = new DataInputStream(fis);
        Shop_name = dis.readUTF();
        Shop_id = dis.readUTF();
    }

}

