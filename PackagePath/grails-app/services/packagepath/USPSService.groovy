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
			uri.path = '/ShippingAPI.dll'
			uri.query = [API:'TrackV2', XML: xmlRequest]
			
			response.success = {resp, xml ->				
				Package p = new Package()
				SimpleDateFormat uspsDf = new SimpleDateFormat("MMMM d, yyyy");
				
				p.shippingService = ShippingProviderType.USPS
				p.trackingNumber = trackingNumber
				
				// iterate over each XML 'TrackDetail' element in the response
				// the last element will be the start location
				xml?.TrackInfo?.TrackDetail?.each {  
					PackageLocation loc = new PackageLocation()
					String strZip = it.EventZIPCode?.text()
					if (strZip != null && !strZip.isEmpty()) {
						loc.zip = strZip
						loc.zipType = ZipType.OTHER
						p.locations.add(loc)
					}
					
					String strDate = it.EventDate?.text()
					if (strDate != null && !strDate.isEmpty()) {
						p.startTransitDate = uspsDf.parse(strDate);
					}
				}
				// the last element will be the start location
				if (p.locations.size() > 0) {
					PackageLocation startLoc = p.locations.removeLast()
					startLoc.zipType = ZipType.START
					p.locations.add(startLoc)
				}
				
				String currentZip = xml?.TrackInfo?.TrackSummary?.EventZIPCode?.text()
				if (currentZip != null && !currentZip.isEmpty()) {
					PackageLocation currentLoc = new PackageLocation()
					currentLoc.zip = currentZip
					currentLoc.zipType = ZipType.CURRENT
					p.locations.add(currentLoc)
				}
				
				p.inTransit = true
				String uspsStatus = xml?.TrackInfo?.TrackSummary?.Event?.text()			
				if (USPS_DELIVERED_STATUS.equalsIgnoreCase(uspsStatus)) {
					PackageLocation endLoc = new PackageLocation()
					endLoc.zip = currentZip
					endLoc.zipType = ZipType.END
					p.locations.add(endLoc)
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
