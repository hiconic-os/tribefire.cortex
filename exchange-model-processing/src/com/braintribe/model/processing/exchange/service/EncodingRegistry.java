// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.exchange.service;

import java.util.HashMap;
import java.util.Map;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.bin.BinMarshaller;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.codec.marshaller.stax.StaxMarshaller;
import com.braintribe.model.exchangeapi.EncodingType;

public class EncodingRegistry {

	public static EncodingRegistry DEFAULT = new EncodingRegistry().registerMarshaller(EncodingType.xml, StaxMarshaller.defaultInstance, "xml")
			.registerMarshaller(EncodingType.json, new JsonStreamMarshaller(), "json")
			.registerMarshaller(EncodingType.binary, new BinMarshaller(), "bin");

	private Map<EncodingType, EncodingRegistryEntry> entriesByType = new HashMap<>();
	private Map<String, EncodingRegistryEntry> entriesByExtension = new HashMap<>();

	public EncodingRegistry registerMarshaller(EncodingType type, Marshaller marshaller, String extension) {
		EncodingRegistryEntry entry = new EncodingRegistryEntry(type, marshaller, extension);
		this.entriesByType.put(type, entry);
		this.entriesByExtension.put(extension, entry);
		return this;
	}

	public EncodingRegistry registerMarshaller(EncodingType type, EncodingRegistryEntry entry) {
		this.entriesByType.put(type, entry);
		return this;
	}

	public EncodingRegistryEntry findEntry(EncodingType type) {
		return entriesByType.get(type);
	}

	public EncodingRegistryEntry getEntry(EncodingType type) {
		EncodingRegistryEntry entry = findEntry(type);
		if (entry == null) {
			throw new RuntimeException("No marshalling entry found for type: " + type);
		}
		return entry;
	}

	public EncodingRegistryEntry findEntryByExtension(String extension) {
		return entriesByExtension.get(extension);
	}

	public EncodingRegistryEntry getEntryByType(String extension) {
		EncodingRegistryEntry entry = findEntryByExtension(extension);
		if (entry == null) {
			throw new RuntimeException("No marshalling entry found for extension: " + extension);
		}
		return entry;
	}

	public class EncodingRegistryEntry {

		private EncodingType type;
		private Marshaller marshaller;
		private String extension;

		public EncodingRegistryEntry(EncodingType type, Marshaller marshaller, String extension) {
			this.type = type;
			this.marshaller = marshaller;
			this.extension = extension;
		}

		public EncodingType getType() {
			return type;
		}

		public Marshaller getMarshaller() {
			return marshaller;
		}

		public String getExtension() {
			return extension;
		}

	}

}
