<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1>
    <fmt:message key="msg.region-list.title" />
</h1>

<a href="${pageContext.request.contextPath}/regions?action=new">
    <fmt:message key="msg.region-list.add" />
</a>

<table border="1">
    <thead>
        <tr>
            <th>
                <fmt:message key="msg.region-list.id" />
            </th>

            <th>
                <fmt:message key="msg.region-list.code" />
            </th>

            <th>
                <fmt:message key="msg.region-list.name" />
            </th>

            <th>
                <fmt:message key="msg.region-list.actions" />
            </th>
        </tr>
    </thead>

    <tbody>

        <c:forEach var="region" items="${listRegions}">
            <tr>

                <td>${region.id}</td>
                <td>${region.code}</td>
                <td>${region.name}</td>

                <td>

                    <a href="${pageContext.request.contextPath}/regions?action=edit&id=${region.id}">
                        <fmt:message key="msg.region-list.edit" />
                    </a>

                    <form action="${pageContext.request.contextPath}/regions"
                          method="post"
                          style="display:inline;">

                        <input type="hidden"
                               name="id"
                               value="${region.id}">

                        <input type="hidden"
                               name="action"
                               value="delete">

                        <button type="submit"
                                onclick="return confirm('<fmt:message key="msg.region-list.confirm" />');">

                            <fmt:message key="msg.region-list.delete" />

                        </button>

                    </form>

                </td>

            </tr>
        </c:forEach>

    </tbody>
</table>

<%@ include file="../../partials/footer.jsp" %>