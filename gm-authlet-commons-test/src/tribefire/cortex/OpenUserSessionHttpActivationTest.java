package tribefire.cortex;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

import com.braintribe.model.security.service.config.HttpRequestSelector;
import com.braintribe.model.security.service.config.OpenUserSessionConfiguration;
import com.braintribe.model.security.service.config.OpenUserSessionEntryPoint;
import com.braintribe.testing.junit.assertions.assertj.core.api.Assertions;
import com.braintribe.web.servlet.auth.OpenUserSessionConfigurationProvider;
import com.braintribe.web.servlet.auth.providers.OpenUserSessionConfigurationProviderImpl;

public class OpenUserSessionHttpActivationTest {
	private static final String ORIGIN_UNKNOWN = "origin-unknown";
	private static final String ORIGIN_KNOWN = "origin";
	private static final String HOST_KNOWN = "host";
	private static final String HOST_UNKNOWN = "host-unknown";
	private static OpenUserSessionConfigurationProvider provider = buildProvider();

	@Test
	public void testHostAndOrigin() {
		String name = provider.findEntryPoint(buildRequest(null, HOST_KNOWN, ORIGIN_KNOWN)).getName();
		Assertions.assertThat(name).isEqualTo("host_origin");
	}
	
	@Test
	public void testHostAndNull() {
		String name = provider.findEntryPoint(buildRequest(null, HOST_KNOWN, null)).getName();
		Assertions.assertThat(name).isEqualTo("host_wc");
	}
	
	@Test
	public void testForwardedHostAndNull() {
		String name = provider.findEntryPoint(buildRequest(HOST_KNOWN, null, null)).getName();
		Assertions.assertThat(name).isEqualTo("host_wc");
	}
	
	@Test
	public void testHostAndUnkown() {
		String name = provider.findEntryPoint(buildRequest(null, HOST_KNOWN, ORIGIN_UNKNOWN)).getName();
		Assertions.assertThat(name).isEqualTo("host_wc");
	}
	
	@Test
	public void testNullAndOrigin() {
		String name = provider.findEntryPoint(buildRequest(null, null, ORIGIN_KNOWN)).getName();
		Assertions.assertThat(name).isEqualTo("wc_origin");
	}
	
	@Test
	public void testUnknownAndOrigin() {
		String name = provider.findEntryPoint(buildRequest(null, HOST_UNKNOWN, ORIGIN_KNOWN)).getName();
		Assertions.assertThat(name).isEqualTo("wc_origin");
	}
	
	@Test
	public void testNullAndNull() {
		String name = provider.findEntryPoint(buildRequest(null, null, null)).getName();
		Assertions.assertThat(name).isEqualTo("wc_wc");
	}
	
	@Test
	public void testNullAndExplicitNull() {
		String name = provider.findEntryPoint(buildRequest(null, null, "null")).getName();
		Assertions.assertThat(name).isEqualTo("wc_wc");
	}
	
	@Test
	public void testUnknownAndUnknown() {
		String name = provider.findEntryPoint(buildRequest(null, HOST_UNKNOWN, ORIGIN_UNKNOWN)).getName();
		Assertions.assertThat(name).isEqualTo("wc_wc");
	}
	
	private HttpServletRequest buildRequest(String xForwaredHost, String host, String origin) {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-Host")).thenReturn(xForwaredHost);
		Mockito.when(request.getHeader("Host")).thenReturn(host);
		Mockito.when(request.getHeader("Origin")).thenReturn(origin);
		
		return request;
	}
	
	private static OpenUserSessionConfigurationProviderImpl buildProvider() {
		OpenUserSessionConfigurationProviderImpl provider = new OpenUserSessionConfigurationProviderImpl(5);
		
		provider.setOpenUserSessionConfiguration(buildConfig());
		
		return provider;
	}
	
	private static OpenUserSessionConfiguration buildConfig() {
		OpenUserSessionConfiguration config = OpenUserSessionConfiguration.T.create();
		
		config.getEntryPoints().addAll(Arrays.asList( //
			entryPoint("wc_origin", selector("*", ORIGIN_KNOWN)), //
			entryPoint("wc_origin_low_prio", selector("*", ORIGIN_KNOWN)), //
			entryPoint("host_wc", selector(HOST_KNOWN, "*")), //
			entryPoint("host_wc_low_prio", selector(HOST_KNOWN, "*")), //
			entryPoint("host_origin", selector(HOST_KNOWN, ORIGIN_KNOWN)), //
			entryPoint("host_origin_low_prio", selector(HOST_KNOWN, ORIGIN_KNOWN)), //
			entryPoint("wc_wc", selector("*", "*")), //
			entryPoint("wc_wc_low_prio", selector("*", "*")), //
			entryPoint("empty") //
		));
		
		return config;
	}
	
	private static HttpRequestSelector selector(String host, String origin) {
		HttpRequestSelector selector = HttpRequestSelector.T.create();
		selector.setHost(host);
		selector.setOrigin(origin);
		
		return selector;
	}
	
	private static OpenUserSessionEntryPoint entryPoint(String name, HttpRequestSelector... selectors) {
		OpenUserSessionEntryPoint entryPoint = OpenUserSessionEntryPoint.T.create();
		entryPoint.setName(name);
		
		entryPoint.getHttpActivations().addAll(Arrays.asList(selectors));
		
		return entryPoint;
	}
}
