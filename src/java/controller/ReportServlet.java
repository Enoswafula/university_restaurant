// Member E (Reports & Admin Dashboard) should implement this file

package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.dao.DailyReportDAO;
import model.DailyReport;

public class ReportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DailyReportDAO dao = new DailyReportDAO();
        List<DailyReport> reports = dao.getAllReports();

        req.setAttribute("reports", reports);
        req.getRequestDispatcher("report.jsp").forward(req, resp);
    }
}
