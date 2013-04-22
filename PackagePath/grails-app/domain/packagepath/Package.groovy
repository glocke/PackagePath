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
	
	Boolean inTransit
	
    static constraints = {
    }
}
