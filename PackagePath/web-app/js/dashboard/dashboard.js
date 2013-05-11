var pp = pp || {};

/*
 * Package Path dashboard
 */
pp.dash = function(){
	
	/*
	 * private variables
	 */
	var _map_style = [
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
	
	/*
	 * private functions
	 */
	
	/*
	 * API
	 */
	return {
		
		/*
		 * public variables
		 */
		
		/*
		 * public functions
		 */
		load: function(){
			
			/*
			 * Create the initial google map
			 */
			var map = pp.dash.createMap();
			
			/*
			 * set package path
			 */
			pp.dash.setPackagePath(map);

			/*
			 * set the markers
			 */
			pp.dash.setMarkers(map);
		},
		
		/**
		 * Create the initial google map
		 */
		createMap: function(){
			var opt = {
	       		mapTypeId: google.maps.MapTypeId.ROADMAP,
				center: new google.maps.LatLng(43.0731, -89.4011),
				styles: _map_style,
				mapTypeControl: false,
	            zoom: 5
			};
	 		          
			return new google.maps.Map(document.getElementById('map'), opt);
		},
		
		/**
		 * Draw the package path's on the screen
		 * 
		 * @param map - google map reference
		 */
		setPackagePath: function(map){
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
		},
		
		/**
		 * Set the package path's markers on the screen
		 * 
		 *  @param map = google map reference
		 */
		setMarkers: function(map){
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
			    icon: "/PackagePath/images/iconic/blue/map_pin_fill_20x32.png",
			    title:"Chicago, IL"
			});
	
			google.maps.event.addListener(marker, 'click', function() {
			  infowindow.open(map,marker);
			});
		}
	}
}();

/*
 * jQuery window load
 */
$(window).load(pp.dash.load);