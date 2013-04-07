<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to PackagePath</title>
		
		<script>
			function initialize() {
  				var mapOptions = {
    				zoom: 8,
    				center: new google.maps.LatLng(-34.397, 150.644),
    				mapTypeId: google.maps.MapTypeId.ROADMAP
  				};

  				var map = new google.maps.Map(document.getElementById('map'), mapOptions);
			}

			function loadScript() {
  				var script = document.createElement('script');
  				script.type = 'text/javascript';
  				script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&callback=initialize';
  				document.body.appendChild(script);
			}

			window.onload = loadScript;
    	</script>
    	
    	<style type="text/css">
    		.map-section{
    			height: 400px;
    			width: 100%;
    		}
    		.map-canvas{
    			height: 100%;
    		}
    	</style>
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
	          <a class="brand" href="#">Package<span class="brand-blue">Path</span></a>
	          <div class="nav-collapse collapse">
	            <ul class="nav">
	              <li class="active"><a href="#">Home</a></li>
	              <li><a href="#about">About</a></li>
	              <li><a href="#contact">Contact</a></li>
	            </ul>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
		
	    <div class="container" style="margin-top:50px;">
			<section class="calendar"></section>
			<section class="map-section">
	     		<div id="map" class="map-canvas"></div>
	     	</section>
	    </div>
	</body>
</html>
