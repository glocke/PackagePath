package packagepath

import packagepath.auth.User

// not an interface.  Groovy Interfaces must have static final fields
class Package {
	static belongsTo = [user: User]
	private final static String STATUS_DELIVERED = "D"
	
	String shippingService
	String trackingNumber
	String startZip	
	String endZip
	String currentZip
	String currentPackageStatus
	
	Date startTransitDate
	Date endTransitDate
	Date estimatedEndTransitDate
	
	Boolean inTransit
	
    static constraints = {
    }
}
