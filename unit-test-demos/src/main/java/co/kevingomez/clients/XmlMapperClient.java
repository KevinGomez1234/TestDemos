package co.kevingomez.clients;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class XmlMapperClient  {

	public Optional<String> getRequest(String host) {
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(host + "string.php", String.class);
		if(result != null) {
			return Optional.of(result);
		}
		return Optional.empty();
	}

}
