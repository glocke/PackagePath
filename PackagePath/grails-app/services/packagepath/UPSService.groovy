package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovy.util.XmlSlurper
import java.text.SimpleDateFormat

class UPSService {
	
    Package getTrackingInfo(String trackingNumber) {
		def http = new HTTPBuilder('https://www.ups.com/ups.app/xml/Track')
		
		http.request( POST, XML ) {
			body = 
			    '<AccessRequest>' +
					'<AccessLicenseNumber>4CB2A17181704496</AccessLicenseNumber>' +
					'<UserId>packagepath</UserId>' +
					'<Password>JayWeissExcel2007</Password>' +
				'</AccessRequest>' +
				'<TrackRequest>' +
					'<Request>' +
						'<TransactionReference>' + 
							'<CustomerContext>guidlikesubstance</CustomerContext>' +
							'<XpciVersion>1.0</XpciVersion>' +
						'</TransactionReference>' +
						'<RequestAction>Track</RequestAction>' +
						'<RequestOption>activity</RequestOption>' +
					'</Request>' +
					'<TrackingNumber>' + trackingNumber.value + '</TrackingNumber>' +
				'</TrackRequest>'
			
			// for testing purposes
			response.success = { resp, xml ->
				
				Package p = new Package()
				p.shippingService = "ups"
				p.trackingNumber = xml?.Shipment?.Package?.TrackingNumber?.text()
				p.startZip = xml?.Shipment?.Shipper?.Address?.PostalCode?.text()
				p.currentZip = xml?.Shipment?.Package?.Activity?.ActivityLocation?.Address?.PostalCode?.text()
				p.currentPackageStatus = xml?.Shipment?.Package?.Activity?.Status?.StatusType?.Code?.text()
				p.endZip = xml?.Shipment?.ShipTo?.Address?.PostalCode?.text()
				p.inTransit = true
				if ("D".equals(p.currentPackageStatus)) {
					p.endZip = p.currentZip
					p.inTransit = false
				}
				
				SimpleDateFormat upsDf = new SimpleDateFormat("yyyyMMdd");
				String strDate = null;
				
				strDate = xml?.Shipment?.EstimatedDeliveryDetails?.Date?.text()
				if (strDate != null && !strDate.isEmpty()) {
					p.estimatedEndTransitDate = upsDf.parse(strDate);
				}
				// update estimate if a scheduled date is present first
				strDate = xml?.Shipment?.ScheduledDeliveryDate?.text()
				if (strDate != null && !strDate.isEmpty()) {
					p.estimatedEndTransitDate = upsDf.parse(strDate);
				}
				
				return p
				
			}
			
			return null
		}
		
    }
	
}
