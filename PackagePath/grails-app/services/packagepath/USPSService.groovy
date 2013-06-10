package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovy.util.XmlSlurper
import java.text.SimpleDateFormat
import groovyx.net.http.HTTPBuilder

class USPSService {

     Package getTrackingInfo(String trackingNumber) {
		def http = new HTTPBuilder('http://production.shippingapis.com')
		String xmlRequest = 
			'<TrackRequest USERID="649PACKA6931">' +
				'<TrackID ID="' + trackingNumber + '"></TrackID>' +
			'</TrackRequest>';
		
		http.get( path : '/ShippingAPITest.dll', query : [API:'TrackV2', XML: xmlRequest] ) {
			/*body = 
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
					'<TrackingNumber>' + trackingNumber + '</TrackingNumber>' +
				'</TrackRequest>'*/
			
			// for testing purposes
			resp, xml ->
				
				/*Package p = new Package()
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
				if (strDate != null) {
					p.estimatedEndTransitDate = upsDf.parse(strDate);
				}
				// update estimate if a scheduled date is present first
				strDate = xml?.Shipment?.ScheduledDeliveryDate?.text()
				if (strDate != null) {
					p.estimatedEndTransitDate = upsDf.parse(strDate);
				}
				
				return p*/
				
				println "response status: ${resp.statusLine}"
				println 'Headers: -----------'
				resp.headers.each { h ->
				  println " ${h.name} : ${h.value}"
				}
				println 'Response data: -----'
				System.out << xml
				println '\n--------------------'
				
			
		}	
		return null
				
    }
}
