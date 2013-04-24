package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovy.util.XmlSlurper

class UPSService {
	
    Package getTrackingInfo(String trackingNumber) {
		def http = new HTTPBuilder('https://www.ups.com/ups.app/xml/Track')
		//def http = new HTTPBuilder('https://wwwcie.ups.com/ups.app/xml/Track')
		
		http.request( POST, XML ) {
			/*body = {
				AccessRequest {
					AccessLicenseNumber: '4CB2A17181704496'
					UserId: 'packagepath'
					Password: 'JayWeissExcel2007'
				}
				TrackRequest {
					Request {
						TransactionReference {
							CustomerContext: 'guidlikesubstance'
							XpciVersion: '1.0'
						}
						RequestAction: 'Track'
						RequestOption: 'activity'
					}
					TrackingNumber: '1Z12345E1512345676'
				}
			}*/
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
					'<TrackingNumber>' + trackingNumber + '</TrackingNumber>' +
				'</TrackRequest>'
			
			// for testing purposes
			response.success = { resp, xml ->
				
				Package p = new Package()
				p.shippingService = "ups"
				p.trackingNumber = xml?.Shipment?.Package?.TrackingNumber?.text()
				p.startZip = xml?.Shipment?.Shipper?.Address?.PostalCode?.text()
				p.currentZip = xml?.Shipment?.Package?.Activity?.ActivityLocation?.Address?.PostalCode?.text()
				p.currentPackageStatus = xml?.Shipment?.Package?.Activity?.Status?.StatusType?.Code?.text()
				p.endZip = null
				p.inTransit = true
				if ("D".equals(p.currentPackageStatus)) {
					p.endZip = p.currentZip
					p.inTransit = false
				}
				// TODO: still need date fields for Package object
				
				return p
				
			}
			
			return null
		}
		
    }
	
}
