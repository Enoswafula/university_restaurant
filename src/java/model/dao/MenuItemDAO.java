package model.dao;

import model.MenuItem;
import java.sql.*;
import java.util.*;

public class MenuItemDAO {

    // 1. Get all food for the menu
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE is_available = 1";
        
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
            System.out.println("Loaded " + items.size() + " items from the menu.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    // 2. Add new food (Admin)
    public boolean insertMenuItem(MenuItem m) {
        String sql = "INSERT INTO menu_items (name, description, price, meal_period, image_url, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getDescription());
            ps.setDouble(3, m.getPrice());
            ps.setString(4, m.getMealPeriod());
            ps.setString(5, m.getImageUrl());
            ps.setBoolean(6, m.isAvailable());

            boolean result = ps.executeUpdate() > 0;
            if(result) System.out.println("New menu item inserted successfully!");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Update food (Admin)
    public boolean updateMenuItem(MenuItem m) {
        String sql = "UPDATE menu_items SET name=?, description=?, price=?, meal_period=?, image_url=?, is_available=? WHERE menu_id=?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getDescription());
            ps.setDouble(3, m.getPrice());
            ps.setString(4, m.getMealPeriod());
            ps.setString(5, m.getImageUrl());
            ps.setBoolean(6, m.isAvailable());
            ps.setInt(7, m.getMenuId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
