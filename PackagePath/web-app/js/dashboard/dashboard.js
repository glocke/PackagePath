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
	var _geocoder;				//reference to the geocoder
	var _paths = [];			//all of the existing paths ['tracking #'] = path
	var _markers = [];			//all of the markers ['tracking #']
	var _f_Destination;			//map of the destination
	var _f_Days;				//map of the days
	var _f_Carrier;				//map of the carriers
	var _f_Carriers_to_Days;	//map of the carriers to days ['fedex', [date1, date2, etc]]
	
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
	
	var _lineColor = {
		'ups': "#5C3317",
		'fedex': "#5c3977"
	}
	
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
		_f_Days = new Object();			// ['05222013', [number1, number2, etc]]
		_f_Carriers_to_Days = new Object();	//['fedex', [date1, date2, etc]]
		
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
			var unformattedDay = pack_obj.endTransitDate.replace(/\//g,'')
			if (unformattedDay in _f_Days) {
				
				//get the existing list
				days_nums = _f_Days[unformattedDay];
			}
			//add to the list
			days_nums.push(tracking_number);
			
			//set the list of tracking numbers for that day
			_f_Days[unformattedDay] = days_nums;
			
			/*
			 * Map the carriers to the days
			 */
			var carrier_to_days = [];
			if (pack_obj.shippingService in _f_Carriers_to_Days) {
				
				//get the existing list
				carrier_to_days = _f_Carriers_to_Days[pack_obj.shippingService];
			}
			//add to the list
			carrier_to_days.push(pack_obj.endTransitDate.replace(/\//g,''));
			
			//set the list of days to the carrier
			_f_Carriers_to_Days[pack_obj.shippingService] = carrier_to_days;
			
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
		var unformattedDate;
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
			_$form_map_filter.html(this);
		});
	}
	
	/**
	 * Bind the events for the filter
	 */
	function _bindFilteringEvents(){
		
		/*
		 * Regular UI fun
		 */
		_$form_map_filter.on('click', "input:checkbox", function(){
			var $this = $(this);
			//adding or removing?
			if($this.prop('checked')){
				//add the class
				$this.parent().addClass("active");
			}
			else{
				//remove the class
				$this.parent().removeClass("active");
			}
		});
		
		/*
		 * Direction
		 */
		_$form_map_filter.on('click', "input.filter-direction", function(){

			//_updateCarrierFilterDisplay(_f_Carrier[carrier], $this);
		});
		
		/*
		 * Carrier
		 */
		_$form_map_filter.on('click', "input.filter-carrier", function(){
			var $this = $(this);
			
			//get the carrier
			var carrier = $this.val();

			//filter the packages that are displayed on the map
			_filterPackages(_f_Carrier[carrier], $this);
			
			
			//if we are going to disable days, we need to get all of the existing carrier numbers that are not checked
			if(!$this.prop('checked')){
				//get all of the checked carrier tracking numbers
				var carrier_keys = Object.keys(_f_Carrier);
				var carrier_key;
				var $carrier_key;
				var carrier_days = [];
				var idx;
				for(idx = 0; idx < carrier_keys.length; idx++) {
					carrier_key = carrier_keys[idx];
					$carrier_key = $("#f_" + carrier_key);

					if($carrier_key.prop('checked')){
						carrier_days = carrier_days.concat(_f_Carriers_to_Days[carrier_key]);
					}
				}
				//pass in all of the checked carrier days that way we can disable the day if none are no longer used
				_disableDaysFromCarriers(carrier_days, $this);
			}
			else{
				//update the filter display
				_enableDaysFromCarriers(_f_Carriers_to_Days[carrier], $this);
			}
			
			_updateTrackingNumbersFilterDisplay(_f_Carrier[carrier], $this);
		});
		
		/*
		 * Days
		 */
		_$form_map_filter.on('click', "input.filter-day", function(){
			var $this = $(this);
			
			//get the unformatted date
			var endDate = $this.val();

			//filter the packages that are displayed on the map
			_filterPackages(_f_Days[endDate], $this);
			
			//update the filter display
			_updateTrackingNumbersFilterDisplay(_f_Days[endDate], $this);
		});
		
		/*
		 * Tracking Number
		 */
		_$form_map_filter.on('click', "input.filter-number", function(){
			var $this = $(this);
			
			//get the id
			var id = $this.val();
			var filterArray = [];
			filterArray.push(id);
			
			_filterPackages(filterArray, $this);
		});
	}
	
	/**
	 * This method will be responsible for filtering the packages on the screen
	 */
	function _filterPackages(num_array, $this){
		var idx;
		for(idx = 0; idx < num_array.length; idx++) {
			pp.dash.drawPath(_packages[num_array[idx]], $this);
		}
	}
	
	/**
	 * Draw the path on the map based on the package object
	 */
	function _drawPolyline(package_obj){
		
		//these are asynchronous and need to be changed so that order is maintained... nastiness but i'm not sure what the h to do right now
		var flightPlanCoordinates = [];
		_geocoder.geocode( { 'address': package_obj.startZip}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				flightPlanCoordinates.push(results[0].geometry.location);
				
				_geocoder.geocode( { 'address': package_obj.currentZip}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						flightPlanCoordinates.push(results[0].geometry.location);
						
						_geocoder.geocode( { 'address': package_obj.endZip}, function(results, status) {
							if (status == google.maps.GeocoderStatus.OK) {
								flightPlanCoordinates.push(results[0].geometry.location);
								
								var flightPath = new google.maps.Polyline({
								    path: flightPlanCoordinates,
								    strokeColor: _lineColor[package_obj.shippingService],
								    strokeOpacity: 1.0,
								    strokeWeight: 3
								});
						
								_paths[package_obj.trackingNumber] = flightPath;
								flightPath.setMap(_map);
							}
						});
					}
				});
			}
			else{
				alert('wtf');
			}
		});
	}
	
	/**
	 * Update the days display when a carrier is unchecked
	 * 
	 * @param days_array - all days that are associated to a checked carrier
	 * @param $this
	 */
	function _disableDaysFromCarriers(days_array, $this){
		
		//iterate all days and see if that day should be checked
		var day_keys = Object.keys(_f_Days);
		var $f_day;
		var day_key;
		for(idx = 0; idx < day_keys.length; idx++) {
			day_key = day_keys[idx];
			$f_day = $("#f_" + day_key);
			
			//if its no longer part of the checked days, disable it
			if($.inArray(day_key, days_array) < 0){
				$f_day.prop('checked', false);
				$f_day.prop('disabled', true);
				$f_day.parent().addClass("disabled");
			}
		}
	}
	
	/**
	 * Update the days filter display based on the tracking numbers
	 * 
	 * @param days_array - the days ['05/22/2013', etc]
	 * @param $this - are we adding a tracking number or not?
	 */
	function _enableDaysFromCarriers(days_array, $this){

		/*
		 * Find the unique days for filtering
		 */
		var idx;

		var $f_day;
		var day_key;
		for(idx = 0; idx < days_array.length; idx++) {
			day_key = days_array[idx];
			$f_day = $("#f_" + day_key);
			
			//set for enabling
			$f_day.prop('disabled', false);
			$f_day.prop('checked', true);
			$f_day.parent().removeClass("disabled").addClass("active");
		}
	}
	
	/**
	 * Update the tracking Numbers filter display based on the tracking numbers
	 * 
	 * @param num_array - tracking numbers
	 * @param $this - are we adding a tracking number or not?
	 */
	function _updateTrackingNumbersFilterDisplay(num_array, $this){
		var addingTracking = $this.prop("checked");
		var idx;
		var $tn;
		for(idx = 0; idx < num_array.length; idx++) {
			$tn = $("#f_" + num_array[idx]);
			if(addingTracking){
				$tn.parent().removeClass('disabled');
				$tn.prop('checked', true);
				$tn.prop('disabled', false);
			}
			else{
				$tn.parent().addClass('disabled');
				$tn.prop('checked', false);
				$tn.prop('disabled', true);
			}
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
			
			//initialize the geocoder
			_geocoder = new google.maps.Geocoder();
			
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
				  url: "/PackagePath/dashboard/retrievePackages",
				  beforeSend: function ( xhr ){},
				  dataType: "json"
			}).done(function (data, textStatus, jqXHR) {
				
				/*
				 * Update the packages
				 */
				_updatePackages(data);
				
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
		 */
		setPackagePath: function(){
			
			//draw all of the package paths
			var keys = Object.keys(_packages);
			var idx;
			for(idx = 0; idx < keys.length; idx++) {
				
				/*
				 * Draw the polyline
				 */
				_drawPolyline(_packages[keys[idx]]);
			}
		},
		
		/**
		 * Draw the path
		 * 
		 * @param packobj - the object that needs to be drawn
		 * @param $this - the jquery object that was clicked
		 */
		drawPath: function(packobj, $this){
			
			if($this.prop('checked')){
				/*
				 * Assuming the tracking number is already drawn.  Just put it back on the map
				 */
				_paths[packobj.trackingNumber].setMap(_map);
				
				/*
				 * Draw the markers
				 */
				//pp.dash.setMarkers(packobj);
			}
			else{
				/*
				 * Clear the polyline
				 */
				_paths[packobj.trackingNumber].setMap(null);
				
				/*
				 * Clear the markers
				 */
				//_markers[packobj.trackingNumber].setMap(null);
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