package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder

class UPSService {
	
    Object getTrackingInfo(String trackingNumber) {
		def http = new HTTPBuilder('https://www.ups.com/ups.app/xml/Track')
		
		resp = http.request( POST, XML ) {
			body = {
				AccessRequest {
					AccessLicenseNumber: '4CB2A17181704496'
					UserId: 'packagepath'
					Password: 'JayWeissExcel2007'
				}
				TrackRequest {
					Request {
						TransactionReference {
							CustomerContext: 'guidlikesubstance'
						}
						RequestAction: 'Track'
					}
					TrackingNumber: '1Z12345E1512345676'
				}
			}
			
			// for testing purposes
			response.success = { resp ->
				println "UPS response status: ${resp.statusLine}"
				assert resp.statusLine.statusCode == 200
			}
		}
		
		return resp.data
		
    }
	
}
