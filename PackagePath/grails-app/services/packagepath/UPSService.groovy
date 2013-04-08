package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

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
			response.success = { xml, reader ->
				println "response status: ${xml.statusLine}"
				println 'Headers: -----------'
				xml.headers.each { h ->
				  println " ${h.name} : ${h.value}"
				}
				println 'Response data: -----'
				System.out << reader
				// response is in 'reader'
				println '\n--------------------'
				assert xml.statusLine.statusCode == 200
			}
		}
		
    }
	
}
