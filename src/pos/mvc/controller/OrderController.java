/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.mvc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import pos.mvc.db.DbConnection;
import pos.mvc.model.Order;
import pos.mvc.model.OrderDetail;

/**
 *
 * @author anjanathrishakya
 */
public class OrderController {

    public String placeOrder(Order order, List<OrderDetail> orderDetails) throws Exception {
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String queryOrder = "INSERT INTO `order` VALUES (?,?,?,?)";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            
            PreparedStatement preparedStatement = connection.prepareStatement(queryOrder);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, order.getCustomerId());
            preparedStatement.setString(3, sdf.format(order.getOrderDate()));
            preparedStatement.setDouble(4, order.getTotal());
          
            if (preparedStatement.executeUpdate() > 0) {
                
                String queryOrderDetails = "INSERT INTO orderdetail (order_id, unit_price, qty, item_id, sub_total) VALUES (?,?,?,?,?)";
                
                boolean isSaved = true;
                
                for (OrderDetail orderDetail : orderDetails) {
                    PreparedStatement preparedStatementForOrderDetails = connection.prepareStatement(queryOrderDetails);
                    preparedStatementForOrderDetails.setInt(1, order.getId());
                    preparedStatementForOrderDetails.setDouble(2, orderDetail.getUnitPrice());
                    preparedStatementForOrderDetails.setInt(3, orderDetail.getQty());
                    preparedStatementForOrderDetails.setInt(4, orderDetail.getItemId());
                    preparedStatementForOrderDetails.setDouble(5, orderDetail.getUnitPrice() * orderDetail.getQty());
                    if(!(preparedStatementForOrderDetails.executeUpdate()>0)){
                        isSaved = false;
                    }
                }
                
                if(isSaved){
                    boolean isUpdated = true;
                    String queryItem = "UPDATE ITEM SET qoh = qoh - ? WHERE id = ?";
                    for (OrderDetail orderDetail : orderDetails) {
                        PreparedStatement preparedStatementForItem = connection.prepareStatement(queryItem);
                        preparedStatementForItem.setInt(1, orderDetail.getQty());
                        preparedStatementForItem.setInt(2, orderDetail.getItemId());
                        
                        if(!(preparedStatementForItem.executeUpdate() > 0)){
                            isUpdated = false;
                        }
                    }
                    
                    if(isUpdated){
                        connection.commit();
                        return "Success";
                    } else{
                        connection.rollback();
                        return "Error Updating item";
                    }
                    
                    
                } else{
                    connection.rollback();
                    return "Error saving order details";
                }
                
                
            } else {
                connection.rollback();
                return "Error saving order";
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
