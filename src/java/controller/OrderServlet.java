// Member C (Orders & Queue Management) should implement this file

package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class OrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Retrieve selected menu item and quantity
        int menuId = Integer.parseInt(req.getParameter("menuId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // Create a cart item (simple map for now)
        Map<Integer, Integer> cart = (Map<Integer, Integer>) req.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Add or update item in cart
        cart.put(menuId, cart.getOrDefault(menuId, 0) + quantity);

        // Save cart back to session
        req.getSession().setAttribute("cart", cart);

        // Redirect to cart review page
        resp.sendRedirect("order.jsp");
    }
}
