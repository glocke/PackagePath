package packagepath

import grails.converters.JSON

import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException

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
	private static String searchString = "usps OR fedex";

    def index() { }

	/**
	 * Return a set a tacking numbers 
	 */
	@Override
	public Set<String> retrieveTrackingNumbers() {
		
		String sessionKey = oauthService.findSessionKeyForAccessToken('google')
		Token token = session[sessionKey]
		def response = oauthService.getGoogleResource(token, 'https://www.googleapis.com/userinfo/email?alt=json')
		
		JSONElement json = JSON.parse(response.getBody())
		String email = json['data'].email
		
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
		//GmailSSLStore  store = (GmailSSLStore) session.getStore("gimaps");
		//store.connect("imap.gmail.com", userEmail, userPassword);					
		
        GmailFolder folder = null;
		try {
			
			/*
			 * Search all of their folders... what if they have a 'shipping folder'
			 */
			GmailFolder[] folders = ((GmailFolder[])store.getDefaultFolder().list());
			
			/*
			 * Create one search term
			 */
			GmailRawSearchTerm rawTerm = new GmailRawSearchTerm(searchString);
			
			for(GmailFolder fd : folders){
				if (fd != null) {
               
	                System.out.println("Searching started....");
	               
	                // Create GMail raw search term and use it to search in folder 
	                fd.open(Folder.READ_ONLY);
	               
	                Message[] messagesFound = fd.search(rawTerm);
	               
	                System.out.println("Total messages found for keyword are = "+messagesFound.length);
	                System.out.println("Messages found are:");
	               
	                // Process the messages found in search
	                System.out.println("--------------------------------------------");
					GmailMessage gm;
	                for(Message message : messagesFound){
						gm = (GmailMessage)message;
	                    System.out.println("# "+ gm.getSubject());
						gm.writeTo(System.out);
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
									
		return null;
	}
}