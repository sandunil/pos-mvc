/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.mvc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pos.mvc.db.DbConnection;
import pos.mvc.model.Item;

/**
 *
 * @author anjanathrishakya
 */
public class ItemController {

    public String addItem(Item item) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String query = "INSERT INTO item VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, item.getId());
        preparedStatement.setString(2, item.getName());
        preparedStatement.setInt(3, item.getQtyOnHand());
        preparedStatement.setDouble(4, item.getUnitPrice());

        if(preparedStatement.executeUpdate()>0){
            return "Success";
        } else{
            return "Fail";
        } 
    }
    
    public List<Item> getItems() throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM item";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<Item> items = new ArrayList<>();
        
        while (resultSet.next()) {
            Item item = new Item(
                    resultSet.getInt("id"), 
                    resultSet.getString("name"), 
                    resultSet.getInt("qoh"),
                    resultSet.getDouble("unitPrice"));
            
            items.add(item);
        }
        return items;
    }
    
    public Item getItem(Integer id) throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM item where id = ?";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        
       
        
        while (resultSet.next()) {
            return new Item(
                    resultSet.getInt("id"), 
                    resultSet.getString("name"), 
                    resultSet.getInt("qoh"),
                    resultSet.getDouble("unitPrice"));
        }
        return null;
    }
    
    public String updateItem(Item item) throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        
        String query = "UPDATE item SET name =?, qoh = ?, unitPrice = ? WHERE id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1, item.getName());
        preparedStatement.setInt(2, item.getQtyOnHand());
        preparedStatement.setDouble(3, item.getUnitPrice());
        preparedStatement.setInt(4, item.getId());

        if(preparedStatement.executeUpdate()>0){
            return "Success";
        } else{
            return "Fail";
        } 
    }
    
    public String deleteItem(Integer id) throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "DELETE FROM item WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        if(preparedStatement.executeUpdate()>0){
            return "Success";
        } else{
            return "Fail";
        } 
    }

}
