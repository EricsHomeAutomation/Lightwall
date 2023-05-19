package org.golde.dormroom.lightwall.http.justjson.routes;

import java.io.IOException;

import org.golde.dormroom.lightwall.Main;
import org.golde.router.annotations.Route;
import org.golde.router.enums.RequestMethod;
import org.golde.router.enums.StatusCode;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

public class RouteDeleteScene {

	@Route(value = "delete/{id}", method = RequestMethod.POST)
	public void deleteSceneRequest(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		int id = req.getWildcardAsInteger("id");
		try {
			Main.animationHandler.deleteScene(id);
			res.sendSuccess();
		} 
		catch (IOException e) {
			res.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR).sendText("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Route(value = "delete/{id}", method = RequestMethod.OPTIONS)
	public void fuckCORS(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		res.getHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		res.getHeaders().set("Access-Control-Allow-Headers", "*");
		res.getHeaders().set("Access-Control-Allow-Credentials", "true");
		res.getHeaders().set("Access-Control-Allow-Credentials-Header", "*");
		res.sendSuccess();
	}
	
}
