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
        ProcessReader reader = runMavenProcessAndWait("package");
        Path executableJar = outputPath.resolve("target/openapi-java-server.jar");

        assertThat(reader.readOutputConsole(), containsString("BUILD SUCCESS"));
        assertThat(Files.exists(executableJar), is(true));
    }

    @Test
    void buildProjectInterfaceOnly() {
        generate(createConfigurator().addAdditionalProperty(FunctionalBase.INTERFACE_ONLY, "true"));
        ProcessReader reader = runMavenProcessAndWait("package");
        String output = reader.readOutputConsole();

        assertThat(output, containsString("BUILD SUCCESS"));
        assertThat(output, containsString("Errors: 0"));
        assertThat(output, containsString("Failures: 0"));
        assertThat(output, containsString("Skipped: 0"));
    }
}
