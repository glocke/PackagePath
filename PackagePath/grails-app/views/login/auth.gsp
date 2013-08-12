<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title><g:message code="springSecurity.login.title" /></title>
		<r:require modules="login"/>
	</head>
	<body>
		<div class="navbar navbar-inverse navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="brand" href="#">PACKAGE<span class="brand-blue">PATH</span></a>
	          <div class="nav-collapse collapse">
	            <div class="nav-collapse collapse">
					<ul class="nav">
						<li><a class="nav-icon nav-dash" href="${createLink(controller: 'Dashboard')}" title="Dashboard"></a></li>
						<li><a class="nav-icon nav-contact" href="#contact" title="Contact Us"></a></li>
					</ul>
				</div>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
		
	    <div class="container" style="margin-top:40px;">
			<div id='login'>
			    <div class='inner'>
			      <g:if test='${flash.message}'>
			      <div class='login_message'>${flash.message}</div>
			      </g:if>
			      <ul class="socialLogins">
			        <li class="google"><oauth:connect provider="google" id="google-connect-link">Google</oauth:connect></li>
			        <li class="live"><oauth:connect provider="live">Live</oauth:connect></li>
			        <li class="yahoo"><oauth:connect provider="yahoo">Yahoo</oauth:connect></li>
			      </ul>
			    </div>
		  </div>
	    </div>
	</body>
</html>