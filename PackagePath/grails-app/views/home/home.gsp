<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to PackagePath</title>
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
			
			<section>
				<h2>This is our new home page.</h2>
	     	</section>
	     	<oauth:connect provider="google">Connect to Google</oauth:connect>
	    </div>
	</body>
</html>
