<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to PackagePath</title>
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
		<script>
			function initialize() {

 		        var map_style = [
 		         	{
						"featureType": "water",
 						"stylers": [
							{ "lightness": -39 }
 						]
 					},
 					{
 						"featureType": "landscape",
 					    "stylers": [
							{ "lightness": 40 }
 						]
 					},
 					{
 					    "featureType": "road",
 					    "stylers": [
 							{ "visibility": "off" }
 						]
 					}
				];

 		       	var opt = {
 		       		mapTypeId: google.maps.MapTypeId.ROADMAP,
 					center: new google.maps.LatLng(-34.397, 150.644),
 					styles: map_style,
 					mapTypeControl: false,
 		            zoom: 8
				};
 	 		          
				var map = new google.maps.Map(document.getElementById('map'), opt);        
			}

			google.maps.event.addDomListener(window, 'load', initialize);
    	</script>
    	
    	<style type="text/css">
    		.navbar-fixed-top .navbar-inner, .navbar-static-top .navbar-inner {
    			box-shadow: 0 1px 10px #BABABA;
    			border-bottom: 1px solid #ABABAB;
			}
    		.map-section{
    			height: 400px;
    			width: 100%;
    		}
    		.map-canvas{
    			height: 100%;
    		}
    		.calendar-ul{
    			margin: 0;
    		}
    		.calendar-ul li{
    			display: inline;
    			list-style:none;
    		}
    		.calendar-day-first{
    			border-left: 1px solid #ABABAB;
    		}
    		.calendar-day-content{
				filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#e5e5e5',GradientType=1 ); /* IE6-9 fallback on horizontal gradient */
    			border-collapse: collapse;
    			border-right: 1px solid #ABABAB;
    			box-shadow: 0 0 20px #BDBDBD inset;
    			display: inline-block;
    			height: 100px; 
    			margin-right: -4px;
    			text-align: center; 
    			width: 200px; 
    		}
    		.calendar-day-content-today{
    			background: #efefef; /* Old browsers */
				background: -moz-radial-gradient(center, ellipse cover, #efefef 0%, #cdcdcd 100%); /* FF3.6+ */
				background: -webkit-gradient(radial, center center, 0px, center center, 100%, color-stop(0%,#efefef), color-stop(100%,#cdcdcd)); /* Chrome,Safari4+ */
				background: -webkit-radial-gradient(center, ellipse cover, #efefef 0%,#cdcdcd 100%); /* Chrome10+,Safari5.1+ */
				background: -o-radial-gradient(center, ellipse cover, #efefef 0%,#cdcdcd 100%); /* Opera 12+ */
				background: -ms-radial-gradient(center, ellipse cover, #efefef 0%,#cdcdcd 100%); /* IE10+ */
				background: radial-gradient(ellipse at center, #efefef 0%,#cdcdcd 100%); /* W3C */
				filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#efefef', endColorstr='#cdcdcd',GradientType=1 ); /* IE6-9 fallback on horizontal gradient */
    		}
    		.calendar-day-title{
    			color: rgb(85, 85, 85);
    			display: inline-block; 
    			font-size: 11px; 
    			margin: 15px 0 0; 
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
		
	    <div class="container" style="margin-top:40px;">
			<section class="calendar">
				<ul class="calendar-ul">
					<li>
						<div class="calendar-day-content calendar-day-first calendar-day-content-today">
							<span class="calendar-day-title">APRIL 7</span>
						</div>
					</li>
					<li>	
						<div class="calendar-day-content">
							<span class="calendar-day-title">APRIL 8</span>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<span class="calendar-day-title">APRIL 9</span>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<span class="calendar-day-title">APRIL 10</span>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<span class="calendar-day-title"">APRIL 11</span>
						</div>
					</li>
				</ul>
			</section>
			<section class="map-section">
	     		<div id="map" class="map-canvas"></div>
	     	</section>
	    </div>
	</body>
</html>
