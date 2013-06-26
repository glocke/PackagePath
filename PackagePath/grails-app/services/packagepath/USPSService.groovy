package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovy.util.XmlSlurper
import java.text.SimpleDateFormat
import groovyx.net.http.HTTPBuilder

class USPSService {
	private final String USPS_DELIVERED_STATUS = "DELIVERED";
	
     Package getTrackingInfo(String trackingNumber) {
		def http = new HTTPBuilder('http://production.shippingapis.com')
		String xmlRequest = 
			'<TrackFieldRequest USERID="649PACKA6931">' +
				'<TrackID ID="' + trackingNumber + '"></TrackID>' +
			'</TrackFieldRequest>';
		
		http.request( GET, XML  ) {
			uri.path = '/ShippingAPITest.dll'
			uri.query = [API:'TrackV2', XML: xmlRequest]
			
			response.success = {resp, xml ->				
				Package p = new Package()
				SimpleDateFormat uspsDf = new SimpleDateFormat("MMMM d, yyyy");
				
				p.shippingService = "usps"
				p.trackingNumber = trackingNumber
				
				xml?.TrackInfo?.TrackDetail?.each {  // iterate over each XML 'TrackDetail' element in the response:
					String strDate = null;
					p.startZip = it.EventZIPCode?.text()
					strDate = it.EventDate?.text()

					if (strDate != null) {
						p.startTransitDate = uspsDf.parse(strDate);
					}
				}
				p.currentZip = xml?.TrackInfo?.TrackSummary?.EventZIPCode?.text()
				p.inTransit = true
				String uspsStatus = xml?.TrackInfo?.TrackSummary?.Event?.text() // TODO: shorten to 1 char				
				if (USPS_DELIVERED_STATUS.equalsIgnoreCase(uspsStatus)) {
					p.endZip = p.currentZip
					p.inTransit = false
					p.currentPackageStatus = Package.STATUS_DELIVERED
				}
				
				String strDate = null
				strDate = xml?.TrackInfo?.TrackSummary?.EventDate?.text()
				if (strDate != null) {
					if (USPS_DELIVERED_STATUS.equalsIgnoreCase(uspsStatus)) {
						p.endTransitDate = uspsDf.parse(strDate);
					}					
				}
				
				return p
			
			}
			return null
		}	
				
    }
}
