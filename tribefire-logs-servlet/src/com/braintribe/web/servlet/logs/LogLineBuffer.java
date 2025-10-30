package com.braintribe.web.servlet.logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogLineBuffer {
	private BufferedReader reader;
	private LinkedList<String> bufferedLines = new LinkedList<>();
	private int maxBufferSize = 1000;

	public LogLineBuffer(BufferedReader reader) {
		this.reader = reader;
		fillBuffer();
	}

	private void fillBuffer() {
		while (bufferedLines.size() < maxBufferSize && reader != null) {
			try {
				String line = reader.readLine();
				if (line == null) {
					// End of reader reached
					reader = null;
				} else {
					bufferedLines.add(line);
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

	}

	public String getNextLinesUntilMatching(Pattern pattern) {
		fillBuffer();
		if (bufferedLines.isEmpty()) {
			return null;
		}
		int nextIndex = 1;
		for (; nextIndex < bufferedLines.size(); ++nextIndex) {
			String line = bufferedLines.get(nextIndex);
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				break;
			}
		}
		if (nextIndex == 1) {
			return bufferedLines.remove(0);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nextIndex; ++i) {
			if (i > 0) {
				sb.append('\n');
			}
			sb.append(bufferedLines.remove(0));
		}
		return sb.toString();
	}
}
