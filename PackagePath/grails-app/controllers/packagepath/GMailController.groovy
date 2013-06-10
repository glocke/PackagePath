package packagepath

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONElement
import org.scribe.model.Token

import uk.co.desirableobjects.oauth.scribe.OauthService

import com.google.code.OAuth2Authenticator
import com.sun.mail.imap.IMAPStore


class GMailController implements EmailControllerInterface{
	
	/*
	 * Variables
	 */
	OauthService oauthService = new OauthService()// TODO: change to spring injection

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
		
		OAuth2Authenticator.initialize();
		
		IMAPStore imapStore = OAuth2Authenticator.connectToImap("imap.gmail.com",
                                        993,
                                        email,
                                        token.getToken(),
										token.getSecret(),
                                        true);
		System.out.println("Successfully authenticated to IMAP.\n");
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
