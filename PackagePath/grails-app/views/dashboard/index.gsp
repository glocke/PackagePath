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
							{ "lightness": -45 }
 						]
 					},
 					{
 						"featureType": "landscape",
 					    "stylers": [
							{ "lightness": 3 }
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
 					center: new google.maps.LatLng(43.0731, -89.4011),
 					styles: map_style,
 					mapTypeControl: false,
 		            zoom: 5
				};
 	 		          
				var map = new google.maps.Map(document.getElementById('map'), opt);

				var flightPlanCoordinates = [
					new google.maps.LatLng(35.65, -105.15),
					new google.maps.LatLng(37.77, -99.97),
				    new google.maps.LatLng(41.90, -87.65)
				];
				var flightPath = new google.maps.Polyline({
				    path: flightPlanCoordinates,
				    strokeColor: '#5c3977',
				    strokeOpacity: 1.0,
				    strokeWeight: 2
				});

				flightPath.setMap(map);

				var contentString = '<div class="maps-info">'+
				    '<h2>Testing</h2>'+
				    '<p>The package is in Chicago</p>'+
				    '</div>';

				var infowindow = new google.maps.InfoWindow({
				    content: contentString
				});
	
				var marker = new google.maps.Marker({
				    position: new google.maps.LatLng(41.90, -87.65),
				    map: map,
				    title:"Chicago, IL"
				});

			google.maps.event.addListener(marker, 'click', function() {
			  infowindow.open(map,marker);
			});
			}

			google.maps.event.addDomListener(window, 'load', initialize);
    	</script>
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
	            <ul class="nav">
	              <li><a class="nav-icon nav-home" href="#"></a></li>
	              <li><a class="nav-icon nav-refresh" href="#about"></a></li>
	              <li><a class="nav-icon nav-list" href="#contact"></a></li>
	            </ul>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
		
	    <div style="margin-top:40px;">
			<section class="calendar">
				<ul class="calendar-ul">
					<li>
						<div class="calendar-day-content calendar-day-first calendar-day-content-today">
							<div class="calendar-day-title">APRIL 7</div>
						</div>
					</li>
					<li>	
						<div class="calendar-day-content">
							<div class="calendar-day-title">APRIL 8</div>
							<div class="calendar-day-arrival-content">
								<ul class="calendar-day-ul">
									<li><div class="calendar-day-arrival fedex-brand-background"></div></li>
									<li><div class="calendar-day-arrival ups-brand-background"></div></li>
								</ul>
							</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<div class="calendar-day-title">APRIL 9</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<div class="calendar-day-title">APRIL 10</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<div class="calendar-day-title"">APRIL 11</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<div class="calendar-day-title"">APRIL 12</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content">
							<div class="calendar-day-title"">APRIL 13</div>
						</div>
					</li>
				</ul>
			</section>
			<section class="map-section">
				<div class="map-section-filter"></div>
				<div class="map-section-filter-buttons">
					<div class="btn-group">
						<button class="btn btn-mini">Inbound</button>
						<button class="btn btn-mini">Outbound</button>
					</div>
				</div>
	     		<div id="map" class="map-canvas"></div>
	     		<div id="filter" class="master-map-filter">
	     			<div class="master-filter-header">
	     				<h3 class="master-filter-title">Filter Tracking Numbers</h3>
	     			</div>
	     			<div class="master-filter-content"></div>
	     		</div>
	     	</section>
	    </div>
	</body>
</html>
