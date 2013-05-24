var pp = pp || {};

/*
 * Package Path dashboard
 */
pp.dash = function(){
	
	/*
	 * private variables
	 */
	var _packages;
	var _map;
	var _flightPaths = [];
	
	//google map styles
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
	/**
	 * This method will update the map of packages
	 * 
	 * @param json
	 */
	function _updatePackages(json){
		
		_packages = new Object();
		
		/*
		 * Iterate the response
		 */
		$.each(json, function(i, item) {
			_packages[json[i].shippingService] = json[i];
		});
	}
	
	/**
	 * This method will be responsible for filtering the packages on the screen
	 */
	function _filterPackages(){
		
	}
	
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
		},
		
		/**
		 * Create the initial google map
		 */
		createMap: function(){
			
			$.ajax({
				  url: "/PackagePath/package/testTracker?type=ups&trackingNumber=1Z12345E1512345676",
				  beforeSend: function ( xhr ){},
				  dataType: "json"
			}).done(function (data, textStatus, jqXHR) {
				
				/*
				 * Update the packages
				 */
				_updatePackages(data);
				
				/*
				 * Filter the packages
				 */
				_filterPackages();
				
				/*
				 * Initial map drawing
				 */
				var opt = {
		       		mapTypeId: google.maps.MapTypeId.ROADMAP,
					center: new google.maps.LatLng(43.0731, -89.4011),
					styles: _map_style,
					mapTypeControl: false,
		            zoom: 5
				};
		 		          
				_map = new google.maps.Map(document.getElementById('map'), opt);
				
				/*
				 * set package path
				 */
				pp.dash.setPackagePath();

				/*
				 * set the markers
				 */
				pp.dash.setMarkers();
				
			}).fail(function (){
				
			}).always(function (){
				
			});
		},
		
		/**
		 * Draw the package path's on the screen
		 * 
		 * @param map - google map reference
		 */
		setPackagePath: function(){
			
			/*
			 * Get the flight paths
			 */
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

 			_flightPaths.push(flightPath);
 			flightPath.setMap(_map);
		},
		
		/**
		 * Set the package path's markers on the screen
		 * 
		 *  @param map = google map reference
		 */
		setMarkers: function(){
			var contentString = '<div class="popover-title">Testing</div>'+
		    '<div class="popover-content">'+
		    '<p>The package is in Chicago</p>'+
		    '</div>';

			var infowindow = new google.maps.InfoWindow({
			    content: contentString
			});
	
			var marker = new google.maps.Marker({
			    position: new google.maps.LatLng(41.90, -87.65),
			    map: _map,
			    icon: "/PackagePath/images/iconic/blue/map_pin_fill_20x32.png",
			    title:"Chicago, IL"
			});
	
			google.maps.event.addListener(marker, 'click', function() {
			  infowindow.open(_map,marker);
			});
		}
	}
}();

/*
 * jQuery window load
 */
$(window).load(pp.dash.load);