class UrlMappings {

	static mappings = {

        "/hello"(controller: 'hello', action: 'testMethod', method: 'GET')

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
