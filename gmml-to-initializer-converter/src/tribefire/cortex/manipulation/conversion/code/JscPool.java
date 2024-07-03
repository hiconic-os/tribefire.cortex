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
package tribefire.cortex.manipulation.conversion.code;

import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.braintribe.model.folder.Folder;
import com.braintribe.model.generic.value.type.DynamicallyTypedDescriptor;
import com.braintribe.model.workbench.WorkbenchPerspective;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.api.util.Lists;
import com.braintribe.wire.api.util.Maps;
import com.braintribe.wire.api.util.Sets;

import tribefire.cortex.initializer.support.impl.lookup.GlobalId;
import tribefire.cortex.initializer.support.impl.lookup.InstanceLookup;
import tribefire.cortex.initializer.support.wire.space.AbstractInitializerSpace;
import tribefire.cortex.sourcewriter.JavaSourceClass;

/**
 * @author peter.gazdik
 */
public class JscPool {

	// We only index acquired Jsc's,
	private final Map<String, JavaSourceClass> fullNameToJsc = newMap();

	// Common
	public final JavaSourceClass dateJsc = createJsc(Date.class);
	public final JavaSourceClass calendarJsc = createJsc(Calendar.class);
	public final JavaSourceClass timeZoneJsc = createJsc(TimeZone.class);

	// For Space
	public final JavaSourceClass managedAnnoJsc = createJsc(Managed.class);
	public final JavaSourceClass importAnnoJsc = createJsc(Import.class);
	public final JavaSourceClass abstractInitializerSpaceJsc = createJsc(AbstractInitializerSpace.class);

	public final JavaSourceClass mapsJsc = createJsc(Maps.class);
	public final JavaSourceClass listsJsc = createJsc(Lists.class);
	public final JavaSourceClass setsJsc = createJsc(Sets.class);

	// For Lookup Contract
	public final JavaSourceClass globalIdAnnoJsc = createJsc(GlobalId.class);
	public final JavaSourceClass instanceLookupAnnoJsc = createJsc(InstanceLookup.class);
	public final JavaSourceClass wireSpaceJsc = createJsc(WireSpace.class);

	// For WB
	public final JavaSourceClass folderJsc = createJsc(Folder.class);
	public final JavaSourceClass wbPerscpectiveJsc = createJsc(WorkbenchPerspective.class);

	
	private JavaSourceClass createJsc(Class<?> clazz) {
		JavaSourceClass jsc = JavaSourceClass.create(clazz);
		fullNameToJsc.put(clazz.getName(), jsc);

		return jsc;
	}

	public JavaSourceClass acquireJsc(DynamicallyTypedDescriptor ref) {
		return acquireJsc(ref.getTypeSignature());
	}

	public JavaSourceClass acquireJsc(Enum<?> enumValue) {
		return acquireJsc(enumValue.getClass().getName());
	}

	public JavaSourceClass acquireJsc(String typeSignature) {
		return fullNameToJsc.computeIfAbsent(typeSignature, ts -> JavaSourceClass.build(ts).isInterface(true).please());
	}

}
