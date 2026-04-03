package model.dao;

import model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    // Member D uses this to save the transaction
    public boolean insertPayment(Payment payment) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO payments(order_id, cashier_id, amount, payment_method, transaction_reference, payment_date, payment_time) VALUES (?, ?, ?, ?, ?, CURDATE(), CURTIME())")) {

            ps.setInt(1, payment.getOrderId());
            ps.setInt(2, payment.getCashierId());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getTransactionReference());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Payment> getPaymentsByOrder(int orderId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setOrderId(rs.getInt("order_id"));
                p.setCashierId(rs.getInt("cashier_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentMethod(rs.getString("payment_method"));
                p.setTransactionReference(rs.getString("transaction_reference"));
                p.setPaymentDate(rs.getDate("payment_date").toString());
                p.setPaymentTime(rs.getTime("payment_time").toString());
                payments.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
}
