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
package com.braintribe.model.processing.management.impl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.meta.GmCollectionType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmLinearCollectionType;
import com.braintribe.model.meta.GmMapType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.GmTypeKind;

public class MetaModelDependencyRegistry {
	
	private final Map<GmType, Set<DependencyLink>> dependencies = new HashMap<GmType, Set<DependencyLink>>();

	public MetaModelDependencyRegistry(GmMetaModel metaModel) {
//		Set<GmEntityType> entityTypes = metaModel.getEntityTypes();
//		if (entityTypes != null) {
//			for (GmEntityType entityType : entityTypes) {
//				enumerateDependenciesInternal(entityType);
//			}
//		}
	}
	
	private void enumerateDependenciesInternal(GmEntityType entityType) {
		if (entityType == null) {
			return;
		}
		
		List<GmEntityType> superTypes = entityType.getSuperTypes();
		if (superTypes != null) {
			for (GmEntityType superType : superTypes) {
				boolean wasMissing = addDependencyLink(superType, entityType, 
						"extended by '" + entityType.getTypeSignature() + "'");
				if (wasMissing) {
					enumerateDependenciesInternal(superType);
				}
			}
		}
		
		List<GmProperty> properties = entityType.getProperties();
		if (properties != null) {
			for (GmProperty property : properties) {
				if (property != null) {					
					GmType propertyType = property.getType();
					
					if ((propertyType != null) && ((propertyType.typeKind() == GmTypeKind.LIST) || (propertyType.typeKind() == GmTypeKind.SET))) {
						GmLinearCollectionType ct = (GmLinearCollectionType) propertyType;
						GmType elementType = ct.getElementType();
						if (elementType instanceof GmCollectionType) {
							//do not check nested collections - they are not allowed
						} else {
							boolean wasMissing = addDependencyLink(elementType, property,
									"property '" + property.getName() + "' of '" + entityType.getTypeSignature() + 
									"' is a collection of this type");
							
							if (wasMissing && (elementType.typeKind() == GmTypeKind.ENTITY)) {
								enumerateDependenciesInternal((GmEntityType) elementType);
							}
						}
					} else if ((propertyType != null) && (propertyType.typeKind() == GmTypeKind.MAP)) {
						GmMapType ct = (GmMapType) propertyType;
						GmType keyType = ct.getKeyType();

						if (keyType instanceof GmCollectionType) {
							//do not check nested collections - they are not allowed
						} else {
							boolean wasMissingKey = addDependencyLink(keyType, property,
									"property '" + property.getName() + "' of '" + entityType.getTypeSignature() + 
									"' is a map with keys of this type");
							if (wasMissingKey && (keyType.typeKind() == GmTypeKind.ENTITY)) {
								enumerateDependenciesInternal((GmEntityType) keyType);
							}
						}
	
						GmType valueType = ct.getValueType();
						if (valueType instanceof GmCollectionType) {
							//do not check nested collections - they are not allowed
						} else {
							boolean wasMissingValue = addDependencyLink(valueType, property,
									"property '" + property.getName() + "' of '" + entityType.getTypeSignature() + 
									"' is a map with values of this type");
		
							if (wasMissingValue && (valueType.typeKind() == GmTypeKind.ENTITY)) {
								enumerateDependenciesInternal((GmEntityType) valueType);
							}
						}
						
					} else { // BASE, SIMPLE, ENTITY, ENUM
						boolean wasMissing = addDependencyLink(propertyType, property,
								"property '" + property.getName() + "' of '" + entityType.getTypeSignature() + "' is of this type");
	
						if (wasMissing && (propertyType != null) && (propertyType.typeKind() == GmTypeKind.ENTITY)) {
							enumerateDependenciesInternal((GmEntityType) propertyType);
						}
					}
				}
			}
		}
		
	}
	
	private boolean addDependencyLink(GmType dependency, GenericEntity dependent, String depDesc) {
		Set<DependencyLink> depLinkSet = dependencies.get(dependency);
		if (depLinkSet == null) {
			depLinkSet = new HashSet<DependencyLink>();
			dependencies.put(dependency, depLinkSet);
		}
		DependencyLink depLink = new DependencyLink();
		depLink.dependent = dependent;
		depLink.description = depDesc;
		return depLinkSet.add(depLink);
	}
	
	public Set<DependencyLink> getDependencyLinks(GmType type) {
		return dependencies.get(type);
	}

	public Set<GmType> getDependencies() {
		return Collections.unmodifiableSet(dependencies.keySet());
	}
	
	public static String describe(Set<DependencyLink> dependencyLinks) {
		return Arrays.toString(extractDescriptions(dependencyLinks).toArray());
	}

	public static Collection<String> extractDescriptions(Set<DependencyLink> dependents) {
		return dependents.stream() //
				.map(d -> d == null ? null : d.description) //
				.collect(Collectors.toList());
	}

	public static class DependencyLink {
		
		public GenericEntity dependent;
		public String description;
		
		@Override
		public int hashCode() {
			return description.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return (this.getClass() == obj.getClass()) 
					&& Objects.equals(this.description, ((DependencyLink)obj).description);
		}
		
	}
}
