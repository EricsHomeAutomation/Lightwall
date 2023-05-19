package org.golde.dormroom.lightwall.http.justjson.routes;

import java.io.File;

import org.golde.router.annotations.Route;
import org.golde.router.enums.StatusCode;
import org.golde.router.objects.Request;
import org.golde.router.objects.Response;

public class RouteGetPreviewImage {

	@Route(value = "previmgs/{id}")
	public void onPreviewImageRequest(Request req, Response res) {
		res.getHeaders().set("Access-Control-Allow-Origin", "*");
		String id = req.getWildcard("id");
		
		File file = new File("files"+ File.separator + "previmgs" + File.separator + id + ".gif");
		
		//Send a ? image if we failed to load an image
		if(!file.exists()) {
			file = new File("files"+ File.separator + "previmgs" + File.separator + "unknown.png");
			res.setStatusCode(StatusCode.NOT_FOUND);
		}
		
		res.sendFile(file, false);
	}
	
}
