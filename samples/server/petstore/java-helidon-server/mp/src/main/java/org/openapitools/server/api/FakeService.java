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

import java.math.BigDecimal;
import org.openapitools.server.model.Client;
import org.openapitools.server.model.EnumClass;
import java.io.File;
import org.openapitools.server.model.FileSchemaTestClass;
import org.openapitools.server.model.HealthCheckResult;
import java.time.LocalDate;
import java.util.Map;
import java.time.OffsetDateTime;
import org.openapitools.server.model.OuterComposite;
import org.openapitools.server.model.OuterObjectWithEnumProperty;
import org.openapitools.server.model.Pet;
import org.openapitools.server.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/fake")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaHelidonServerCodegen")
public class FakeService {

    @GET
    @Path("/health")
    @Produces({ "application/json" })
    public Response fakeHealthGet() {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/http-signature-test")
    @Consumes({ "application/json", "application/xml" })
    public Response fakeHttpSignatureTest(@Valid @NotNull Pet pet,@QueryParam("query_1") String query1,@HeaderParam("header_1")  String header1) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/outer/boolean")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    public Response fakeOuterBooleanSerialize(@Valid Boolean body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/outer/composite")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    public Response fakeOuterCompositeSerialize(@Valid OuterComposite outerComposite) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/outer/number")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    public Response fakeOuterNumberSerialize(@Valid BigDecimal body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/outer/string")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    public Response fakeOuterStringSerialize(@Valid String body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/property/enum-int")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    public Response fakePropertyEnumIntegerSerialize(@Valid @NotNull OuterObjectWithEnumProperty outerObjectWithEnumProperty) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/body-with-binary")
    @Consumes({ "image/png" })
    public Response testBodyWithBinary(@Valid File body) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/body-with-file-schema")
    @Consumes({ "application/json" })
    public Response testBodyWithFileSchema(@Valid @NotNull FileSchemaTestClass fileSchemaTestClass) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/body-with-query-params")
    @Consumes({ "application/json" })
    public Response testBodyWithQueryParams(@QueryParam("query") @NotNull String query,@Valid @NotNull User user) {
        return Response.ok().entity("magic!").build();
    }

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response testClientModel(@Valid @NotNull Client client) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Consumes({ "application/x-www-form-urlencoded" })
    public Response testEndpointParameters(@FormParam(value = "number")  BigDecimal number,@FormParam(value = "double")  Double _double,@FormParam(value = "pattern_without_delimiter")  String patternWithoutDelimiter,@FormParam(value = "byte")  byte[] _byte,@FormParam(value = "integer")  Integer integer,@FormParam(value = "int32")  Integer int32,@FormParam(value = "int64")  Long int64,@FormParam(value = "float")  Float _float,@FormParam(value = "string")  String string, @FormParam(value = "binary") InputStream binaryInputStream,@FormParam(value = "date")  LocalDate date,@FormParam(value = "dateTime")  OffsetDateTime dateTime,@FormParam(value = "password")  String password,@FormParam(value = "callback")  String paramCallback) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Consumes({ "application/x-www-form-urlencoded" })
    public Response testEnumParameters(@HeaderParam("enum_header_string_array")  List<String> enumHeaderStringArray,@HeaderParam("enum_header_string")  @DefaultValue("-efg") String enumHeaderString,@QueryParam("enum_query_string_array") List<String> enumQueryStringArray,@QueryParam("enum_query_string") @DefaultValue("-efg") String enumQueryString,@QueryParam("enum_query_integer") Integer enumQueryInteger,@QueryParam("enum_query_double") Double enumQueryDouble,@QueryParam("enum_query_model_array") List<EnumClass> enumQueryModelArray,@FormParam(value = "enum_form_string_array")  List<String> enumFormStringArray,@FormParam(value = "enum_form_string")  String enumFormString) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    public Response testGroupParameters(@QueryParam("required_string_group") @NotNull Integer requiredStringGroup,@HeaderParam("required_boolean_group") @NotNull  Boolean requiredBooleanGroup,@QueryParam("required_int64_group") @NotNull Long requiredInt64Group,@QueryParam("string_group") Integer stringGroup,@HeaderParam("boolean_group")  Boolean booleanGroup,@QueryParam("int64_group") Long int64Group) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/inline-additionalProperties")
    @Consumes({ "application/json" })
    public Response testInlineAdditionalProperties(@Valid @NotNull Map<String, String> requestBody) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/jsonFormData")
    @Consumes({ "application/x-www-form-urlencoded" })
    public Response testJsonFormData(@FormParam(value = "param")  String param,@FormParam(value = "param2")  String param2) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/test-query-parameters")
    public Response testQueryParameterCollectionFormat(@QueryParam("pipe") @NotNull List<String> pipe,@QueryParam("ioutil") @NotNull List<String> ioutil,@QueryParam("http") @NotNull List<String> http,@QueryParam("url") @NotNull List<String> url,@QueryParam("context") @NotNull List<String> context,@QueryParam("allowEmpty") @NotNull String allowEmpty,@QueryParam("language") Map<String, String> language) {
        return Response.ok().entity("magic!").build();
    }
}
