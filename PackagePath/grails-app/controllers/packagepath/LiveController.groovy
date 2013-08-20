package packagepath

import grails.converters.JSON

import java.util.regex.Matcher

import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Store
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

class LiveController implements EmailControllerInterface{
	
	/*
	 * Variables
	 */
	OauthService oauthService = new OauthService()// TODO: change to spring injection

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
		
		//String sessionKey = oauthService.findSessionKeyForAccessToken('live')
		//Token token = session[sessionKey]
		//def guid = oauthService.getLiveResource(token, 'http://social.yahooapis.com/v1/me/guid')
		//def response = oauthService.getLiveResource(token, 'http://social.yahooapis.com/v1/user/' + guid + '/profile?format=json')
		
		//JSONElement json = JSON.parse(response.getBody())
		//String email = json['email']
		
		/*
		 * Get the imap store
		 */
		//OAuth2Authenticator.initialize();
		//POP3Store store = OAuth2Authenticator.connectToPop3("pop3.live.com",
        //                               995,
        //                               email,
        //                               token.getToken(),
        //                               true);
		
		/*
		 * Just doing this for local development... not sure why OAuth is not working.
		 */
		String host = "pop3.live.com";
		String username = "";
		String password = "";
		
		Properties props = System.getProperties();
		props.setProperty("mail.pop3.ssl.enable", "true");
		props.setProperty("mail.pop3s.port",  "995");
		props.setProperty("mail.debug", "true");
		props.setProperty("mail.pop3s.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.setProperty("mail.pop3s.socketFactory.fallback", "false"); 
		props.setProperty("mail.pop3s.port", "995"); 
		props.setProperty("mail.store.protocol", "gimaps");
		props.setProperty("mail.pop3s.ssl.trust", "*");
		props.setProperty("mail.pop3s.ssl.checkserveridentity", "false");
		props.setProperty("mail.pop3s.socketFactory.port", "995");
		
		Session session = Session.getInstance(props, null);
		Store store = session.getStore("pop3s");
		store.connect(host, 995, username, password);
		
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
			SearchTerm st = new AndTerm(new ReceivedDateTerm(ComparisonTerm.GT, pastDate.getTime()), new OrTerm(new BodyTerm(Constants.UPS), new BodyTerm(Constants.USPS), new BodyTerm(Constants.FEDEX)));
			
			for(Folder fd : folders){
				if (fd != null) {
			   
					System.out.println("Searching started....");

					fd.open(Folder.READ_ONLY);
					
					/*
					 * Search it
					 */
					Message[] messagesFound = fd.search(st);
				   
					System.out.println("Total messages found for keyword are = "+messagesFound.length);
					System.out.println("Messages found are:");
				   
					// Process the messages found in search
					System.out.println("--------------------------------------------");
					
					Matcher m;
					for(Message message : messagesFound){
						
						/*
						 * Get the body content
						 */
						StringWriter writer = new StringWriter();
						IOUtils.copy(message.getContent(), writer, "UTF-8");
						String messageBody = writer.toString();
						
						/*
						 * Iterate regex
						 */
						
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
