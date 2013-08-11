class UrlMappings {

	static mappings = {
		
		"/login/auth" {
			controller = "openId"
			action = "auth"
		}

		"/login/openIdCreateAccount" {
			controller = "openId"
			action = "createAccount"
		}
		
		"/oauth/success"(controller: "springSecurityOAuth", action: "onSuccess")
		"/oauth/failure"(controller: "springSecurityOAuth", action: "onFailure")
		
		"/testTracker/$type?/$trackingNumber?"(controller:"package", action="testTracker")
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/"(view:"/home/home")
		"500"(view:'/error')
	}
}
