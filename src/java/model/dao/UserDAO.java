package model.dao;

import model.User;
import java.sql.*;

public class UserDAO {

    // WHY findByEmail: Used by login to find user and verify password
    // MUST include passwordHash so login can compare passwords
    public User findByEmail(String email) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM users WHERE email=?")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setStudentId(rs.getString("student_id"));
                u.setName(rs.getString("name"));
                u.setPhoneNumber(rs.getString("phone_number"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash")); // ← ADDED
                u.setRole(rs.getString("role"));
                u.setActive(rs.getBoolean("is_active"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // WHY insertUser: Used by registration to save new user
    // Includes phone_number to match the database table structure
    public boolean insertUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users(student_id, name, phone_number, email, password_hash, role, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, user.getStudentId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPasswordHash());
            ps.setString(6, user.getRole());
            ps.setBoolean(7, user.isActive());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}