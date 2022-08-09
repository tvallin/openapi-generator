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
    public void validatePetApi() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/PetService.java"))
                .fileContains("org.openapitools.server.model.Pet")
                .assertMethod("addPet", "Pet")
                .toFileAssert()
                .assertMethod("addPets", "String", "InputStream", "InputStream", "List<String>", "List<Long>", "Integer")
                .toFileAssert()
                .assertMethod("deletePet", "Long", "String", "Long", "String", "Integer", "List<Integer>", "List<String>")
                .toFileAssert()
                .assertMethod("findPetsByStatus", "List<String>")
                .toFileAssert()
                .assertMethod("findPetsByTags", "List<Integer>")
                .toFileAssert()
                .assertMethod("getPetById", "Long")
                .toFileAssert()
                .assertMethod("updatePet", "Pet")
                .toFileAssert()
                .assertMethod("updatePetWithForm", "Long", "String", "String")
                .toFileAssert()
                .assertMethod("uploadFile", "Long", "Long", "String", "InputStream");
    }

    @Test
    public void validateStoreApi() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/StoreService.java"))
                .fileContains("org.openapitools.server.model.Order")
                .assertMethod("deleteOrder", "String")
                .toFileAssert()
                .assertMethod("getInventory")
                .toFileAssert()
                .assertMethod("getOrderById", "BigDecimal")
                .toFileAssert()
                .assertMethod("placeOrder", "Order");
    }

    @Test
    public void validateUserApi() {
        generate();

        JavaFileAssert.assertThat(Paths.get(apiPackage + "/UserService.java"))
                .fileContains("org.openapitools.server.model.User")
                .assertMethod("createUser", "User")
                .toFileAssert()
                .assertMethod("createUsersWithArrayInput", "List<User>")
                .toFileAssert()
                .assertMethod("createUsersWithListInput", "List<User>")
                .toFileAssert()
                .assertMethod("deleteUser", "String")
                .toFileAssert()
                .assertMethod("getUserByName", "String")
                .toFileAssert()
                .assertMethod("loginUser", "String", "String", "String", "Long", "BigDecimal")
                .toFileAssert()
                .assertMethod("logoutUser")
                .toFileAssert()
                .assertMethod("updateUser", "String", "User");
    }

}