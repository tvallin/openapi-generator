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

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.features.BeanValidationFeatures;
import org.openapitools.codegen.languages.features.PerformBeanValidationFeatures;

public abstract class JavaHelidonCommonCodegen extends AbstractJavaCodegen
        implements BeanValidationFeatures, PerformBeanValidationFeatures {

    static final String HELIDON_MP = "mp";
    static final String HELIDON_SE = "se";

    static final String MICROPROFILE_REST_CLIENT_DEFAULT_ROOT_PACKAGE = "javax";
    static final String HELIDON_NIMA = "nima";
    static final String HELIDON_NIMA_ANNOTATIONS = "nima-annotations";

    static final String MICROPROFILE_ROOT_PACKAGE_PROPERTY = "rootJavaEEPackage";

    static final String SERIALIZATION_LIBRARY_JACKSON = "jackson";
    static final String SERIALIZATION_LIBRARY_JSONB = "jsonb";

    static final String HELIDON_VERSION = "helidonVersion";
    static final String DEFAULT_HELIDON_VERSION = "2.5.2";

    private String helidonVersion;

    @Override
    public void processOpts() {
        super.processOpts();

        String userHelidonVersion = "";
        String userParentVersion = "";

        if (additionalProperties.containsKey(CodegenConstants.PARENT_VERSION)) {
            userParentVersion = additionalProperties.get(CodegenConstants.PARENT_VERSION).toString();
        }

        if (additionalProperties.containsKey(HELIDON_VERSION)) {
            userHelidonVersion = additionalProperties.get(HELIDON_VERSION).toString();
        }

        if (!userHelidonVersion.isEmpty()) {
            if (!userParentVersion.isEmpty() && !userHelidonVersion.equals(userParentVersion)) {
                throw new IllegalArgumentException(
                        String.format("Both %s and %s properties were set with different value.",
                                CodegenConstants.PARENT_VERSION,
                                HELIDON_VERSION));
            }
            setHelidonVersion(userHelidonVersion);
        } else if (!userParentVersion.isEmpty()) {
            setHelidonVersion(userParentVersion);
        } else {
            setHelidonVersion(DEFAULT_HELIDON_VERSION);
        }

        additionalProperties.put(HELIDON_VERSION, helidonVersion);
    }

    private void setHelidonVersion(String version) {
        helidonVersion = version;
        setParentVersion(version);
    }
}
