<jsp:include page="header.jsp" />

<h2>Dashboard Summary</h2>

<div class="alert-summary">
    <div class="card high">
        <h3>High Severity</h3>
        <p style="font-size: 2rem; font-weight: bold;">${counts['high']}</p>
    </div>
    <div class="card medium">
        <h3>Medium Severity</h3>
        <p style="font-size: 2rem; font-weight: bold;">${counts['medium']}</p>
    </div>
    <div class="card low">
        <h3>Low Severity</h3>
        <p style="font-size: 2rem; font-weight: bold;">${counts['low']}</p>
    </div>
</div>

<div class="form-section" style="text-align: center;">
    <h3>Fraud Engine Control</h3>
    <form action="dashboard" method="post">
        <input type="hidden" name="action" value="runDetection">
        <button type="submit" class="btn btn-danger">Run Fraud Detection Rules</button>
    </form>
    <p style="color: #666; margin-top: 1rem;">This will analyze transactions and generate new alerts based on fraud patterns.</p>
</div>

<jsp:include page="footer.jsp" />
