package com.braintribe.web.servlet.logs;

import com.braintribe.utils.stream.api.StreamPipe;

public record LogStreamPipe(int index, String mimeType, String filename, StreamPipe streamPipe) {
}