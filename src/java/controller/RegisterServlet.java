// Member B (Authentication & User Management) should implement this file

package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.User;
import model.dao.UserDAO;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Collect form inputs
        String studentId = req.getParameter("studentId");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role"); // student, cashier, admin

        // Create user object
        User newUser = new User();
        newUser.setStudentId(studentId);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash(password); // TODO: hash password properly
        newUser.setRole(role);
        newUser.setActive(true);

        // Save user via DAO
        UserDAO dao = new UserDAO();
        boolean success = dao.insertUser(newUser);

        if (success) {
            resp.sendRedirect("login.jsp");
        } else {
            req.setAttribute("error", "Registration failed. Try again.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
