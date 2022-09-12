/*
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openapitools.server.api;

import org.openapitools.server.model.FooGetDefaultResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/foo")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaHelidonServerCodegen")
public class DefaultServiceImpl extends DefaultService {

    @GET
    @Produces({ "application/json" })
    public Response fooGet() {
        return Response.ok().entity("magic!").build();
    }
}
