// ============================================================================
package tribefire.platform.impl.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class MockServletOutputStream extends ServletOutputStream {

	private final ByteArrayOutputStream baos;

	public MockServletOutputStream(ByteArrayOutputStream baos) {
		this.baos = baos;
	}

	@Override
	public void write(int data) throws IOException {
		baos.write(data);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// Intentionally left empty
	}

}