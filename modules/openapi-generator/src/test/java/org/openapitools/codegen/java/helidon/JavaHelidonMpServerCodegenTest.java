package org.openapitools.codegen.java.helidon;

import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.openapitools.codegen.java.assertions.JavaFileAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaHelidonMpServerCodegenTest {

    private DefaultGenerator generator;
    private String outputPath;
    private String apiPackage;

    @BeforeMethod
    public void setup() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();
        outputPath = output.getAbsolutePath().replace('\\', '/');
        apiPackage = outputPath + "/src/main/java/org/openapitools/server/api";
        generator = new DefaultGenerator();
    }

    private CodegenConfigurator createConfigurator() {
        return new CodegenConfigurator()
                .setGeneratorName("java-helidon-server")
                .setLibrary("mp")
                .setInputSpec("src/test/resources/3_0/helidon/petstore-for-testing.yaml")
                .setOutputDir(outputPath);
    }

    private void generate(CodegenConfigurator config) {
        generator.opts(config.toClientOptInput());
        generator.generate();
    }

    private void generate() {
        generate(createConfigurator());
    }

    @Test
    public void doGeneratePathParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .fileContains("import java.util.Objects;")
                .assertMethod("deletePet", "ServerRequest", "ServerResponse")
                .bodyContainsLines(
                        "Long petId = Optional.ofNullable(request.path().param(\"petId\")).map(Long::valueOf).orElse" +
                                "(null);",
                        "ValidatorUtils.checkNonNull(petId);"
                )
                .toFileAssert()
                .assertMethod("getPetById")
                .bodyContainsLines(
                        "Long petId = Optional.ofNullable(request.path().param(\"petId\")).map(Long::valueOf).orElse" +
                                "(null);",
                        "ValidatorUtils.checkNonNull(petId);"
                );
    }

    @Test
    public void doGenerateQueryParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .fileContains("import java.util.List;")
                .assertMethod("findPetsByTags")
                .bodyContainsLines(
                        "List<String> tags = Optional.ofNullable(request.queryParams().toMap().get(\"tags\"))" +
                                ".orElse(null);",
                        "ValidatorUtils.checkNonNull(tags);"
                )
                .toFileAssert()
                .assertMethod("findPetsByStatus")
                .bodyContainsLines(
                        "List<String> status = Optional.ofNullable(request.queryParams().toMap().get(\"status\")).orElse" +
                                "(null);",
                        "ValidatorUtils.checkNonNull(status);"
                );
    }

    @Test
    public void doGenerateBodyParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .assertMethod("update")
                .bodyContainsLines(
                        "rules.post(\"/pet\", Handler.create(Pet.class, this::addPet));",
                        "rules.put(\"/pet\", Handler.create(Pet.class, this::updatePet));"
                )
                .toFileAssert()
                .assertMethod("addPet", "ServerRequest", "ServerResponse", "Pet")
                .bodyContainsLines(
                        "ValidatorUtils.checkNonNull(pet);",
                        "handleAddPet(request, response, pet);"
                )
                .toFileAssert()
                .assertMethod("updatePet", "ServerRequest", "ServerResponse", "Pet")
                .bodyContainsLines(
                        "ValidatorUtils.checkNonNull(pet);",
                        "handleUpdatePet(request, response, pet);"
                );

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/UserService.java"))
                .assertMethod("update")
                .bodyContainsLines(
                        "rules.post(\"/user\", Handler.create(User.class, this::createUser));",
                        "rules.post(\"/user/createWithArray\", this::createUsersWithArrayInput);",
                        "rules.post(\"/user/createWithList\", this::createUsersWithListInput);",
                        "rules.put(\"/user/{username}\", Handler.create(User.class, this::updateUser));"
                )
                .toFileAssert()
                .assertMethod("createUser", "ServerRequest", "ServerResponse", "User")
                .bodyContainsLines(
                        "ValidatorUtils.checkNonNull(user);",
                        "handleCreateUser(request, response, user);"
                )
                .toFileAssert()
                .assertMethod("createUsersWithArrayInput", "ServerRequest", "ServerResponse")
                .bodyContainsLines(
                        "Single.create(request.content().as(new GenericType<List<User>>() { }))",
                        ".thenAccept(user -> {",
                        "ValidatorUtils.checkNonNull(user);",
                        "handleCreateUsersWithArrayInput(request, response, user);",
                        ".exceptionally(throwable -> handleError(request, response, throwable));"
                );
    }

    @Test
    public void doGenerateHeaderParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .assertMethod("deletePet", "ServerRequest", "ServerResponse")
                .bodyContainsLines(
                        "String apiKey = request.headers().value(\"api_key\").orElse(null);",
                        "Long headerLong = request.headers().value(\"headerLong\").map(Long::valueOf).orElse(null);",
                        "ValidatorUtils.checkNonNull(headerLong);"
                );
    }

    @Test
    public void doGenerateCookiesParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .assertMethod("deletePet", "ServerRequest", "ServerResponse")
                .bodyContainsLines(
                        "String cookieString = request.headers().cookies().toMap().getOrDefault(\"cookieString\", List.of" +
                                "()).stream().findFirst().orElse(null);",
                        "ValidatorUtils.checkNonNull(cookieString);",
                        "Integer cookieInt = request.headers().cookies().toMap().getOrDefault(\"cookieInt\", List.of())" +
                                ".stream().findFirst().map(Integer::valueOf).orElse(null);",
                        "List<String> cookieIntArray = Optional.ofNullable(request.headers().cookies().toMap().get" +
                                "(\"cookieIntArray\")).orElse(null);",
                        "List<String> cookieStringArray = Optional.ofNullable(request.headers().cookies().toMap().get" +
                                "(\"cookieStringArray\")).orElse(null);"
                );
    }

    @Test
    public void doGenerateFormParams() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .assertMethod("addPets", "ServerRequest", "ServerResponse")
                .bodyContainsLines(
                        "Map<String, List<String>> nonFileFormContent = new HashMap<>();",
                        "Map<String, List<InputStream>> fileFormContent = new HashMap<>();",
                        " Single<Void> formSingle = request.content().asStream(ReadableBodyPart.class)",
                        "if (\"images[]\".equals(name)) {",
                        "processFileFormField(name, fileFormContent, part);",
                        "if (\"image\".equals(name)) {",
                        "if (\"titles[]\".equals(name)) {",
                        "processNonFileFormField(name, nonFileFormContent, part);",
                        "if (\"longArray\".equals(name)) {",
                        "if (\"stringParam\".equals(name)) {",
                        "if (\"intParam\".equals(name)) {",
                        "List<InputStream> images = Optional.ofNullable(fileFormContent.get(\"images[]\")).orElse(null);",
                        "InputStream image = Optional.ofNullable(fileFormContent.get(\"image\")).flatMap(list->list" +
                                ".stream().findFirst()).orElse(null);",
                        "List<String> titles = Optional.ofNullable(nonFileFormContent.get(\"titles[]\")).orElse(null);",
                        "List<String> longArray = Optional.ofNullable(nonFileFormContent.get(\"longArray\")).orElse(null);",
                        "Integer intParam = Optional.ofNullable(nonFileFormContent.get(\"intParam\")).flatMap(list->list" +
                                ".stream().findFirst()).map(Integer::valueOf).orElse(null);"
                );
    }

    @Test
    public void doGenerateParamsValidation() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .assertMethod("findPetsByStatus")
                .bodyContainsLines(
                        "ValidatorUtils.checkNonNull(status);",
                        "List<String> status = Optional.ofNullable(request.queryParams().toMap().get(\"status\")).orElse" +
                                "(null);"
                )
                .toFileAssert()
                .assertMethod("findPetsByTags")
                .bodyContainsLines(
                        "List<String> tags = Optional.ofNullable(request.queryParams().toMap().get(\"tags\")).orElse" +
                                "(null);",
                        "ValidatorUtils.checkNonNull(tags);"
                );

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/UserService.java"))
                .assertMethod("loginUser")
                .bodyContainsLines(
                        "ValidatorUtils.validatePattern(username, \"^[a-zA-Z0-9]+[a-zA-Z0-9\\\\" +
                                ".\\\\-_]*[a-zA-Z0-9]+$\");",
                        ""
                );
    }

}