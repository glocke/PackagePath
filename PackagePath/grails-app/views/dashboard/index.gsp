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
	     				<form id="form_map_filter">
	     					<ul class="master-filter-ul">
	     						<li class="master-filter-li">
	     							<div class="master-filter-list-content">
		     							<h4 class="filter-list-title">Inbound / Outbound</h4>
		     							<ul class="filter-list-ul">
					     					<li><label id="f_IN_label" for="f_IN" class="btn active filter-checkbox-label"><input id="f_IN" type="checkbox" value="IN" class="filter-checkbox filter-direction" checked/>Inbound</label></li>
					     					<li><label id="f_OUT_label" for="f_OUT" class="btn filter-checkbox-label"><input id="f_OUT" type="checkbox" value="OUT" class="filter-checkbox filter-direction"/>Outbound</label></li>
					     				</ul>
									</div>
	     						</li>
	     						<li class="master-filter-li">
	     							<div class="master-filter-list-content">
	     								<h4 class="filter-list-title">Carrier</h4>
	     								<ul class="filter-list-ul">
					     					<li><label id="f_fedex_label" for="f_fedex" class="btn filter-checkbox-label"><input id="f_fedex" type="checkbox" value="fedex" class="filter-checkbox filter-carrier"/>FedEx</label></li>
					     					<li><label id="f_ups_label" for="f_ups" class="btn filter-checkbox-label"><input id="f_ups" type="checkbox" value="ups" class="filter-checkbox filter-carrier" checked/>UPS</label></li>
					     					<li><label id="f_usps_label" for="f_usps" class="btn active filter-checkbox-label"><input id="f_usps" type="checkbox" value="usps" class="filter-checkbox filter-carrier" checked/>USPS</label></li>
					     				</ul>
	     							</div>
	     						</li>
	     						<li class="master-filter-li">
	     							<div class="master-filter-list-content">
	     								<h4 class="filter-list-title">Days</h4>
	     								<ul class="filter-list-ul">
					     					<li><label id="f_05112013_label" for="f_05112013" class="btn filter-checkbox-label"><input id="f_05112013" type="checkbox" value="05112013" class="filter-checkbox filter-day"/>May 11, 2013</label></li>
					     					<li><label id="f_05122013_label" for="f_05122013" class="btn active filter-checkbox-label"><input id="f_05122013" type="checkbox" value="05122013" class="filter-checkbox filter-day" checked/>May 12, 2013</label></li>
					     					<li><label id="f_05132013_label" for="f_05132013" class="btn filter-checkbox-label"><input id="f_05132013" type="checkbox" value="05132013" class="filter-checkbox filter-day"/>May 13, 2013</label></li>
					     					<li><label id="f_05142013_label" for="f_05142013" class="btn filter-checkbox-label"><input id="f_05142013" type="checkbox" value="05142013" class="filter-checkbox filter-day"/>May 14, 2013</label></li>
					     					<li><label id="f_05152013_label" for="f_05152013" class="btn filter-checkbox-label"><input id="f_05152013" type="checkbox" value="05152013" class="filter-checkbox filter-day"/>May 15, 2013</label></li>
					     					<li><label id="f_05162013_label" for="f_05162013" class="btn filter-checkbox-label"><input id="f_05162013" type="checkbox" value="05162013" class="filter-checkbox filter-day"/>May 16, 2013</label></li>
					     					<li><label id="f_05172013_label" for="f_05172013" class="btn filter-checkbox-label"><input id="f_05172013" type="checkbox" value="05172013" class="filter-checkbox filter-day"/>May 17, 2013</label></li>
					     				</ul>
	     							</div>
	     						</li>
	     						<li class="master-filter-li">
	     							<div class="master-filter-list-content master-filter-list-content-last">
		     							<h4 class="filter-list-title">Tracking #</h4>
		     							<ul class="filter-list-ul">
					     					<li><label id="f_123456789102_label" for="f_123456789102" class="btn filter-checkbox-label"><input id="f_123456789102" type="checkbox" value="123456789102" class="filter-checkbox filter-number"/>123456789102</label></li>
					     					<li><label id="f_223456789122_label" for="f_223456789122" class="btn active filter-checkbox-label"><input id="f_223456789122" type="checkbox" value="223456789122" class="filter-checkbox filter-number" checked/>223456789122</label></li>
					     				</ul>
				     				</div>
	     						</li>
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
