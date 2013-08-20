package packagepath
import groovyx.net.http.*
import groovy.xml.StreamingMarkupBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovy.util.XmlSlurper
import java.text.SimpleDateFormat

class UPSService {
	private final String API_DELIVERED_FLAG = "D"
	
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
				p.shippingService = ShippingProviderType.UPS
				p.trackingNumber = trackingNumber
				
				String tmpStartZip = xml?.Shipment?.Shipper?.Address?.PostalCode?.text()
				if (tmpStartZip != null && !tmpStartZip.isEmpty()) {
					PackageLocation startLoc = new PackageLocation()
					startLoc.zipType = ZipType.START
					startLoc.zip = tmpStartZip
					p.locations.add(startLoc)
				}
				String tmpCurrentZip = xml?.Shipment?.Package?.Activity?.ActivityLocation?.Address?.PostalCode?.text()
				if (tmpCurrentZip != null && !tmpCurrentZip.isEmpty()) {
					PackageLocation currentLoc = new PackageLocation()
					currentLoc.zipType = ZipType.CURRENT
					currentLoc.zip = tmpCurrentZip
					p.locations.add(currentLoc)
				}
				String tmpEndZip = xml?.Shipment?.ShipTo?.Address?.PostalCode?.text()
				p.currentPackageStatus = xml?.Shipment?.Package?.Activity?.Status?.StatusType?.Code?.text()
				p.inTransit = true
				if (API_DELIVERED_FLAG.equals(p.currentPackageStatus) && tmpCurrentZip != null  && !tmpCurrentZip.isEmpty()) {
					PackageLocation currentLoc = new PackageLocation()
					currentLoc.zipType = ZipType.END
					currentLoc.zip = tmpCurrentZip
					p.locations.add(currentLoc)
				} else if (tmpEndZip != null && !tmpEndZip.isEmpty()) {
					PackageLocation endLoc = new PackageLocation()
					endLoc.zipType = ZipType.END
					endLoc.zip = tmpEndZip
					p.locations.add(endLoc)
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
