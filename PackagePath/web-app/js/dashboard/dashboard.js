var pp = pp || {};

/*
 * Package Path dashboard
 */
pp.dash = function(){
	
	/*
	 * private variables
	 */
	var _packages;				//map of packages ['tracking #'] = package
	var _map;					//reference to the google map
	var _paths = [];			//all of the existing paths ['tracking #'] = path
	var _markers = [];			//all of the markers ['tracking #']
	var _queueAdds = [];		//all of the tracking numbers that need to be added to the map
	var _queueDeletes = [];	//all of the tracking numbers that need to be removed from the map
	
	/*
	 * jQuery variables
	 */
	var _$form_map_filter;		//jQuery reference to the form
	
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
			
			/*
			 * Keep track of the packages
			 */
			_packages[json[i].trackingNumber] = json[i];
			
			/*
			 * Draw the filter
			 */
			_drawFilter();
		});
		
		/*
		 * Bind filters
		 */
		_bindFilteringEvents();
	}
	
	/**
	 * Draw the filter section of the screen
	 */
	function _drawFilter(){
		
		var filtersource = {};
		
		var destinations = [];
		var o1 = {};
		o1.name = "in";
		o1.label = "Inbound";
		destinations.push(o1);
		
		var o2 = {};
		o2.name = "out";
		o2.label = "Outbound";
		destinations.push(o2);
		
		filtersource.destination = destinations;
		
		var carriers = [];
		var o3 = {};
		o3.name = "fedex";
		o3.label = "FedEx";
		carriers.push(o3);
		
		var o4 = {};
		o4.name = "ups";
		o4.label = "UPS";
		carriers.push(o4);
		
		filtersource.carrier = carriers;
		
		var days = [];
		var o5 = {};
		o5.name = "05122013";
		o5.label = "May 12, 2013";
		days.push(o5);
		
		filtersource.days = days;
		
		var numbers = [];
		var o6 = {};
		o6.name = "123456789102";
		o6.label = "123456789102";
		numbers.push(o6);
		
		var o7 = {};
		o7.name = "223456789122";
		o7.label = "223456789122";
		numbers.push(o7);
		
		filtersource.numbers = numbers;
		
		console.debug("%o", filtersource);
		
		/**
		* Parse an existing JSON data structure with a XSLT template
		*/
		Stapling.parse(filtersource, '/PackagePath/js/stapling/templates/filter.xslt', function (xml) {

			if (console && console.log) {
				console.log("XML-document: ", xml);
			}

			_$form_map_filter.html(this);
		});
	}
	
	/**
	 * Bind the events for the filter
	 */
	function _bindFilteringEvents(){
		
		//reset the queues
		_queueAdds = [];		
		_queueDeletes = [];
		
		
		/*
		 * Direction
		 */
		_$form_map_filter.on('click', "input.filter-direction", function(){

		});
		
		/*
		 * Carrier
		 */
		_$form_map_filter.on('click', "input.filter-carrier", function(){
			var $this = $(this);
			
			//get the carrier
			var carrier = $this.val().slice()[1];
			
			//iterate the packages
			for (var key in _packages) {
				
				//does the carrier match?
				if(_packages.hasOwnProperty(key) && _packages[key].shippingService === carrier){
					if($this.prop('checked')){
						_queueAdds.push(id);
					}
					else{
						_queueDeletes.push(id);
					}
				}
			}
			_filterPackages();
		});
		
		/*
		 * Days
		 */
		_$form_map_filter.on('click', "input.filter-day", function(){
			var $this = $(this);
			
			//get the unformatted date
			var endDate = $this.val().slice()[1];
			endDate = endDate.substring(0,2) + '/' + endDate.substring(2, 4) + "/" + endDate.substring(4);
			
			//iterate the packages
			for (var key in _packages) {
				
				//does the date match?
				if(_packages.hasOwnProperty(key) && _packages[key].endTransitDate === endDate){
					if($this.prop('checked')){
						_queueAdds.push(id);
					}
					else{
						_queueDeletes.push(id);
					}
				}
			}
			_filterPackages();
		});
		
		/*
		 * Tracking Number
		 */
		_$form_map_filter.on('click', "input.filter-number", function(){
			var $this = $(this);
			
			//get the id
			var id = $this.val().slice()[1];
			if($this.prop('checked')){
				_queueAdds.push(id);
			}
			else{
				_queueDeletes.push(id);
			}
			
			_filterPackages();
		});
	}
	
	/**
	 * This method will be responsible for filtering the packages on the screen
	 */
	function _filterPackages(){
		for(var key in _queueAdds){
			pp.dash.drawPath(_packages[key], true);
		}
		for(var key in _queueDeletes){
			pp.dash.drawPath(_packages[key], false);
		}
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
		/**
		 * Window onload function
		 */
		load: function(){
			
			/*
			 * Create the initial google map
			 */
			var map = pp.dash.createMap();
		},
		
		/**
		 * Document ready function
		 */
		documentReady: function(){
			
			//set the form reference
			_$form_map_filter = $("#form_map_filter");
			
			/*
			 * Bind the filtering events.  We shouldn't have to bind after each ajax call if
			 * we bind to the form and not the input
			 */
			_bindFilteringEvents();
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

 			_paths['123456789102'] = flightPath;
 			flightPath.setMap(_map);
 			
 			/*
 			 * Make another one
 			 */
 			var upsCoordinates = [
 				new google.maps.LatLng(34.05, -118.24),
 				new google.maps.LatLng(39.74, -104.98)
 			];
 			var upsPath = new google.maps.Polyline({
 			    path: upsCoordinates,
 			    strokeColor: '#5C3317',
 			    strokeOpacity: 1.0,
 			    strokeWeight: 2
 			});
 			
 			_paths['223456789122'] = upsPath;
 			upsPath.setMap(_map);
		},
		
		/**
		 * Draw the path
		 * 
		 * @param packobj - the object that needs to be drawn
		 * @param add - boolean to decide if we are adding or removing
		 */
		drawPath: function(packobj, add){
			
			if(add){
				/*
				 * Draw the polyline
				 */
				
				/*
				 * Draw the markers
				 */
				pp.dash.setMarkers(packobj);
			}
			else{
				/*
				 * Clear the polyline
				 */
				_paths[packobj.trackingNumber].setMap(null);
				
				/*
				 * Clear the markers
				 */
				_markers[packobj.trackingNumber].setMap(null);
			}
		},
		
		/**
		 * Set the package path's markers on the screen
		 * 
		 *  @param packobj - the object that needs to be drawn
		 */
		setMarkers: function(packobj){
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
			
			//add the marker
			_markers[packobj.trackingNumber] = marker;
	
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

/*
 * jQuery document ready
 */
$(document).ready(function(){
	pp.dash.documentReady();
});