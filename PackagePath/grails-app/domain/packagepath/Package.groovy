package packagepath

import packagepath.auth.User

// not an interface.  Groovy Interfaces must have static final fields
class Package {
	static belongsTo = [user: User]
	private final static String STATUS_DELIVERED = "D"
	
	String shippingService
	String trackingNumber
	LinkedList<PackageLocation> locations
	String currentPackageStatus
	
	Date startTransitDate
	Date endTransitDate
	Date estimatedEndTransitDate
	
	Boolean inTransit
	
    static constraints = {
    }
	static mapping = {
		locations lazy: false
	}
	
	Package() {
		locations = new LinkedList<PackageLocation>()
	}
}
