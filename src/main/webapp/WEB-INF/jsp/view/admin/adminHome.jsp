<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">

			<jsp:include page="../fragments/menu.jsp" />

		</div>
	</nav>





	<h3>Administration home page</h3>
	<p>Hello and welcome to your application</p>

<div style="text-align:center; padding-top: 50px">
	<p>Click on the "Choose File" button to upload the file:</p>

<form action="${pageContext.request.contextPath}/admin/ImportData" method="POST" enctype="multipart/form-data">
  <input type="file" id="myFile" name="file">
  <input type="submit">
  
</form>
</div>
</div>