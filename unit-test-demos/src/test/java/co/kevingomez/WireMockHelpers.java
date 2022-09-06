package co.kevingomez;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMockHelpers {
	private static WireMockServer localhost;

	public static void setupHttpServer() {
		localhost = new WireMockServer(WireMockConfiguration.options().dynamicPort());
		localhost.start();
		System.setProperty("wiremock.server.port", Integer.toString(localhost.port()));
		System.out.println("Wiremock server has started on port " + localhost.port());
	}

	public static void resetHttpServer() {
		localhost.resetAll();
	}

	public static void stopHttpServer() {
		localhost.stop();
	}
	
	public static int httpPort() {
		return localhost.port();
	}
	
	public static WireMockServer getHttpServer() {
		return localhost;
	}

}
