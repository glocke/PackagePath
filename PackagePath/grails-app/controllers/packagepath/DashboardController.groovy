package packagepath

import grails.converters.JSON

import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb

import uk.co.desirableobjects.oauth.scribe.OauthService

class DashboardController {
	
	/*
	 * Variables
	 */
	OauthService oauthService = new OauthService()// TODO: change to spring injection

	/**
	 * Load the main dashboard screen
	 * 
	 * @return
	 */
    def index() { }
	
	/**
	 * An action that retrieves the packages for this user
	 * 
	 * @return
	 */
	def retrievePackages() {
		
		String sessionKey = oauthService.findSessionKeyForAccessToken('google')
		Token token = session[sessionKey]
		def response = oauthService.getGoogleResource(token, 'https://mail.google.com/')
		
		/*
		 * Get a map of tracking numbers for each carrier for the email address
		 */
		//Map<String, String> trackingNumbersMap = emailController.getTrackingNumbers();
		
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
