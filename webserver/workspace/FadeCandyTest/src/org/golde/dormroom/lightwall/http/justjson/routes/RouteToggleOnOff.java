package org.golde.dormroom.lightwall.http.justjson.routes;

import org.golde.dormroom.lightwall.Main;
import org.golde.router.annotations.Route;
import org.golde.router.enums.RequestMethod;
import org.golde.router.enums.StatusCode;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

import com.google.gson.JsonObject;

public class RouteToggleOnOff {

	@Route(value = "isOn", method = RequestMethod.GET)
	public void arWeOn(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		
		JsonObject json = new JsonObject();
		json.addProperty("on", Main.animationHandler.isDisplaying());
		res.sendJSON(json);
		
	}
	
	@Route(value = "toggle", method = RequestMethod.POST)
	public void toggleSceneRequest(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");

		String isOnStr = req.getQueryParameters().get("on");
		
		if(isOnStr == null) {
			res.setStatusCode(StatusCode.BAD_REQUEST).sendText("Must include ?on=true or ?on=false");
			return;
		}
		
		Main.animationHandler.setDisplaying(Boolean.parseBoolean(isOnStr));
		res.sendSuccess();
	}
	
	@Route(value = "toggle", method = RequestMethod.OPTIONS)
	public void fuckCORS(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		res.getHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		res.getHeaders().set("Access-Control-Allow-Headers", "*");
		res.getHeaders().set("Access-Control-Allow-Credentials", "true");
		res.getHeaders().set("Access-Control-Allow-Credentials-Header", "*");
		res.sendSuccess();
	}
	
}
