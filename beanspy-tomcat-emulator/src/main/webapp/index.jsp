<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
<h2>BeanSpy Tomcat Emulator</h2>

<c:choose>
    <c:when test="${not empty applicationScope.applications}">
    	<span>Loaded Applications:</span>
    	<ul>
		<c:forEach var="application" items="${applicationScope.applications}">
    		<li>${application}</li>
		</c:forEach>
		</ul>
    </c:when>
    <c:otherwise>
        No applications configured
    </c:otherwise>
</c:choose>

</body>
</html>