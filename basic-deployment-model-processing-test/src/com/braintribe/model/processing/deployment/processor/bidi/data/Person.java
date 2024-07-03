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
package com.braintribe.model.processing.deployment.processor.bidi.data;

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Bidirectional;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface Person extends GenericEntity {

	EntityType<Person> T = EntityTypes.T(Person.class);

	/** 1:1 - {@link Company#getOwner()} */
	@Bidirectional(type = Company.class, property = "owner")
	Company getOwnCompany();
	void setOwnCompany(Company ownCompany);

	/** 1:n - {@link Company#getEmployeeSet()} (Set) */
	@Bidirectional(type = Company.class, property = "employeeSet")
	Company getEmployerCompany();
	void setEmployerCompany(Company employerCompany);

	/** 1:n - {@link Company#getEmployeeList()} (List) */
	@Bidirectional(type = Company.class, property = "employeeList")
	Company getEmployerCompanyList();
	void setEmployerCompanyList(Company employerCompanyList);

	/** n:n - {@link Company#getOwnersFriends()} */
	@Bidirectional(type = Company.class, property = "ownersFriends")
	Set<Company> getFriendsCompanies();
	void setFriendsCompanies(Set<Company> friendsCompanies);

	/** just identifier */
	String getName();
	void setName(String name);

}
