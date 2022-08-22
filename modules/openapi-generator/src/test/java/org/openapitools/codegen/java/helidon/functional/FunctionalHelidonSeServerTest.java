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
import org.testng.annotations.Test;

public class FunctionalHelidonSeServerTest extends FunctionalBase {

    @BeforeClass
    public void setup() {
        library("se");
        generatorName("java-helidon-server");
    }

    @Test
    void buildPetstoreWithDefaultOptions() {
        generate("src/test/resources/3_0/petstore.yaml");
        buildAndVerify("target/openapi-java-server.jar");
    }

    @Test
    void buildPetstoreWithInterfaceOnly() {
        inputSpec("src/test/resources/3_0/petstore.yaml");
        generate(createConfigurator().addAdditionalProperty(FunctionalBase.INTERFACE_ONLY, "true"));
        buildAndVerify("target/openapi-java-server.jar");
    }

    @Test
    void buildPetstoreWithFullProject() {
        inputSpec("src/test/resources/3_0/petstore.yaml");
        generate(createConfigurator().addAdditionalProperty("fullProject", "true"));
        buildAndVerify("target/openapi-java-server.jar");
    }
}
