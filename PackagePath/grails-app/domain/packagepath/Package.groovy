package packagepath

class Package {
	static belongsTo = [user: User]
	String shippingService
	String trackingNumber
	String startZip	
	String endZip
	String currentZip
	String currentPackageStatus
	
	Date startTransitDate
	Date endTransitDate
	Date estimatedEndTransitDate
	
	// 0: package was delivered
	// 1: packing is currently in transit
	Integer isInTransit
	
    static constraints = {
    }
}
