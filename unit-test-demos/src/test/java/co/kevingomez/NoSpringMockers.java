package co.kevingomez;

import static co.kevingomez.WireMockHelpers.getHttpServer;
import static co.kevingomez.WireMockHelpers.resetHttpServer;
import static co.kevingomez.WireMockHelpers.setupHttpServer;
import static co.kevingomez.WireMockHelpers.httpPort;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import co.kevingomez.clients.XmlMapperClient;
/*
 * If you are using JUnit 4, don’t forget to add @RunWith(SpringRunner.class)to your test, otherwise the annotations will be ignored. If you are using JUnit 5, there’s no need to add the equivalent @ExtendWith(SpringExtension.class) as @SpringBootTest and the other @…Testannotations are already annotated with it
 * @RunWith is an old annotation from JUnit 4 to use test runners. If you're using JUnit 5 (Jupiter), you should use @ExtendWith to use JUnit extensions.
 * The @SpringBootTest annotation loads the complete Spring application context. In contrast, a test slice annotation only loads beans required to test a particular layer. And because of this, we can avoid unnecessary mocking and side effects.
 * @SpringBootTest by default starts searching in the current package of the test class and then searches upwards through the package structure, looking for a class annotated with @SpringBootConfiguration from which it then reads the configuration to create an application context. This class is usually our main application class since the @SpringBootApplication annotation includes the @SpringBootConfiguration annotation. It then creates an application context very similar to the one that would be started in a production environment.
 * 
 * */

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // this will automatically insert the SpringBoot
																// application context, Creates a web application
																// context
public class NoSpringMockers {
	// To use @Mock we must use the MockitoExtensions import
	// So we don't have to explicitly call CarService carService =
	// Mockito.mock(CarService.class); and then
	// Mockito.when(carService.getCars()).thenReturn(Arrays.asList("Toyota"));
	@Mock
	CarService carsClient;
	// Constructor inject the mock
	@InjectMocks
	Person p = new Person();
	@Autowired //Calling the Bean directly
	XmlMapperClient client;

	// Mocking the bean, normally what you would want to do is mock the components
	// inside the Bean that you would want to test
	// i.e you want to test this client, so you would mock the restTemplate and
	// still autowire the client code to get some coverage and test the methods
	// within the client bean
	// for this example we just wanted to show what MockBean is used for which is to
	// mock spring beans
	
//	@MockBean
//	XmlMapperClient client;

	@BeforeAll
	public static void setupBeforeClass() {
		setupHttpServer();
		MockitoAnnotations.openMocks(NoSpringMockers.class); // without this you will get NPE
	}

	@BeforeEach
	public void setup() {
		resetHttpServer();
	}

	@Test
	public void testCarService() {
		List<String> cars = new ArrayList<>();
		cars.add("Toyota");
		when(carsClient.getCars()).thenReturn(cars);
		assertEquals(cars, p.getCars());
		verify(carsClient, atLeastOnce()).getCars();
	}

	@Test
	public void testCarServiceMultipleTimes() {
		List<String> cars = new ArrayList<>();
		cars.add("Toyota");
		when(carsClient.getCars()).thenReturn(cars);
		assertEquals(cars, p.getCars());
		assertEquals(cars, p.getCars());
		assertEquals(cars, p.getCars());
		verify(carsClient, atLeast(3)).getCars();
	}

	// from our local server, will not work if you do not have your local server, we
	// must use Wiremock here for integration tests. Or we could use @Mockbean
	@Test
	public void testClient() {
//		Optional<String> stringOptional = Optional.of("Kevin");
//		when(client.getRequest()).thenReturn(stringOptional);
		String string = client.getRequest("http://192.168.0.13/").get();
		assertEquals(string, "Kevin");
	}

	@Test
	public void testNameSetterWorks() {
		p.setFirstName("Kevin");
		assertEquals(p.getFirstName(), "Kevin");
	}
	/*
	 * Wiremock Examples, instead of having the client hit the actual http server we are hitting the local server, this will build it on any test/prod pipeline
	 * */
	@Test
	public void TestingHttpClient_Should_Return_KevinGomez_String() {
		WireMockServer server = getHttpServer();
		UrlPattern pattern = urlPathEqualTo("/string.php");
		getHttpServer().stubFor(get(urlPathEqualTo("/string.php")).willReturn(aResponse().withHeader("Content-Type", "text/plain").withBody("Hello World!")));
		Optional<String> str = client.getRequest("http://localhost:" + httpPort() + "/");
		System.out.println(str.get());
		server.verify(2, anyRequestedFor(pattern));
	}

}
