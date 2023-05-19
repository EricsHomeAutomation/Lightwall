package org.golde.dormroom.lightwall.http.justjson.routes;

import java.io.File;

import org.golde.router.annotations.Route;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

public class RouteGetScenesJSON {

	@Route(value = "scenes.json")
	public void onSceneFileRequest(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		File file = new File("files" + File.separator +"scenes.json");
		res.sendFile(file, false);
	}
	
}
