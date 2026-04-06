<%-- Member D (Payments & Notifications) - payment.jsp
     Cashier payment processing page.

     Access: cashier role only. Redirect to login if not cashier.

     Handles three states:
       1. Fresh form (no orderId) — cashier types the order ID manually.
       2. Pre-filled form (orderId passed via GET ?orderId=X or request attribute).
       3. Payment history panel (shown when paymentHistory attribute is set).
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User, model.Payment, java.util.List" %>
<%
    /* ── Session guard ─────────────────────────────────────────────── */
    User user = (User) session.getAttribute("user");
    if (user == null || !"cashier".equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }

    /* ── One-time success flash from session ───────────────────────── */
    String successMsg = (String) session.getAttribute("paymentSuccess");
    if (successMsg != null) {
        session.removeAttribute("paymentSuccess");
    }

    /* ── Error / warning from request (set by PaymentServlet) ───────── */
    String errorMsg   = (String) request.getAttribute("error");
    String warningMsg = (String) request.getAttribute("warning");

    /* ── Pre-fill values (set when cashier arrives via ?orderId=X) ─── */
    Object preOrderIdObj = request.getAttribute("prefilledOrderId");
    Object preAmountObj  = request.getAttribute("prefilledAmount");
    Object selMethodObj  = request.getAttribute("selectedMethod");

    String preOrderId  = preOrderIdObj  != null ? preOrderIdObj.toString() : "";
    String preAmount   = preAmountObj   != null ? preAmountObj.toString()  : "";
    String selMethod   = selMethodObj   != null ? selMethodObj.toString()  : "cash";

    /* ── Payment history (set when action=history) ──────────────────── */
    List<Payment> paymentHistory =
        (List<Payment>) request.getAttribute("paymentHistory");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Process Payment – University Restaurant</title>
    <style>
        /* ── Base reset & layout ─────────────────────────── */
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body   { font-family: Arial, Helvetica, sans-serif; background: #f4f6f8; color: #333; }
        header { background: #2c3e50; color: #fff; padding: 14px 24px; display: flex;
                 align-items: center; justify-content: space-between; }
        header h1 { font-size: 1.2rem; }
        header nav a { color: #ecf0f1; text-decoration: none; margin-left: 18px;
                       font-size: 0.9rem; }
        header nav a:hover { text-decoration: underline; }

        .page-wrap { max-width: 860px; margin: 32px auto; padding: 0 16px; }

        /* ── Card ────────────────────────────────────────── */
        .card { background: #fff; border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.10); padding: 28px 32px;
                margin-bottom: 28px; }
        .card h2 { font-size: 1.3rem; color: #2c3e50; margin-bottom: 20px;
                   border-bottom: 2px solid #3498db; padding-bottom: 8px; }

        /* ── Alert banners ────────────────────────────────── */
        .alert { padding: 12px 16px; border-radius: 6px; margin-bottom: 18px;
                 font-size: 0.95rem; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-error   { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .alert-warning { background: #fff3cd; color: #856404; border: 1px solid #ffeeba; }

        /* ── Form ─────────────────────────────────────────── */
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 6px;
                            font-size: 0.9rem; color: #555; }
        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group select {
            width: 100%; padding: 10px 12px; border: 1px solid #ced4da;
            border-radius: 5px; font-size: 0.95rem; transition: border-color .2s;
        }
        .form-group input:focus,
        .form-group select:focus { border-color: #3498db; outline: none; }
        .form-group small { display: block; color: #888; font-size: 0.8rem; margin-top: 4px; }

        .ref-group { display: none; } /* shown via JS for mobile money */

        /* ── Buttons ──────────────────────────────────────── */
        .btn { display: inline-block; padding: 10px 22px; border: none; border-radius: 5px;
               cursor: pointer; font-size: 0.95rem; text-decoration: none; }
        .btn-primary { background: #3498db; color: #fff; }
        .btn-primary:hover { background: #2980b9; }
        .btn-secondary { background: #7f8c8d; color: #fff; }
        .btn-secondary:hover { background: #636e72; }
        .btn-sm { padding: 6px 14px; font-size: 0.85rem; }
        .btn-row { display: flex; gap: 12px; margin-top: 8px; }

        /* ── Summary box ──────────────────────────────────── */
        .summary-box { background: #eaf4fb; border: 1px solid #b8dff5;
                       border-radius: 6px; padding: 14px 18px; margin-bottom: 20px; }
        .summary-box p { margin: 4px 0; font-size: 0.9rem; }
        .summary-box strong { color: #2c3e50; }

        /* ── Payment history table ────────────────────────── */
        .history-table { width: 100%; border-collapse: collapse; font-size: 0.88rem; }
        .history-table th, .history-table td {
            border: 1px solid #dee2e6; padding: 9px 12px; text-align: left;
        }
        .history-table th { background: #2c3e50; color: #fff; }
        .history-table tr:nth-child(even) { background: #f8f9fa; }
        .badge { display: inline-block; padding: 3px 8px; border-radius: 12px;
                 font-size: 0.78rem; font-weight: bold; }
        .badge-cash   { background: #d4edda; color: #155724; }
        .badge-mobile { background: #cce5ff; color: #004085; }

        footer { text-align: center; padding: 18px; color: #888; font-size: 0.8rem;
                 margin-top: 24px; }
    </style>
</head>
<body>

<!-- ── Header / Nav ─────────────────────────────────────────────────── -->
<header>
    <h1>🏫 University Restaurant System</h1>
    <nav>
        <a href="<%= request.getContextPath() %>/payment.jsp">Payments</a>
        <a href="<%= request.getContextPath() %>/PaymentServlet?action=history">History</a>
        <%@ include file="layout.jsp" %>
    </nav>
</header>

<div class="page-wrap">

    <!-- ── Flash messages ─────────────────────────────────────────── -->
    <% if (successMsg != null) { %>
        <div class="alert alert-success">✅ <%= successMsg %></div>
    <% } %>
    <% if (errorMsg != null) { %>
        <div class="alert alert-error">❌ <%= errorMsg %></div>
    <% } %>
    <% if (warningMsg != null) { %>
        <div class="alert alert-warning">⚠️ <%= warningMsg %></div>
    <% } %>

    <%-- ═══════════════════════════════════════════════════════════
         PAYMENT FORM CARD
    ══════════════════════════════════════════════════════════════ --%>
    <% if (paymentHistory == null) { %>
    <div class="card">
        <h2>💳 Process Payment</h2>

        <% if (!preOrderId.isEmpty() && errorMsg == null && warningMsg == null) { %>
        <div class="summary-box">
            <p><strong>Order #<%= preOrderId %></strong> is ready for payment.</p>
            <% if (!preAmount.isEmpty()) { %>
            <p>Amount due: <strong>KES <%= preAmount %></strong></p>
            <% } %>
        </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/PaymentServlet" method="post"
              id="paymentForm" novalidate>

            <!-- Order ID -->
            <div class="form-group">
                <label for="orderId">Order ID <span style="color:red">*</span></label>
                <input type="number" id="orderId" name="orderId" min="1"
                       value="<%= preOrderId %>"
                       placeholder="Enter the order number"
                       required />
                <small>Ask the student for their Order ID or scan the queue ticket.</small>
            </div>

            <!-- Amount -->
            <div class="form-group">
                <label for="amount">Amount (KES) <span style="color:red">*</span></label>
                <input type="number" id="amount" name="amount"
                       min="0.01" step="0.01"
                       value="<%= preAmount %>"
                       placeholder="0.00"
                       required />
            </div>

            <!-- Payment Method -->
            <div class="form-group">
                <label for="paymentMethod">Payment Method <span style="color:red">*</span></label>
                <select id="paymentMethod" name="paymentMethod"
                        onchange="toggleReference(this.value)">
                    <option value="cash"
                        <%= "cash".equals(selMethod) ? "selected" : "" %>>Cash</option>
                    <option value="mobile_money"
                        <%= "mobile_money".equals(selMethod) ? "selected" : "" %>>Mobile Money (M-Pesa)</option>
                </select>
            </div>

            <!-- Transaction Reference (mobile money only) -->
            <div class="form-group ref-group" id="refGroup">
                <label for="transactionReference">
                    M-Pesa Transaction Reference <span style="color:red">*</span>
                </label>
                <input type="text" id="transactionReference"
                       name="transactionReference"
                       placeholder="e.g. QA12B3C4D5"
                       maxlength="100" />
                <small>Enter the M-Pesa confirmation code sent to the customer's phone.</small>
            </div>

            <div class="btn-row">
                <button type="submit" class="btn btn-primary">✅ Confirm Payment</button>
                <a href="<%= request.getContextPath() %>/payment.jsp"
                   class="btn btn-secondary">🔄 Clear Form</a>
            </div>
        </form>
    </div><!-- /.card -->
    <% } %>

    <%-- ═══════════════════════════════════════════════════════════
         PAYMENT HISTORY TABLE (shown when action=history)
    ══════════════════════════════════════════════════════════════ --%>
    <% if (paymentHistory != null) { %>
    <div class="card">
        <h2>📋 Payment History</h2>

        <div class="btn-row" style="margin-bottom:16px">
            <a href="<%= request.getContextPath() %>/payment.jsp"
               class="btn btn-primary btn-sm">➕ New Payment</a>
        </div>

        <% if (paymentHistory.isEmpty()) { %>
            <p style="color:#888">No payments recorded yet.</p>
        <% } else { %>
        <table class="history-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Payment ID</th>
                    <th>Order ID</th>
                    <th>Amount (KES)</th>
                    <th>Method</th>
                    <th>Reference</th>
                    <th>Cashier</th>
                    <th>Date</th>
                    <th>Time</th>
                </tr>
            </thead>
            <tbody>
                <% int rowNum = 1; for (Payment p : paymentHistory) { %>
                <tr>
                    <td><%= rowNum++ %></td>
                    <td>#<%= p.getPaymentId() %></td>
                    <td>#<%= p.getOrderId() %></td>
                    <td><strong><%= String.format("%.2f", p.getAmount()) %></strong></td>
                    <td>
                        <% if (Payment.METHOD_MOBILE_MONEY.equals(p.getPaymentMethod())) { %>
                            <span class="badge badge-mobile">Mobile Money</span>
                        <% } else { %>
                            <span class="badge badge-cash">Cash</span>
                        <% } %>
                    </td>
                    <td>
                        <%= (p.getTransactionReference() != null &&
                             !p.getTransactionReference().isEmpty())
                            ? p.getTransactionReference() : "—" %>
                    </td>
                    <td><%= p.getCashierName() != null ? p.getCashierName() : "—" %></td>
                    <td><%= p.getPaymentDate() %></td>
                    <td><%= p.getPaymentTime() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } %>
    </div><!-- /.card -->
    <% } %>

</div><!-- /.page-wrap -->

<footer>&copy; 2026 University Restaurant Management System</footer>

<script>
    /* Show / hide the transaction reference field based on selected method */
    function toggleReference(method) {
        var refGroup = document.getElementById('refGroup');
        var refInput = document.getElementById('transactionReference');
        if (method === 'mobile_money') {
            refGroup.style.display = 'block';
            refInput.required = true;
        } else {
            refGroup.style.display = 'none';
            refInput.required = false;
            refInput.value = '';
        }
    }

    /* Run on page load to handle pre-selected method (e.g. after a validation error) */
    (function () {
        var sel = document.getElementById('paymentMethod');
        if (sel) toggleReference(sel.value);
    })();

    /* Client-side validation before submit */
    document.getElementById('paymentForm').addEventListener('submit', function (e) {
        var orderId = document.getElementById('orderId').value.trim();
        var amount  = document.getElementById('amount').value.trim();
        var method  = document.getElementById('paymentMethod').value;
        var ref     = document.getElementById('transactionReference').value.trim();

        if (!orderId || parseInt(orderId) <= 0) {
            alert('Please enter a valid Order ID.');
            e.preventDefault(); return;
        }
        if (!amount || parseFloat(amount) <= 0) {
            alert('Please enter a valid amount greater than zero.');
            e.preventDefault(); return;
        }
        if (method === 'mobile_money' && ref === '') {
            alert('Please enter the M-Pesa transaction reference for Mobile Money payments.');
            e.preventDefault(); return;
        }
    });
</script>
</body>
</html>
