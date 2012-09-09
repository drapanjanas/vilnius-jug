<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}/spring"/>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>New Message</title>
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
    	<form class="form-horizontal">
    		<div class="control-group">
    			<label class="control-label">From</label>
    			<div class="controls">
      				<input type="text" class="input-xxlarge" disabled="disabled" value="${message.content.from}">
    			</div>
  			</div>
  			<div class="control-group">
    			<label class="control-label">To</label>
    			<div class="controls">
      				<input type="text" class="input-xxlarge" disabled="disabled" value="${message.content.to}">
    			</div>
  			</div>
  			<div class="control-group">
    			<label class="control-label">Subject</label>
    			<div class="controls">
      				<input type="text" class="input-xxlarge" disabled="disabled" value="${message.content.subject}">
    			</div>
  			</div>
  			
  			<div class="control-group">
    			<label class="control-label">Message text</label>
    			<div class="controls">
      				<textarea class="input-xxlarge" disabled="disabled">${message.content.body}</textarea>
    			</div>
  			</div>
  			<div class="form-actions">
  				<a class="btn" href="${contextPath}/mail/${folder}" >&lt;&lt; Back</a>
			</div>
  		</form>	
    </div> <!-- /container -->

    
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.js"></script>

  </body>
</html>
