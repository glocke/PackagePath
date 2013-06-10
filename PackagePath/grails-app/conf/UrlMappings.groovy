class UrlMappings {

	static mappings = {
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
