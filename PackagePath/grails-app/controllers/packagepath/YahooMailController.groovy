package packagepath

import grails.converters.JSON

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.search.AndTerm
import javax.mail.search.BodyTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.OrTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.SearchTerm

import org.apache.commons.io.IOUtils
import org.codehaus.groovy.grails.web.json.JSONElement
import org.scribe.model.Token

import uk.co.desirableobjects.oauth.scribe.OauthService

import com.google.code.OAuth2Authenticator
import com.sun.mail.gimap.GmailMessage
import com.sun.mail.imap.IMAPSSLStore

class YahooMailController implements EmailControllerInterface{
	
	/*
	 * Variables
	 */
	OauthService oauthService = new OauthService()// TODO: change to spring injection
	
	private static List<String> FEDEX_REGEX_LIST = new ArrayList<String>();
	private static List UPS_REGEX_LIST = new ArrayList<String>();
	private static List<String> USPS_REGEX_LIST = new ArrayList<String>();
	
	static List<Pattern> FEDEX_REG_PATTERNS;
	static List<Pattern> UPS_REG_PATTERNS;
	static List<Pattern> USPS_REG_PATTERNS;
	
	/*
	 * Global search string
	 */
	private static String searchString = "in:anywhere newer_than:14d (fedex OR ups OR usps)";
	
	static{
		
		//fedex
		FEDEX_REGEX_LIST.add(/(\b96\d{20}\b)|(\b\d{15}\b)|(\b\d{12}\b)/);
		FEDEX_REGEX_LIST.add(/\b((98\d\d\d\d\d?\d\d\d\d|98\d\d) ?\d\d\d\d ?\d\d\d\d( ?\d\d\d)?)\b/);
		FEDEX_REGEX_LIST.add(/^[0-9]{15}$/);
		
		//ups
		UPS_REGEX_LIST.add(/\b(1Z ?[0-9A-Z]{3} ?[0-9A-Z]{3} ?[0-9A-Z]{2} ?[0-9A-Z]{4} ?[0-9A-Z]{3} ?[0-9A-Z]|[\dT]\d\d\d ?\d\d\d\d ?\d\d\d|\d{22})\b/)
		
		//usps
		USPS_REGEX_LIST.add(/(\b\d{30}\b)|(\b91\d+\b)|(\b\d{20}\b)/);
		USPS_REGEX_LIST.add(/^E\D{1}\d{9}\D{2}$|^9\d{15,21}$/);
		USPS_REGEX_LIST.add(/^91[0-9]+$/);
		USPS_REGEX_LIST.add(/^[A-Za-z]{2}[0-9]+US$/);
	}

    def index() { }

	/**
	 * Return a set a tacking numbers 
	 */
	@Override
	public Map<String, Set<String>> retrieveTrackingNumbers() {
		
		/*
		 * Tracking numbers
		 */
		Map<String, Set<String>> trackingNumbers = new HashMap<String, Set<String>>();
		Set<String> fedexTrackingNumbers = new HashSet<String>();
		Set<String> upsTrackingNumbers = new HashSet<String>();
		Set<String> uspsTrackingNumbers = new HashSet<String>();
		
		String sessionKey = oauthService.findSessionKeyForAccessToken('yahoo')
		Token token = session[sessionKey]
		def response = oauthService.getGoogleResource(token, 'https://www.googleapis.com/oauth2/v1/userinfo?alt=json')
		
		JSONElement json = JSON.parse(response.getBody())
		String email = json['email']
		
		/*
		 * Get the imap store
		 */
		OAuth2Authenticator.initialize();
		IMAPSSLStore store = OAuth2Authenticator.connectToImap("imap.mail.yahoo.com",
                                       143,
                                        email,
                                        token.getToken(),
                                        true);
		
		/*
		 * Just doing this for local development... not sure why OAuth is not working.
		 */
		//String userEmail = "";
		//String userPassword = "";
		//Properties props = System.getProperties();
		//props.setProperty("mail.store.protocol", "gimaps");
		//props.setProperty("mail.gimaps.ssl.trust", "*");
		//props.setProperty("mail.gimaps.ssl.checkserveridentity", "false");
		//Session session = Session.getDefaultInstance(props, null);
		//GmailSSLStore store = (GmailSSLStore) session.getStore("gimaps");
		//store.connect("imap.gmail.com", userEmail, userPassword);					
		
        Folder folder = null;
		try {
			
			/*
			 * Search all of their folders... what if they have a 'shipping folder'
			 */
			Folder[] folders = store.getDefaultFolder().list();
			
			/*
			 * Search criteria
			 */
			GregorianCalendar pastDate = new GregorianCalendar();
			pastDate.add(Calendar.DATE, -14);
			SearchTerm st = new AndTerm(new ReceivedDateTerm(ComparisonTerm.GT, pastDate.getTime()), new OrTerm(new BodyTerm("ups"), new BodyTerm("usps"), new BodyTerm("fedex")));
			
			for(Folder fd : folders){
				if (fd != null) {
			   
					System.out.println("Searching started....");
				   
					// Create GMail raw search term and use it to search in folder
					fd.open(Folder.READ_ONLY);
					
					/*
					 * Search it
					 */
					Message[] messagesFound = fd.search(st);
				   
					System.out.println("Total messages found for keyword are = "+messagesFound.length);
					System.out.println("Messages found are:");
				   
					// Process the messages found in search
					System.out.println("--------------------------------------------");
					for(Message message : messagesFound){
						System.out.println("# "+ message.getSubject());
						message.writeTo(System.out);
					}
					System.out.println("--------------------------------------------");
	
					System.out.println("Searching done!");
					fd.close(false);
				}
			}
		} catch (MessagingException ex) {
			System.out.println(ex);
		} finally {
			if (store != null) {
				store.close();
			}
		}
		
		/*
		 * Add all values
		 */
		trackingNumbers.put("fedex", fedexTrackingNumbers);
		trackingNumbers.put("ups", upsTrackingNumbers);
		trackingNumbers.put("usps", uspsTrackingNumbers);
									
		return trackingNumbers;
	}
}