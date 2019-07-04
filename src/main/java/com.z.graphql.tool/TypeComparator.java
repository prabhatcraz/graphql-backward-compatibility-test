package com.z.graphql.tool;

import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Compares the two object types.
 */
public class TypeComparator {

  public static final String FIELD_TYPE_CHANGED_MESSAGE_FORMAT = "Type '%s' has issues, data type of '%s' is change from '%s' to '%s'.";
  public static final String MISSING_FIELD_MESSAGE_FORMAT = "field - '%s' is missing from the type - '%s'.";

  public List<String> compare(final ObjectTypeDefinition oldDef, final ObjectTypeDefinition newDef) {
    final List<String> errorMessages = new ArrayList<>();

    oldDef.getFieldDefinitions().forEach(oldType -> {
      final Optional<FieldDefinition> child = newDef.getFieldDefinitions().stream()
          .filter(y -> y.getName().equals(oldType.getName()))
          .findFirst();

      if (child.isPresent()) {
        final Type childType = child.get().getType();
        final Type oldTypeType = oldType.getType();
        boolean isCorrectType = childType.getClass().equals(oldTypeType.getClass());
        if (!isCorrectType) {
          errorMessages.add(String.format(FIELD_TYPE_CHANGED_MESSAGE_FORMAT,
              oldDef.getName(), child.get().getName(), oldTypeType.getClass(), childType.getClass()));
        }
      } else {
        errorMessages.add(String.format(MISSING_FIELD_MESSAGE_FORMAT, oldType.getName(), oldDef.getName()));
      }
    });

    return errorMessages;
  }
}
