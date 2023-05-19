package org.golde.dormroom.lightwall.http.justjson.routes;

import org.golde.router.annotations.Route;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

public class RouteIndex {

	@Route(value="")
	public void index(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		res.sendText("Hello! You have reached the non existant homepage");
	}
	
}
