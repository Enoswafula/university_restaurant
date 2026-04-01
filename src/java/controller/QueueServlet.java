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

        // Retrieve cart from session
        HttpSession session = req.getSession();
        Object cartObj = session.getAttribute("cart");

        if (cartObj == null) {
            resp.sendRedirect("menu.jsp"); // No cart, go back to menu
            return;
        }

        // For now, simulate assigning a queue number
        Random rand = new Random();
        int queueNumber = rand.nextInt(1000); // TODO: replace with DB logic

        // Save queue number in session
        session.setAttribute("queueNumber", queueNumber);

        // Redirect to queue page
        resp.sendRedirect("queue.jsp");
    }
}
