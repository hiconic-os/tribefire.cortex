// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.processing.platformreflection.processor;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Test;

import com.braintribe.utils.FileTools;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.lcd.CollectionTools2;
import com.braintribe.utils.stream.api.StreamPipeFactory;
import com.braintribe.utils.stream.api.StreamPipes;

import net.lingala.zip4j.ZipFile;

public class EncryptedZippingInputStreamProviderTest {

	private List<File> tempFiles = new ArrayList<>();

	@After
	public void after() {
		tempFiles.forEach(FileTools::deleteFileSilently);
		tempFiles.clear();
	}

	@Test
	public void testEncryptedZip() throws Exception {

		StreamPipeFactory pipeFactory = StreamPipes.simpleFactory();
		File tmpFile = generateTempFile("Hello, world");
		Collection<File> files = CollectionTools2.asList(tmpFile);
		String name = "test";
		String password = "operating";

		EncryptedZippingInputStreamProvider provider = new EncryptedZippingInputStreamProvider(pipeFactory, name, files, false, password);
		assertThat(tmpFile).exists();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = provider.openInputStream()) {
			IOTools.transferBytes(in, baos, IOTools.BUFFER_SUPPLIER_64K);
		}
		byte[] zippedData = baos.toByteArray();

		File outFile = File.createTempFile("output", ".zip");
		tempFiles.add(outFile);
		IOTools.inputToFile(new ByteArrayInputStream(zippedData), outFile);

		ZipFile zipFile = new ZipFile(outFile, password.toCharArray());
		assertThat(zipFile.isEncrypted()).isTrue();
		assertThat(zipFile.isValidZipFile()).isTrue();

		File tempFolder = outFile.getParentFile();
		String tempFolderPath = tempFolder.getAbsolutePath();
		zipFile.extractFile(tmpFile.getName(), tempFolderPath, tmpFile.getName() + "-extracted");
		File extractedFile = new File(tempFolderPath, tmpFile.getName() + "-extracted");
		tempFiles.add(extractedFile);
		assertThat(extractedFile).exists();
	}

	@Test
	public void testUnencryptedZip() throws Exception {

		StreamPipeFactory pipeFactory = StreamPipes.simpleFactory();
		File tmpFile = generateTempFile("Hello, world");
		Collection<File> files = CollectionTools2.asList(tmpFile);
		String name = "test";
		String password = null;

		EncryptedZippingInputStreamProvider provider = new EncryptedZippingInputStreamProvider(pipeFactory, name, files, false, password);
		assertThat(tmpFile).exists();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = provider.openInputStream()) {
			IOTools.transferBytes(in, baos, IOTools.BUFFER_SUPPLIER_64K);
		}
		byte[] zippedData = baos.toByteArray();

		File outFile = File.createTempFile("output", ".zip");
		tempFiles.add(outFile);
		IOTools.inputToFile(new ByteArrayInputStream(zippedData), outFile);

		ZipFile zipFile = new ZipFile(outFile);
		assertThat(zipFile.isEncrypted()).isFalse();
		assertThat(zipFile.isValidZipFile()).isTrue();

		File tempFolder = outFile.getParentFile();
		String tempFolderPath = tempFolder.getAbsolutePath();
		zipFile.extractFile(tmpFile.getName(), tempFolderPath, tmpFile.getName() + "-extracted");
		File extractedFile = new File(tempFolderPath, tmpFile.getName() + "-extracted");
		tempFiles.add(extractedFile);
		assertThat(extractedFile).exists();
	}

	@Test
	public void testEncryptedZipWithDelete() throws Exception {

		StreamPipeFactory pipeFactory = StreamPipes.simpleFactory();
		File tmpFile = generateTempFile("Hello, world");
		Collection<File> files = CollectionTools2.asList(tmpFile);
		String name = "test";
		String password = "operating";

		EncryptedZippingInputStreamProvider provider = new EncryptedZippingInputStreamProvider(pipeFactory, name, files, true, password);
		assertThat(tmpFile).doesNotExist();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = provider.openInputStream()) {
			IOTools.transferBytes(in, baos, IOTools.BUFFER_SUPPLIER_64K);
		}
		byte[] zippedData = baos.toByteArray();

		File outFile = File.createTempFile("output", ".zip");
		tempFiles.add(outFile);
		IOTools.inputToFile(new ByteArrayInputStream(zippedData), outFile);

		ZipFile zipFile = new ZipFile(outFile, password.toCharArray());
		assertThat(zipFile.isEncrypted()).isTrue();
		assertThat(zipFile.isValidZipFile()).isTrue();

		File tempFolder = outFile.getParentFile();
		String tempFolderPath = tempFolder.getAbsolutePath();
		zipFile.extractFile(tmpFile.getName(), tempFolderPath, tmpFile.getName() + "-extracted");
		File extractedFile = new File(tempFolderPath, tmpFile.getName() + "-extracted");
		tempFiles.add(extractedFile);
		assertThat(extractedFile).exists();
	}
	private File generateTempFile(String text) throws IOException {
		File tmpFile = File.createTempFile("test", text == null ? ".bin" : ".txt");
		tempFiles.add(tmpFile);
		if (text == null) {
			Random rnd = new Random();
			byte[] data = new byte[1024];
			rnd.nextBytes(data);
			IOTools.inputToFile(new ByteArrayInputStream(data), tmpFile);
		} else {
			FileTools.writeStringToFile(tmpFile, text, "UTF-8");
		}

		return tmpFile;
	}
}
