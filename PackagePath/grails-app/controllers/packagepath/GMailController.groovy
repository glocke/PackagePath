package packagepath


import grails.converters.JSON

import java.util.regex.Matcher

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
	
	/*
	 * Global search string
	 */
	private static final String searchString = "in:anywhere newer_than:14d (" + Constants.FEDEX + " OR " + Constants.UPS + " OR " + Constants.USPS + ")";

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
					Constants.FEDEX_REGEX_LIST.each{
						m = ( messageBody =~ it )
						for (def i=0; i < m.getCount(); i++) {
							fedexTrackingNumbers.add(m[i][0])
						}
					}
					
					Constants.UPS_REGEX_LIST.each{
						m = ( messageBody =~ it )
						for (def i=0; i < m.getCount(); i++) {
							upsTrackingNumbers.add(m[i][0])
						}
					}
					
					Constants.USPS_REGEX_LIST.each{
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