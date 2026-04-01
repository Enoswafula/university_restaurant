// Member D (Payments & Notifications) should implement this file

package model.dao;
import model.Notification;
import java.sql.*;
import java.util.*;

public class NotificationDAO {
    public boolean insertNotification(Notification n) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO notifications(customer_id, order_id, message, type, is_read, sent_at) VALUES (?, ?, ?, ?, ?, NOW())")) {

            ps.setInt(1, n.getCustomerId());
            ps.setInt(2, n.getOrderId());
            ps.setString(3, n.getMessage());
            ps.setString(4, n.getType());
            ps.setBoolean(5, n.isRead());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Notification> getNotificationsByUser(int customerId) {
        List<Notification> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM notifications WHERE customer_id=? ORDER BY sent_at DESC")) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setCustomerId(rs.getInt("customer_id"));
                n.setOrderId(rs.getInt("order_id"));
                n.setMessage(rs.getString("message"));
                n.setType(rs.getString("type"));
                n.setRead(rs.getBoolean("is_read"));
                n.setSentAt(rs.getString("sent_at"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
