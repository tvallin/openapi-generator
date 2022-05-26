/*
 * Copyright (c) 2022 Oracle and/or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import io.swagger.v3.oas.models.media.Schema;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.meta.features.DocumentationFeature;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.OperationsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaHelidonServerCodegen extends JavaHelidonCommonCodegen {

    private final Logger LOGGER = LoggerFactory.getLogger(JavaHelidonServerCodegen.class);

    protected String serializationLibrary = null;

    public JavaHelidonServerCodegen() {
        modifyFeatureSet(features -> features.includeDocumentationFeatures(DocumentationFeature.Readme));

        outputFolder = "generated-code" + File.separator + "java";
        templateDir = "java-helidon" + File.separator + "server";
        embeddedTemplateDir = "java-helidon" + File.separator + "common";
        invokerPackage = "org.openapitools.server";
        artifactId = "openapi-java-server";
        apiPackage = invokerPackage + ".api";
        modelPackage = invokerPackage + ".model";

        updateOption(CodegenConstants.INVOKER_PACKAGE, invokerPackage);
        updateOption(CodegenConstants.ARTIFACT_ID, artifactId);
        updateOption(CodegenConstants.API_PACKAGE, apiPackage);
        updateOption(CodegenConstants.MODEL_PACKAGE, modelPackage);

        modelTestTemplateFiles.put("model_test.mustache", ".java");

        cliOptions.add(CliOption.newBoolean(USE_BEANVALIDATION, "Use BeanValidation API annotations"));
        cliOptions.add(CliOption.newBoolean(PERFORM_BEANVALIDATION, "Perform BeanValidation"));

        supportedLibraries.put(HELIDON_MP, "Helidon MP Server");
        supportedLibraries.put(HELIDON_SE, "Helidon SE Server");
        supportedLibraries.put(HELIDON_NIMA, "Helidon NIMA Server");
        supportedLibraries.put(HELIDON_NIMA_ANNOTATIONS, "Helidon NIMA Annotations Server");

        CliOption libraryOption = new CliOption(CodegenConstants.LIBRARY,
                "library template (sub-template) to use");
        libraryOption.setEnum(supportedLibraries);
        libraryOption.setDefault(HELIDON_MP);
        cliOptions.add(libraryOption);
        setLibrary(HELIDON_MP);

        CliOption serializationLibrary = new CliOption(CodegenConstants.SERIALIZATION_LIBRARY,
                "Serialization library, defaults to Jackson");
        Map<String, String> serializationOptions = new HashMap<>();
        serializationOptions.put(SERIALIZATION_LIBRARY_JACKSON, "Use Jackson as serialization library");
        serializationOptions.put(SERIALIZATION_LIBRARY_JSONB, "Use JSON-B as serialization library");
        serializationLibrary.setEnum(serializationOptions);
        cliOptions.add(serializationLibrary);
        setSerializationLibrary(SERIALIZATION_LIBRARY_JACKSON);

        this.setLegacyDiscriminatorBehavior(false);
    }

    @Override
    public void setUseBeanValidation(boolean useBeanValidation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setPerformBeanValidation(boolean performBeanValidation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "java-helidon-server";
    }

    @Override
    public String getHelp() {
        return "Generates a Helidon MP or SE server";
    }

    @Override
    public void processOpts() {
        super.processOpts();
        supportingFiles.clear();

        if (!additionalProperties.containsKey(MICROPROFILE_ROOT_PACKAGE_PROPERTY)) {
            additionalProperties.put(MICROPROFILE_ROOT_PACKAGE_PROPERTY, MICROPROFILE_REST_CLIENT_DEFAULT_ROOT_PACKAGE);
        }

        if (additionalProperties.containsKey(CodegenConstants.SERIALIZATION_LIBRARY)) {
            setSerializationLibrary(additionalProperties.get(CodegenConstants.SERIALIZATION_LIBRARY).toString());
        }

        String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");

        if (additionalProperties.containsKey("jsr310") && isLibrary(HELIDON_MP)) {
            supportingFiles.add(new SupportingFile("JavaTimeFormatter.mustache", invokerFolder, "JavaTimeFormatter.java"));
        }

        if (isLibrary(HELIDON_MP)) {
            String apiExceptionFolder = (sourceFolder + File.separator
                    + apiPackage().replace('.', File.separatorChar)).replace('/', File.separatorChar);
            String resourceFolder = "src" + File.separator + "main" + File.separator + "resources";
            String metaInfFolder = resourceFolder + File.separator + "META-INF";
            supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
            supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
            //supportingFiles.add(new SupportingFile("api_exception.mustache", apiExceptionFolder, "ApiException.java"));
            //supportingFiles.add(new SupportingFile("api_exception_mapper.mustache", apiExceptionFolder, "ApiExceptionMapper.java"));
            supportingFiles.add(new SupportingFile("RestApplication.mustache", invokerFolder, "RestApplication.java"));
            supportingFiles.add(new SupportingFile("logging.properties.mustache", resourceFolder, "logging.properties"));
            supportingFiles.add(new SupportingFile("microprofile-config.properties.mustache", metaInfFolder, "microprofile-config.properties"));
            supportingFiles.add(new SupportingFile("beans.xml.mustache", metaInfFolder, "beans.xml"));
        } else if (isLibrary(HELIDON_SE)) {
            throw new UnsupportedOperationException("Not implemented");
        } else if (isLibrary(HELIDON_NIMA)) {
            throw new UnsupportedOperationException("Not implemented");
        } else if (isLibrary(HELIDON_NIMA_ANNOTATIONS)) {
            throw new UnsupportedOperationException("Not implemented");
        } else {
            LOGGER.error("Unknown library option (-l/--library): {}", getLibrary());
        }

        if (getSerializationLibrary() == null) {
            LOGGER.info("No serializationLibrary configured, using '{}' as fallback", SERIALIZATION_LIBRARY_JACKSON);
            setSerializationLibrary(SERIALIZATION_LIBRARY_JACKSON);
        }
        switch (getSerializationLibrary()) {
            case SERIALIZATION_LIBRARY_JACKSON:
                additionalProperties.put(SERIALIZATION_LIBRARY_JACKSON, "true");
                additionalProperties.remove(SERIALIZATION_LIBRARY_JSONB);
                supportingFiles.add(new SupportingFile("RFC3339DateFormat.mustache", invokerFolder, "RFC3339DateFormat.java"));
                break;
            case SERIALIZATION_LIBRARY_JSONB:
                additionalProperties.put(SERIALIZATION_LIBRARY_JSONB, "true");
                additionalProperties.remove(SERIALIZATION_LIBRARY_JACKSON);
                break;
            default:
                additionalProperties.remove(SERIALIZATION_LIBRARY_JACKSON);
                additionalProperties.remove(SERIALIZATION_LIBRARY_JSONB);
                LOGGER.error("Unknown serialization library option");
                break;
        }
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        super.postProcessOperationsWithModels(objs, allModels);
        if (HELIDON_MP.equals(getLibrary())) {
            return AbstractJavaJAXRSServerCodegen.jaxrsPostProcessOperations(objs);
        } else {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    @Override
    public CodegenModel fromModel(String name, Schema model) {
        CodegenModel codegenModel = super.fromModel(name, model);
        if (HELIDON_MP.equals(getLibrary())) {
            // Remove io.swagger.annotations.ApiModel import
            codegenModel.imports.remove("ApiModel");
            codegenModel.imports.remove("ApiModelProperty");
        }
        return codegenModel;
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        generateYAMLSpecFile(objs);
        return super.postProcessSupportingFileData(objs);
    }

    public String getSerializationLibrary() {
        return serializationLibrary;
    }

    public void setSerializationLibrary(String serializationLibrary) {
        if (SERIALIZATION_LIBRARY_JACKSON.equalsIgnoreCase(serializationLibrary)) {
            this.serializationLibrary = SERIALIZATION_LIBRARY_JACKSON;
        } else if (SERIALIZATION_LIBRARY_JSONB.equalsIgnoreCase(serializationLibrary)) {
            this.serializationLibrary = SERIALIZATION_LIBRARY_JSONB;
        } else {
            throw new IllegalArgumentException("Unexpected serializationLibrary value: " + serializationLibrary);
        }
    }

}
