# Hiconic platform


## Runtime properties

### General information

* [How to configure runtime properties](https://github.com/hiconic-os/tribefire.cortex.documentation/blob/main/concepts-doc/src/features/runtime_properties.md)
* [Overview of well-known properties](https://github.com/hiconic-os/tribefire.cortex.documentation/blob/main/includes-doc/src/runtime_properties_include.md)


The `well-known properties` above address the legacy monolith [TribefireRuntime](https://github.com/hiconic-os/com.braintribe.gm/blob/main/tribefire-runtime/src/com/braintribe/model/processing/bootstrapping/TribefireRuntime.java)

A newer approach is to define a contract specific for given domain.


### Error Handling

**Properties contract:** [ErrorHandlingRuntimePropertiesContract](https://github.com/hiconic-os/tribefire.cortex/blob/main/tribefire-platform/src/tribefire/platform/wire/contract/ErrorHandlingRuntimePropertiesContract.java)



#### `TRIBEFIRE_ERROR_EXCEPTION_EXPOSURE`

**Type:** [Exposure](https://github.com/hiconic-os/com.braintribe.gm/blob/main/gm-rpc-commons/src/com/braintribe/servlet/exception/StandardExceptionHandler.java)<br>
**Default:** auto

Determines which of error message and stacktrace are exposed to the client in the server response:

* **full** | message and stacktrace
* **messageOnly** | message only 
* **none** | neither
* **auto**: defers to legacy properties (with values `true`/`false`)
  * TRIBEFIRE_EXCEPTION_MESSAGE_EXPOSITION
  * TRIBEFIRE_EXCEPTION_EXPOSITION


#### `TRIBEFIRE_ERROR_TRACEBACKID_EXPOSURE`

**Type:** boolean<br>
**Default:** true

If true, a `tracebackid` is exposed in the server response in the form of a random UUID.

This `tracebackId` is also appended to the error logs, as its only purpose is to associate a concrete problem with the corresponding log data.

#### `TRIBEFIRE_ERROR_HANDLING_PRE_REASON` (*Deprecated*)

**Type:** boolean<br>
**Default:** true<br>

If true, the legacy mapping of exceptions to HTTP response codes is used.
