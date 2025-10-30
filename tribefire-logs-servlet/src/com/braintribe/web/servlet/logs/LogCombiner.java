package com.braintribe.web.servlet.logs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.braintribe.utils.lcd.StringTools;

public class LogCombiner implements Iterator<String> {

	private LogLineBuffer[] readers;
	private NextLine[] nextLines;
	private Pattern singleLinePattern;
	private Pattern multiLinePattern;

	public LogCombiner(List<InputStream> inputStreams) {
		singleLinePattern = Pattern.compile("^([0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\.[0-9]{3}\\+[0-9]{4}) (.*)");
		multiLinePattern = Pattern.compile("^([0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\.[0-9]{3}\\+[0-9]{4}) (.*)",
				Pattern.MULTILINE | Pattern.DOTALL);
		// patterns.add(Pattern.compile("\\[[0-9]{2}\\/[a-zA-Z]{3}\\/[0-9]{4}\\:[0-9]{2}\\:[0-9]{2}\\:[0-9]{2} \\+[0-9]{4}]")); // Let's not use this
		// regex for the moment as the value cannot be sorted as Strings

		readers = new LogLineBuffer[inputStreams.size()];
		for (int i = 0; i < inputStreams.size(); ++i) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreams.get(i), StandardCharsets.UTF_8));
			readers[i] = new LogLineBuffer(reader);
		}
		nextLines = new NextLine[inputStreams.size()];
		fillNextLines();
	}

	private record NextLine(int idx, String key, String remainder) {

	}

	private void fillNextLines() {
		for (int i = 0; i < readers.length; ++i) {
			if (readers[i] == null) {
				if (nextLines[i] != null) {
					nextLines[i] = null;
				}
				continue;
			}
			if (nextLines[i] == null) {
				String line = readers[i].getNextLinesUntilMatching(singleLinePattern);
				if (line == null) {
					readers[i] = null;
					nextLines[i] = null;
				} else {
					Matcher matcher = multiLinePattern.matcher(line);
					if (matcher.matches()) {
						String key = matcher.group(1);
						String remainder = matcher.group(2);
						nextLines[i] = new NextLine(i, key, remainder);
					} else {
						readers[i] = null;
						nextLines[i] = null;
					}
				}
			}
		}
	}

	@Override
	public boolean hasNext() {
		return Arrays.stream(nextLines).filter(Objects::nonNull).filter(s -> !StringTools.isBlank(s.remainder)).count() > 0;
	}

	@Override
	public String next() {
		List<NextLine> availableLines = new ArrayList<>(
				Arrays.stream(nextLines).filter(Objects::nonNull).filter(nl -> !StringTools.isBlank(nl.remainder)).toList());
		if (availableLines.isEmpty()) {
			throw new IllegalStateException("No more available lines");
		}
		availableLines.sort(new Comparator<NextLine>() {
			@Override
			public int compare(NextLine o1, NextLine o2) {
				return o1.key.compareTo(o2.key);
			}
		});
		NextLine nextLine = availableLines.get(0);
		nextLines[nextLine.idx] = null;

		fillNextLines();

		return nextLine.key + " - " + nextLine.idx + " - " + nextLine.remainder;
	}

}
