// Member C (Orders & Queue Management) should implement this file

package model.dao;
import model.MenuItem;
import java.sql.*;
import java.util.*;

public class MenuItemDAO {
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM menu_items WHERE is_available=1")) {

            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setMenuId(rs.getInt("menu_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setMealPeriod(rs.getString("meal_period"));
                item.setImageUrl(rs.getString("image_url"));
                item.setAvailable(rs.getBoolean("is_available"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
