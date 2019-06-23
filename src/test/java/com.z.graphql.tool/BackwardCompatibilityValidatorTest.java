package com.z.graphql.tool;


import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BackwardCompatibilityValidatorTest {
    @Test
    public void testSchemaValidity() throws IOException {
        // GIVEN
        final BackwardCompatibilityValidator backwardCompatibilityValidator = new BackwardCompatibilityValidator();

        // WHEN
        String oldSchemaFilePath = getClass().getClassLoader().getResource("newSchema.graphql").getFile();
        String newSchemaFilePath = getClass().getClassLoader().getResource("oldSchema.graphql").getFile();
        backwardCompatibilityValidator.validate(oldSchemaFilePath, newSchemaFilePath);


        // THEN
        assertFalse(backwardCompatibilityValidator.getValidationResult().isBackwardCompatible());
        backwardCompatibilityValidator.getValidationResult().getErrors().forEach(System.out::println);

    }
}
