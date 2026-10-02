<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="partials/header.jsp" %>

<h2>Hello, World!</h2>
        <%
            int count = 10;

            for (int i = 0; i < count; i++) {
                out.println("Number: " + i + "<br>");
            }
        %>

        <%!
            private int counter = 0;

            public int getCounter() {
                return ++counter;
            }
        %>

        <p>Counter Value: <%= getCounter() %></p>

        <%
            String userName = "FranFdez";
            int age = 22;
        %>

        <p>User Name: <%= userName %></p>
        <p>Current Time: <%= new java.util.Date() %></p>
        <p>User Age: <%= age %></p>
        <%
            // Scriptlet para realizar cálculos
            int num1 = 10;
            int num2 = 20;
            int sum = num1 + num2;
        %>

        <p>Sum of 10 and 20 is: <%= sum %></p>

        <%
            String role = "admin";

            if ("admin".equals(role)) {
        %>

                <p>Welcome, Administrator!</p>

        <%
            } else {
        %>

                <p>Welcome, User!</p>

        <%
            }
        %>

<%@ include file="partials/footer.jsp" %>