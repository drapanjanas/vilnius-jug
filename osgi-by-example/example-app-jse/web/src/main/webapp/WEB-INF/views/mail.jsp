<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}/spring"/>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    
    <link href="${contextPath}/resources/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="${contextPath}/">MailSpot</a>
        </div>
      </div>
    </div>

    <div class="container">
    	<h3>Inbox: ${currentInbox.address}</h3>
    	<ul class="nav nav-tabs">
  			<li id="inbox-folder"><a href="${contextPath}/mail/inbox">Inbox</a></li>
  			<li id="drafts-folder"><a href="${contextPath}/mail/drafts">Drafts</a></li>
  			<li id="sent-folder"><a href="${contextPath}/mail/sent">Sent</a></li>
  			<li id="received-folder"><a href="${contextPath}/mail/received">Received</a></li>
  			<li id="deleted-folder"><a href="${contextPath}/mail/deleted">Deleted</a></li>
		</ul>
		<table class="table table-striped hover">
  			<tbody>
  				<c:forEach var="message" items="${messages}">
  					<tr>
  						<td>${mesage.displayAddress}</td>
  						<td>${mesage.content.subject}</td>
  					</tr>
  				</c:forEach>
  			</tbody>
  		</table>
  		<div class="form-actions">
  			<a class="btn btn-primary" href="${contextPath}/mail/compose" >Compose</a>
		</div>
		
    </div> <!-- /container -->

    
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.js"></script>
	<script>
		$(function(){
			$('#${folder}-folder').addClass("active");
		});
	</script>
  </body>
</html>
