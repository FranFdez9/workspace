<%@ include file="../partials/header.jsp" %>

        <h2>Hello World</h2>

        <%
            if (request.getAttribute("userName") != null) {
        %>

            <h2>Hello ${userName}</h2>

        <%
            }
        %>

<%@ include file="../partials/footer.jsp" %>