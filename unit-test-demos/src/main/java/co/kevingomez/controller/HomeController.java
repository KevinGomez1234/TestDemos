package co.kevingomez.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kevingomez.clients.XmlMapperClient;

@RestController
public class HomeController {
	private final XmlMapperClient xmlClient;
	public HomeController(XmlMapperClient xmlClient) {
		this.xmlClient = xmlClient;
	}
	@GetMapping("/")
	public String home() {
		String res = xmlClient.getRequest("http://192.168.0.13/").get();
		return "Hello World! " + res;
	}
}
