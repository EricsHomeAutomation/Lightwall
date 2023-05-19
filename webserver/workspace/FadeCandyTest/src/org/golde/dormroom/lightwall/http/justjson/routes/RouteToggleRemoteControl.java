package org.golde.dormroom.lightwall.http.justjson.routes;

import org.golde.router.annotations.Route;
import org.golde.router.enums.RequestMethod;
import org.golde.router.enums.StatusCode;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

public class RouteToggleRemoteControl {

	private static boolean removeControlEnabled = false;
	
	@Route(value = "toggleRemoteControl", method = RequestMethod.POST)
	public void previewSceneRequest(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");

		String isOnStr = req.getQueryParameters().get("on");
		
		if(isOnStr == null) {
			res.setStatusCode(StatusCode.BAD_REQUEST).sendText("Must include ?on=true or ?on=false");
			return;
		}
		
		removeControlEnabled = Boolean.parseBoolean(isOnStr);
		res.sendSuccess();
	}
	
	@Route(value = "toggleRemoteControl", method = RequestMethod.OPTIONS)
	public void fuckCORS(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		res.getHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		res.getHeaders().set("Access-Control-Allow-Headers", "*");
		res.getHeaders().set("Access-Control-Allow-Credentials", "true");
		res.getHeaders().set("Access-Control-Allow-Credentials-Header", "*");
		res.sendSuccess();
	}
	
	public static boolean isRemoteControlEnabled() {
		return removeControlEnabled;
	}
	
}
