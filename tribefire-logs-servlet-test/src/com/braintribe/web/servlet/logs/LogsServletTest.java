package com.braintribe.web.servlet.logs;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;

import com.braintribe.utils.FileTools;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.utils.stream.api.StreamPipe;
import com.braintribe.utils.stream.file.FileBackedPipeFactory;

public class LogsServletTest {

	@Test
	public void testLogCombining() throws Exception {
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

		FileBackedPipeFactory pipeFactory = new FileBackedPipeFactory();

		LogsServlet bean = new LogsServlet();
		bean.setStreamPipeFactory(pipeFactory);

		StreamPipe pipe1 = pipeFactory.newPipe("input1");
		try (OutputStream os = pipe1.acquireOutputStream()) {
			os.write(input1.getBytes(StandardCharsets.UTF_8));
		}
		LogStreamPipe lsp1 = new LogStreamPipe(0, "text/plain", "console_buffer.log", pipe1);

		StreamPipe pipe2 = pipeFactory.newPipe("input2");
		try (OutputStream os = pipe2.acquireOutputStream()) {
			os.write(input2.getBytes(StandardCharsets.UTF_8));
		}
		LogStreamPipe lsp2 = new LogStreamPipe(1, "text/plain", "console_buffer.log", pipe2);

		StreamPipe mergedStreamPipe = bean.mergeLogs(List.of(lsp1, lsp2));

		try (ZipInputStream zis = new ZipInputStream(mergedStreamPipe.openInputStream())) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String filename = FileTools.getName(entry.getName());
				assertThat(filename).isEqualTo("console_buffer.log");

				try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
					IOTools.transferBytes(zis, out);

					String output = out.toString(StandardCharsets.UTF_8);

					List<String> lines = StringTools.getLines(output);

					for (int i = 0; i < 6; ++i) {
						assertThat(lines.get(i)).contains("Line " + (i + 1));
					}
				}

			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Test
	public void testLogCombiningWithAdditionalLines() throws Exception {
		String input1 = """
				2025-10-29T08:39:58.150+0000 FINE    Line 1
				2025-10-29T08:41:58.303+0000 FINE    Line 5
				  Some additional line of Line 5
				  Another additional line of Line 5
				2025-10-29T08:41:58.304+0000 FINE    Line 6
				""";
		String input2 = """
				2025-10-29T08:40:43.374+0000 FINE    Line 2
				2025-10-29T08:40:43.537+0000 FINE    Line 3
				2025-10-29T08:40:43.538+0000 FINE    Line 4
				""";

		FileBackedPipeFactory pipeFactory = new FileBackedPipeFactory();

		LogsServlet bean = new LogsServlet();
		bean.setStreamPipeFactory(pipeFactory);

		StreamPipe pipe1 = pipeFactory.newPipe("input1");
		try (OutputStream os = pipe1.acquireOutputStream()) {
			os.write(input1.getBytes(StandardCharsets.UTF_8));
		}
		LogStreamPipe lsp1 = new LogStreamPipe(0, "text/plain", "console_buffer.log", pipe1);

		StreamPipe pipe2 = pipeFactory.newPipe("input2");
		try (OutputStream os = pipe2.acquireOutputStream()) {
			os.write(input2.getBytes(StandardCharsets.UTF_8));
		}
		LogStreamPipe lsp2 = new LogStreamPipe(1, "text/plain", "console_buffer.log", pipe2);

		StreamPipe mergedStreamPipe = bean.mergeLogs(List.of(lsp1, lsp2));

		try (ZipInputStream zis = new ZipInputStream(mergedStreamPipe.openInputStream())) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String filename = FileTools.getName(entry.getName());
				assertThat(filename).isEqualTo("console_buffer.log");

				try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
					IOTools.transferBytes(zis, out);

					String output = out.toString(StandardCharsets.UTF_8);

					List<String> lines = StringTools.getLines(output);

					assertThat(lines.get(0)).contains("Line 1");
					assertThat(lines.get(1)).contains("Line 2");
					assertThat(lines.get(2)).contains("Line 3");
					assertThat(lines.get(3)).contains("Line 4");
					assertThat(lines.get(4)).contains("Line 5");
					assertThat(lines.get(5)).contains("Some additional line of Line 5");
					assertThat(lines.get(6)).contains("Another additional line of Line 5");
					assertThat(lines.get(7)).contains("Line 6");
				}

			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
