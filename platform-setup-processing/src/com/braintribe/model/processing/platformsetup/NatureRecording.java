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
package com.braintribe.model.processing.platformsetup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.asset.natures.PlatformAssetNature;

public class NatureRecording extends ManipulationRecording {
	
	public static void stringify(File file, PlatformAssetNature nature) {
		
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
			
			stringify(writer, nature);
			
		} catch (Exception e) {
			throw Exceptions.unchecked(e,"Error while serializing nature as manipulations to " + file.getAbsolutePath());
		}
		
	}

	public static String stringify(PlatformAssetNature nature) {
		
		StringWriter writer = new StringWriter();
		
		stringify(writer, nature);
		
		return writer.toString();
	}
	
	public static void stringify(Writer writer, PlatformAssetNature nature) {
		stringify(writer, nature, "$nature", "$natureType");
	}
	
}
