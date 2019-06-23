package com.z.graphql.tool;

import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.parser.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BackwardCompatibilityValidator {
    private final ValidationResult validationResult = new ValidationResult();

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void validate(final String oldSchemaFilePath, final String newSchemaFilePath) throws IOException {
        final Parser parser = new Parser();

        final Document oldDocument = parser.parseDocument(FileUtils.readFileToString(oldSchemaFilePath));
        final Document newDocument = parser.parseDocument(FileUtils.readFileToString(newSchemaFilePath));


        oldDocument.getDefinitions().forEach(x -> this.isBackwardCompatible(x, newDocument.getDefinitions()));
    }


    private void isBackwardCompatible(final Definition oldDef, final List<Definition> newDefs) {
        // first find the corresponding definition in new schema.
        Optional<Definition> newDef = newDefs.stream()
                .filter(x -> oldDef.getClass().equals(x.getClass()) &&
                        ((ObjectTypeDefinition) oldDef).getName().equals((((ObjectTypeDefinition) x).getName())))
                .findFirst();

        if (!newDef.isPresent()) {
            updateResult(true, (FieldDefinition) oldDef);
        };

        final List<FieldDefinition> oldChildren = ((ObjectTypeDefinition) oldDef).getFieldDefinitions();
        final List<FieldDefinition> newChildren = ((ObjectTypeDefinition) newDef.get()).getFieldDefinitions();

        oldChildren.forEach(oldType -> {
            Optional<FieldDefinition> child = newChildren.stream()
                    .filter(y -> y.getName().equals(oldType.getName()))
                    .findFirst();

            if (!child.isPresent()) {
                updateResult(true, oldType);
            }

            final Type childType = child.get().getType();
            final Type oldTypeType = oldType.getType();
            boolean isCorrectType = childType.getClass().equals(oldTypeType.getClass());
            if (!isCorrectType) {
                updateResult(false, oldType);
            } else {
                final String childTypeName = getNameOfAType(childType);
                final String oldTypeTypeName = getNameOfAType(oldTypeType);
                final boolean typeMatch = childTypeName != null && childTypeName.equals(oldTypeTypeName);
                if (!typeMatch) {
                    updateResult(false, oldType);
                }
            }
        });
    }

    private void updateResult(boolean isFieldMissing, FieldDefinition fieldDefinition) {
        final String message;
        if (isFieldMissing) {
            message = String.format("Field %s from previous schema not found at line number: %d:%d ",
                    fieldDefinition.getName(), fieldDefinition.getSourceLocation().getLine(), fieldDefinition.getSourceLocation().getColumn());
        } else {
            message = String.format("Type mismatch found for field: %s at %d:%d",
                    fieldDefinition.getName(), fieldDefinition.getSourceLocation().getLine(), fieldDefinition.getSourceLocation().getColumn());
        }
        this.validationResult.getErrors().add(message);
        this.validationResult.setBackwardCompatible(false);
    }

    private String getNameOfAType(final Type type) {
        if (type.getClass().equals(TypeName.class)) {
            return ((TypeName) type).getName();
        } else if (type.getClass().equals(ListType.class)) {
            return getNameOfAType(((ListType) type).getType());
        }
        return null;
    }
}
