package packagepath

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

class DashboardController {
	
	//Spring injection in the future
	EmailControllerInterface emailController = new GMailController();

	/**
	 * Load the main dashboard screen
	 * 
	 * @return
	 */
	@Secured(['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'])
    def index() { }
	
	/**
	 * An action that retrieves the packages for this user
	 * 
	 * @return
	 */
	@Secured(['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'])
	def retrievePackages() {
		
		/*
		 * Get a map of tracking numbers for each carrier for the email address
		 */
		Map<String, Set<String>> trackingNumbersMap = emailController.retrieveTrackingNumbers();
		
		/*
		 * Call the package controller to get a list of packages
		 */
		//packageList = packgeController.getPackages(trackingNumbersMap);
		List<Package> packageList = testPackages();
		
		render packageList as JSON
	}
	
	def testPackages(){
		//package list
		List<Package> packageList = new ArrayList<Package>()
		
		//start calendar
		Calendar start = Calendar.getInstance()
		start.add(Calendar.DATE, -5);
		
		//end calendar
		Calendar end = Calendar.getInstance()
		end.add(Calendar.DATE, 6)
		
		//test package
		Package p = new Package();
		p.setShippingService("ups")
		p.setTrackingNumber("123")
		p.setStartZip("90201")
		p.setStartTransitDate(start.getTime())
		p.setEndZip("53719")
		p.setEndTransitDate(end.getTime())
		p.setCurrentZip("80111")
		
		packageList.add(p)
		
		//start calendar
		Calendar start2 = Calendar.getInstance()
		start2.add(Calendar.DATE, -2);
		
		//end calendar
		Calendar end2 = Calendar.getInstance()
		end2.add(Calendar.DATE, 6)
		
		//test package
		Package p2 = new Package();
		p2.setShippingService("fedex")
		p2.setTrackingNumber("543")
		p2.setStartZip("98146")
		p2.setStartTransitDate(start2.getTime())
		p2.setEndZip("53719")
		p2.setEndTransitDate(end2.getTime())
		p2.setCurrentZip("58501")
		
		packageList.add(p2)
		
		return packageList
	}
}