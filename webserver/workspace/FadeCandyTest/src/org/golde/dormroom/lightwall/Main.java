package org.golde.dormroom.lightwall;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import org.golde.dormroom.lightwall.http.justjson.WebServerNew;
import org.golde.dormroom.lightwall.http.justjson.WebServerOldApp;
import org.golde.dormroom.lightwall.http.justjson.routes.RouteToggleRemoteControl;
import org.golde.dormroom.lightwall.opc.OPCClient;
import org.golde.dormroom.lightwall.scene.Scene;
import org.golde.dormroom.lightwall.scene.SceneBoot;
import org.golde.dormroom.lightwall.scene.SceneHandler;
import org.golde.dormroom.lightwall.tester.AnimationTester;

public class Main {

	private static final boolean DEPLOYED_TO_PI = true; //CHANGE ME
	private static final boolean ENABLE_WEB_SERVER_NEW = true;
	private static final boolean ENABLE_WEB_SERVER_OLD = true;

	private static final boolean ENABLE_LIGHT_WALL = true;
	
	private static final String IP = DEPLOYED_TO_PI ? "127.0.0.1" : "10.0.0.35";
	private static final int PORT_SERVER_NEW = 8001;
	private static final int PORT_SERVER_OLD = 8000;
	private static final boolean ENABLE_TESTER = !DEPLOYED_TO_PI;

	private static final int PORT_OPC = 7890;

	public static SceneHandler animationHandler = new SceneHandler();
	
	//public static AudioPropertiers audioPropertiers = new AudioPropertiers();
	//public static Audio audio = new Audio();;
	
	public static void main(String[] args) throws InterruptedException, IOException {

		String webserverIp = waitForLocalIPAddress();
		System.out.println("Local IP Address: " + webserverIp);
		//Thread.sleep(5000);

		OPCClient opc = new OPCClient(IP, PORT_OPC);
		AnimationTester animTester = null;
		if(ENABLE_TESTER) {
			animTester = new AnimationTester();
		}
		final AnimationTester ex = animTester;
		

	
		animationHandler.setScene(new SceneBoot());
		//animationHandler.setScene(new ArtnetReciever());
		
		if(ENABLE_TESTER) {
			EventQueue.invokeLater(() -> {
				ex.setVisible(true);
			});
		}

		if(ENABLE_WEB_SERVER_NEW) {
			WebServerNew webServer = new WebServerNew();
			webServer.start(webserverIp, PORT_SERVER_NEW);
		}

		if(ENABLE_WEB_SERVER_OLD) {
			WebServerOldApp webServer = new WebServerOldApp(animationHandler);
			webServer.start(webserverIp, PORT_SERVER_OLD);
		}
		
		//animationHandler.setLastSceneAfterBoot();

		log("Running.");
		log("Press ENTER to exit.");
		while (System.in.available() == 0) {
			Scene animation = animationHandler.getAnimation();
			animation.reset();
			animation.draw();
			
			if(ENABLE_LIGHT_WALL) {
				if (!RouteToggleRemoteControl.isRemoteControlEnabled()) {
					opc.animate(animation);
				}
			}
			if(ENABLE_TESTER) {
				ex.panel.updateFromAnimation(animation);
			}
			
			Thread.sleep(33);
		}

		opc.clear(animationHandler.getAnimation());
		opc.close();

		System.exit(0);

	}
	
	// Wait until we have a local IP address, then return it.
	public static String waitForLocalIPAddress() {
		String ip;
		while (true) {
			ip = getLocalIPAddress();
			if (ip != null) {
				return ip;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public static String getLocalIPAddress() {
		try {
	        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets)) {
	            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress instanceof Inet4Address) {
		            	return inetAddress.toString().substring(1);
		            }
		        }
			}
		}
		catch (Exception e) {
			return null;
		}
		
		return null;
    }


	private static void log(String s) {
		System.out.println(s);
	}

}
