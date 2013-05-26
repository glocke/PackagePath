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
	              <li><a class="nav-icon nav-home" href="${createLink(action: 'Home',controller:'Home')}" title="Home"></a></li>
	              <li><a class="nav-icon nav-refresh" href="#" title="Refresh"></a></li>
	            </ul>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
		
	    <div style="margin-top:40px;">
			<section class="calendar">
				<ul class="calendar-ul">
					<li>
						<div class="calendar-day-content calendar-day-first calendar-day-disabled">
							<div class="calendar-day-title">MAY 11</div>
						</div>
					</li>
					<li>	
						<div class="calendar-day-content">
							<div class="calendar-day-title">MAY 12</div>
							<div class="calendar-day-arrival-content">
								<ul class="calendar-day-ul">
									<li><div class="calendar-day-arrival fedex-brand-background"></div></li>
									<li><div class="calendar-day-arrival ups-brand-background"></div></li>
								</ul>
							</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content calendar-day-disabled">
							<div class="calendar-day-title">MAY 13</div>
							<div class="calendar-day-arrival-content">
								<ul class="calendar-day-ul">
									<li></li>
									<li><div class="calendar-day-arrival ups-brand-background"></div></li>
								</ul>
							</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content calendar-day-disabled">
							<div class="calendar-day-title">MAY 14</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content calendar-day-disabled">
							<div class="calendar-day-title">MAY 15</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content calendar-day-disabled">
							<div class="calendar-day-title">MAY 16</div>
						</div>
					</li>
					<li>
						<div class="calendar-day-content calendar-day-disabled">
							<div class="calendar-day-title">MAY 17</div>
						</div>
					</li>
				</ul>
			</section>
			<section class="map-section">
				<div class="map-section-filter map-section-filter-top"></div>
				<div class="map-section-filter map-section-filter-bottom"></div>
	     		<div id="map" class="map-canvas"></div>
	     		<div id="filter" class="master-map-filter">
	     			<div class="master-filter-content">
	     				<form id="form_map_filter"></form>
	     			</div>
	     		</div>
	     	</section>
	    </div>
	    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
	    <script type="text/javascript" src="${resource(dir: 'js', file: 'stapling/stapling.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'dashboard/dashboard.js')}"></script>
	</body>
</html>
