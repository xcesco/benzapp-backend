package it.insiel.innovazione.poc.benzapp;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("it.insiel.innovazione.poc.benzapp");

        noClasses()
            .that()
            .resideInAnyPackage("it.insiel.innovazione.poc.benzapp.service..")
            .or()
            .resideInAnyPackage("it.insiel.innovazione.poc.benzapp.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..it.insiel.innovazione.poc.benzapp.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
