<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1>
    <c:choose>
        <c:when test="${region == null}">
            <fmt:message key="msg.region-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.region-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">
        ${errorMessage}
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/regions" method="post">

    <input type="hidden"
           name="id"
           value="${region != null ? region.id : ''}" />

    <input type="hidden"
           name="action"
           value="${region == null ? 'insert' : 'update'}" />

    <label for="code">
        <fmt:message key="msg.region-form.code" />:
    </label>

    <input type="text"
           name="code"
           id="code"
           value="${region != null ? region.code : ''}"
           required />

    <br><br>

    <label for="name">
        <fmt:message key="msg.region-form.name" />:
    </label>

    <input type="text"
           name="name"
           id="name"
           value="${region != null ? region.name : ''}"
           required />

    <br><br>

    <c:choose>
        <c:when test="${region == null}">
            <input type="submit"
                   value="<fmt:message key='msg.region-form.create' />" />
        </c:when>

        <c:otherwise>
            <input type="submit"
                   value="<fmt:message key='msg.region-form.update' />" />
        </c:otherwise>
    </c:choose>

</form>

<a href="${pageContext.request.contextPath}/regions">
    <fmt:message key="msg.region-form.returnback" />
</a>

<%@ include file="../../partials/footer.jsp" %>