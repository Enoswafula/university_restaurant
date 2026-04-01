// Member D (Payments & Notifications) should implement this file

package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.dao.PaymentDAO;
import model.Payment;

public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get payment details from form
        String method = req.getParameter("paymentMethod"); // cash or mobile_money
        double amount = Double.parseDouble(req.getParameter("amount"));
        String reference = req.getParameter("transactionReference"); // only for mobile money

        // Create payment object
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setTransactionReference(reference);

        // Save payment via DAO
        PaymentDAO dao = new PaymentDAO();
        boolean success = dao.insertPayment(payment);

        if (success) {
            // Redirect to confirmation page
            resp.sendRedirect("notification.jsp");
        } else {
            req.setAttribute("error", "Payment failed. Try again.");
            req.getRequestDispatcher("payment.jsp").forward(req, resp);
        }
    }
}
