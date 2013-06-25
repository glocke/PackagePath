package packagepath

import grails.converters.JSON

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException

import org.apache.commons.io.IOUtils
import org.codehaus.groovy.grails.web.json.JSONElement
import org.scribe.model.Token

import uk.co.desirableobjects.oauth.scribe.OauthService

import com.google.code.OAuth2Authenticator
import com.sun.mail.gimap.GmailFolder
import com.sun.mail.gimap.GmailMessage
import com.sun.mail.gimap.GmailRawSearchTerm
import com.sun.mail.gimap.GmailSSLStore



class GMailController implements EmailControllerInterface{
	
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
		
		String sessionKey = oauthService.findSessionKeyForAccessToken('google')
		Token token = session[sessionKey]
		def response = oauthService.getGoogleResource(token, 'https://www.googleapis.com/oauth2/v1/userinfo?alt=json')
		
		JSONElement json = JSON.parse(response.getBody())
		String email = json['email']
		
		/*
		 * Get the imap store
		 */
		OAuth2Authenticator.initialize();
		GmailSSLStore store = OAuth2Authenticator.connectToImap("imap.gmail.com",
                                       993,
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
		
        GmailFolder folder = null;
		try {
			
			/*
			 * Search all of their folders... what if they have a 'shipping folder'
			 */
			GmailFolder fd = store.getFolder("[Gmail]/All Mail");
			
			def ups_regex = UPS_REGEX_LIST[0];
			
			/*
			 * Create one search term
			 */
			GmailRawSearchTerm rawTerm = new GmailRawSearchTerm(searchString);
			
			if (fd != null) {
                /*
                 *  Create GMail raw search term and use it to search in folder 
                 */
                fd.open(Folder.READ_ONLY);
                Message[] messagesFound = fd.search(rawTerm);
				GmailMessage gm;
				
				/*
				 * Iterate the messages found
				 */
                for(Message message : messagesFound){
					
					/*
					 * Get the gmail message
					 */
					gm = (GmailMessage)message;
					
					/*
					 * Get the body content
					 */
					StringWriter writer = new StringWriter();
					IOUtils.copy(gm.getContentStream(), writer, "UTF-8");
					String messageBody = writer.toString();
					
					/*
					 * Iterate regex
					 */
					Matcher m;
					FEDEX_REGEX_LIST.each{
						m = ( messageBody =~ it )
						for (def i=0; i < m.getCount(); i++) {
							fedexTrackingNumbers.add(m[i][0])
						}
					}
					
					UPS_REGEX_LIST.each{
						m = ( messageBody =~ it )
						for (def i=0; i < m.getCount(); i++) {
							upsTrackingNumbers.add(m[i][0])
						}
					}
					
					USPS_REGEX_LIST.each{
						m = ( messageBody =~ it )
						for (def i=0; i < m.getCount(); i++) {
							uspsTrackingNumbers.add(m[i][0])
						}
					}
                }
				fd.close(false);
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