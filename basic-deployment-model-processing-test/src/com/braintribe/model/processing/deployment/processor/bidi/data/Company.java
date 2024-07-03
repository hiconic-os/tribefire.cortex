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

import java.util.List;
import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Bidirectional;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface Company extends GenericEntity {

	EntityType<Company> T = EntityTypes.T(Company.class);

	/** 1:1 - {@link Person#getOwnCompany()} */
	@Bidirectional(type = Person.class, property = "ownCompany")
	Person getOwner();
	void setOwner(Person owner);

	/** n:1 - {@link Person#getEmployerCompany()} */
	@Bidirectional(type = Person.class, property = "employerCompany")
	Set<Person> getEmployeeSet();
	void setEmployeeSet(Set<Person> employeeSet);

	/** n:1 - {@link Person#getEmployerCompanyList()} */
	@Bidirectional(type = Person.class, property = "employerCompanyList")
	List<Person> getEmployeeList();
	void setEmployeeList(List<Person> employeeList);

	/** n:n - {@link Person#getFriendsCompanies()} */
	@Bidirectional(type = Person.class, property = "friendsCompanies")
	Set<Person> getOwnersFriends();
	void setOwnersFriends(Set<Person> ownersFriends);

	String getName();
	void setName(String name);

	Integer getAge();
	void setAge(Integer age);

	int getAge2();
	void setAge2(int age2);

}
