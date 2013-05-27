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
	var _queueDeletes = [];		//all of the tracking numbers that need to be removed from the map
	var _f_Destination;			//map of the destination
	var _f_Days;				//map of the days
	var _f_Carrier;				//map of the carriers
	
	/*
	 * jQuery variables
	 */
	var _$form_map_filter;		//jQuery reference to the form
	
	//english messages
	var EnglishMessage = {
		'label.fedex': "FedEx",
		'label.ups': "UPS",
		'label.usps': "USPS"
	};
	
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
		_f_Carrier = new Object();		// ['ups', [number1, number2, etc]]
		_f_Days = new Object();			// ['05/22/2013', [number1, number2, etc]]
		
		/*
		 * Iterate the response
		 */
		$.each(json, function(i, item) {
			
			/*
			 * Keep track of the packages
			 */
			var pack_obj = json[i];
			var tracking_number = pack_obj.trackingNumber;
			_packages[tracking_number] = pack_obj;
			
			/*
			 * Find the unique carriers for filtering
			 */
			var carrier_nums = [];
			if (pack_obj.shippingService in _f_Carrier) {
				
				//get the existing list
				carrier_nums = _f_Carrier[pack_obj.shippingService];
			}
			//add to the list
			carrier_nums.push(tracking_number);
			
			//set the list of tracking numbers for that carrier
			_f_Carrier[pack_obj.shippingService] = carrier_nums;
			
			/*
			 * Find the unique days for filtering
			 */
			var days_nums = [];
			if (pack_obj.endTransitDate in _f_Days) {
				
				//get the existing list
				days_nums = _f_Days[pack_obj.endTransitDate];
			}
			//add to the list
			days_nums.push(tracking_number);
			
			//set the list of tracking numbers for that day
			_f_Days[pack_obj.endTransitDate] = days_nums;
			
		});
		
		/*
		 * Draw the filter
		 */
		_drawFilter();
		
		/*
		 * Bind filters
		 */
		_bindFilteringEvents();
	}
	
	/**
	 * Draw the filter section of the screen
	 */
	function _drawFilter(){
		
		//filter source
		var filtersource = {};
		
		//destinations
		var destinations = [];
		var o1 = {};
		o1.name = "in";
		o1.label = "Inbound";
		destinations.push(o1);
		
		var o2 = {};
		o2.name = "out";
		o2.label = "Outbound";
		destinations.push(o2);
		
		//set the destinations
		filtersource.destination = destinations;
		
		var idx;
		
		//carriers
		var carriers = [];
		var carrier_obj = {};
		var carrier_keys = Object.keys(_f_Carrier);
		for(idx = 0; idx < carrier_keys.length; idx++) {
			carrier_obj = {};
			carrier_obj.name = carrier_keys[idx];
			carrier_obj.label = pp.dash.getText("label." + carrier_keys[idx]);
			carriers.push(carrier_obj);
		}
		filtersource.carrier = carriers;
		
		//days
		var days = [];
		var days_obj = {};
		var day_keys = Object.keys(_f_Days);
		for(idx = 0; idx < day_keys.length; idx++) {
			days_obj = {};
			days_obj.name = day_keys[idx];
			days_obj.label = day_keys[idx];
			days.push(days_obj);
		}
		filtersource.days = days;
		
		//tracking numbers
		var nums = [];
		var num_obj = {};
		var num_keys = Object.keys(_packages);
		for(idx = 0; idx < num_keys.length; idx++) {
			num_obj = {};
			num_obj.name = num_keys[idx];
			num_obj.label = num_keys[idx];
			nums.push(num_obj);
		}
		filtersource.numbers = nums;
		
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
				 * TESTING!
				 */
				var datas = [
				    	{"class":"packagepath.Package",
				    		"id":null,
				    		"currentPackageStatus":"",
				    		"currentZip":"",
				    		"endTransitDate":"05/12/2013",
				    		"endZip":"",
				    		"estimatedEndTransitDate":null,
				    		"inTransit":true,
				    		"shippingService":"ups",
				    		"startTransitDate":null,
				    		"startZip":"",
				    		"trackingNumber":"123456789102",
				    		"user":null},
				    		{"class":"packagepath.Package",
				    		"id":null,
				    		"currentPackageStatus":"",
				    		"currentZip":"",
				    		"endTransitDate":"05/12/2013",
				    		"endZip":"",
				    		"estimatedEndTransitDate":null,
				    		"inTransit":true,
				    		"shippingService":"fedex",
				    		"startTransitDate":null,
				    		"startZip":"",
				    		"trackingNumber":"223456789122",
				    		"user":null}
				    	];
				
				/*
				 * Update the packages
				 */
				_updatePackages(datas);
				
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
		},
		
		/**
		 * Get text for this screen via a key lookup
		 * 
		 * @param key
		 */
		getText: function (key) {
			//eventually check to see which language we should use
			return EnglishMessage[key];
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