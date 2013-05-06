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
	     			<div class="master-filter-content">
	     				<h4 class="filter-list-title">List of Tracking Numbers</h4>
	     				<form id="form_map_filter">
		     				<ul class="filter-list-ul">
		     					<li class="filter-list-li-all"><input id="f_ALL" type="checkbox" value="ALL" class="filter-checkbox" checked/><label id="f_ALL_label" for="f_ALL" class="filter-checkbox-label">All</label></li>
		     					<li><input id="f_123456789102" type="checkbox" value="123456789102" class="filter-checkbox"/><label id="f_123456789102_label" for="f_123456789102" class="filter-checkbox-label">123456789102</label></li>
		     					<li><input id="f_223456789122" type="checkbox" value="223456789122" class="filter-checkbox"/><label id="f_223456789122_label" for="f_223456789122" class="filter-checkbox-label">223456789122</label></li>
		     				</ul>
	     				</form>
	     			</div>
	     		</div>
	     	</section>
	    </div>
	    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'dashboard/dashboard.js')}"></script>
	</body>
</html>
