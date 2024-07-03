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
package com.braintribe.model.processing.deployment.hibernate.data;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


/**
 * Shall not be a top level entity. Bank and CardCompany will be used as member types.
 */

public interface Company extends GenericEntity {

	EntityType<Company> T = EntityTypes.T(Company.class);
	
	List<Employee> getEmployeeList();
	void setEmployeeList(List<Employee> employeeList);

	String getName();
	void setName(String name);

	default Employee hire(String fullName, Long employeeNumber) {
		Employee employee = Employee.T.create();
		employee.setFullName(fullName);
		employee.setId(employeeNumber);

		this.getEmployeeList().add(employee);
		return employee;
	}

}
