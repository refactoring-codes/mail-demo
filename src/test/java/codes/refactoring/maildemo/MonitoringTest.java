package codes.refactoring.maildemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMetrics
@ActiveProfiles("test")
class MonitoringTest {

	@LocalServerPort
	private int port;

	@Autowired
	private WebApplicationContext context;

	@Test
	void healthEndpointWithMail() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();

		MockHttpServletResponse response = mvc.perform(get("http://localhost:" + port + "/actuator/health")).andReturn()
				.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

		System.out.println(response.getContentAsString());
		JSONObject jo = new JSONObject(response.getContentAsString());

		JSONObject componentsJo = jo.getJSONObject("components");
		JSONObject mailJo = componentsJo.getJSONObject("mail");
		assertEquals("UP", mailJo.getString("status"));

	}

}
