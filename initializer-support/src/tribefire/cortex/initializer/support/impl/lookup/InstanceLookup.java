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
package tribefire.cortex.initializer.support.impl.lookup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

 /**
 * This annotation marks a wire contract indicating that it contains managed instances to be looked
 * up.
 *
 * <p>
 * In cooperation with this annotation, a managed instance within the contract can be marked with {@link GlobalId}. If
 * set, the lookup will not check for the globalId being constructed by default globalId calculation, but for the
 * given globalId. <br />
 * (Default globalId pattern: wire://WireModuleSimpleName|InitializerId/WireSpace/managedInstance )
 * 
 * <p>
 * In case <code>lookupOnly</code> is set to <code>true</code>, all managed instances are handled as lookup instances.
 * 
 * <p>
 * Property <code>globalIdPrefix</code> can be set to avoid redundancy in case annotation {@link GlobalId} share
 * commonalities.
 * 
 * <p>
 * <b>Attention: </b>The contract must contain managed instances only!
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface InstanceLookup {
	boolean lookupOnly() default false;
	String globalIdPrefix() default "";
}
