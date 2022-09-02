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

package org.openapitools.codegen.java.helidon.functional;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionalHelidonMPServerTest extends FunctionalBase {

    @BeforeClass
    public void setup() {
        library("mp");
        generatorName("java-helidon-server");
        inputSpec("src/test/resources/3_0/helidon/petstore-for-testing.yaml");
    }

    @Test
    void buildProjectDefaultOptions() {
        generate();
        buildAndVerify("target/openapi-java-server.jar");
    }

    @Test
    void buildProjectInterfaceOnly() {
        generate(createConfigurator().addAdditionalProperty(INTERFACE_ONLY, "true"));
        buildAndVerify("target/openapi-java-server.jar");
    }

    //TODO remove it or change after MP implements new fullProject option
    @Ignore
    @Test
    void buildProjectAbstractClasses() {
        generate(createConfigurator().addAdditionalProperty(FULL_PROJECT, "false"));
        buildAndVerify("target/openapi-java-server.jar");
    }

    @Test
    void buildFullProject() {
        generate(createConfigurator().addAdditionalProperty(FULL_PROJECT, "true"));
        buildAndVerify("target/openapi-java-server.jar");
    }
}
