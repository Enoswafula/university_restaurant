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

        HttpSession session = req.getSession();

        Map<Integer, Integer> cart = new HashMap<>();

        // Loop through all request parameters
        Map<String, String[]> params = req.getParameterMap();

        for (String key : params.keySet()) {
            if (key.startsWith("qty_")) {
                int menuId = Integer.parseInt(key.replace("qty_", ""));
                int qty = Integer.parseInt(req.getParameter(key));

                if (qty > 0) {
                    cart.put(menuId, qty);
                }
            }
        }

        if (cart.isEmpty()) {
            resp.sendRedirect("menu.jsp");
            return;
        }

        session.setAttribute("cart", cart);

        resp.sendRedirect("order.jsp");
    }
}