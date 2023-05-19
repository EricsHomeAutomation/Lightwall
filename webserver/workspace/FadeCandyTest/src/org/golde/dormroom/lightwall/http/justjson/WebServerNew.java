package org.golde.dormroom.lightwall.http.justjson;

import java.io.IOException;

import org.golde.dormroom.lightwall.http.justjson.routes.RouteGetPreviewImage;
import org.golde.dormroom.lightwall.http.justjson.routes.RoutePreviewScene;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteSetScene;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteToggleOnOff;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteToggleRemoteControl;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteAddScene;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteDeleteScene;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteGetScenesJSON;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteIndex;
import org.golde.router.Router;

public class WebServerNew {

	public void start(String ip, int port) {
		try {
			Router router = new Router(port);
			router.register(RouteIndex.class);
			
			router.register(RouteToggleOnOff.class);
			router.register(RouteToggleRemoteControl.class);
			
			router.register(RouteDeleteScene.class);
			router.register(RouteAddScene.class);
			router.register(RoutePreviewScene.class);
			router.register(RouteSetScene.class);
			
			router.register(RouteGetPreviewImage.class);
			router.register(RouteGetScenesJSON.class);
			
			
			router.start();
			System.out.println("NEW Router started on: " + ip + ":" + port);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
