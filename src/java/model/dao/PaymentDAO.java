// Member D (Payments & Notifications) should implement this file

package model.dao;
import model.Payment;
import java.sql.*;

public class PaymentDAO {
    public boolean insertPayment(Payment payment) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO payments(order_id, cashier_id, amount, payment_method, transaction_reference, payment_date, payment_time) VALUES (?, ?, ?, ?, ?, CURDATE(), CURTIME())")) {

            ps.setInt(1, payment.getOrderId());
            ps.setInt(2, payment.getCashierId());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getTransactionReference());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
