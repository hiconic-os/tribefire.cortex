package com.braintribe.web.servlet.logs;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LogCombinerTest {

	@Test
	public void testLogCombiner() throws Exception {
		String input1 = """
				2025-10-29T08:39:58.150+0000 FINE    Line 1
				2025-10-29T08:41:58.303+0000 FINE    Line 5
				2025-10-29T08:41:58.304+0000 FINE    Line 6
				""";
		String input2 = """
				2025-10-29T08:40:43.374+0000 FINE    Line 2
				2025-10-29T08:40:43.537+0000 FINE    Line 3
				2025-10-29T08:40:43.538+0000 FINE    Line 4
				""";

		List<String> result = new ArrayList<>();
		try (InputStream in1 = new ByteArrayInputStream(input1.getBytes(StandardCharsets.UTF_8));
				InputStream in2 = new ByteArrayInputStream(input2.getBytes(StandardCharsets.UTF_8))) {
			LogCombiner bean = new LogCombiner(List.of(in1, in2));

			while (bean.hasNext()) {
				result.add(bean.next());
			}
		}

		for (int i = 0; i < 6; ++i) {
			assertThat(result.get(i)).contains("Line " + (i + 1));
		}

	}
}
