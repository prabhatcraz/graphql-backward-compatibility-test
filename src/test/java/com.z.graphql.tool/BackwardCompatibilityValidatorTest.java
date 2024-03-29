package com.z.graphql.tool;


import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BackwardCompatibilityValidatorTest {
    @Test
    public void testSchemaValidity() throws IOException {
        // GIVEN
        final BackwardCompatibilityValidator backwardCompatibilityValidator = new BackwardCompatibilityValidator();

        // WHEN
        String oldSchemaFilePath = getClass().getClassLoader().getResource("oldSchema-1.graphql").getFile();
        String newSchemaFilePath = getClass().getClassLoader().getResource("newSchema-1.graphql").getFile();
        final ValidationResult result = backwardCompatibilityValidator.validate(oldSchemaFilePath, newSchemaFilePath);

        // THEN
        assertFalse(result.isBackwardCompatible());
        result.getErrors().forEach(System.out::println);
    }
}
