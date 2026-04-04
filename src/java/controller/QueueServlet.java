// Member C (Orders & Queue Management) should implement this file

package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;

public class QueueServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object cartObj = session.getAttribute("cart");

        if (cartObj == null) {
            resp.sendRedirect("menu.jsp"); // No cart, go back
            return;
        }

        // Simulate assigning a queue number
        Random rand = new Random();
        int queueNumber = rand.nextInt(1000) + 1; // 1–1000

        session.setAttribute("queueNumber", queueNumber);

        resp.sendRedirect("queue.jsp");
    }
}