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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BackwardCompatibilityValidator {

  private final TypeComparator typeComparator;
  private final QueryComparator queryComparator;

  public BackwardCompatibilityValidator() {
    typeComparator = new TypeComparator();
    queryComparator = new QueryComparator();
  }

  public ValidationResult validate(final String oldSchemaFilePath, final String newSchemaFilePath) throws IOException {

    final Parser parser = new Parser();

    final Document oldDocument = parser.parseDocument(FileUtils.readFileToString(oldSchemaFilePath));
    final Document newDocument = parser.parseDocument(FileUtils.readFileToString(newSchemaFilePath));

    final List<String> messges = oldDocument.getDefinitions().stream()
        .map(x -> this.checkBakwardCompatibility(x, newDocument.getDefinitions().stream()
            .filter(y -> this.isSameType(x, y))
            .findFirst()
            .orElse(null)))
        .flatMap(List::stream)
        .collect(Collectors.toList());

    final ValidationResult validationResult = new ValidationResult();
    if(messges.size() > 0) {
      validationResult.setBackwardCompatible(false);
      validationResult.setErrors(messges);
    }
    return validationResult;
  }

  private boolean isSameType(Definition a, Definition b) {
    return (((ObjectTypeDefinition) a).getName()).equals(((ObjectTypeDefinition) b).getName());
  }

  /**
   * Its guaranteed that this method gets the types with same name.
   */
  private List<String> checkBakwardCompatibility(final Definition oldDef, final Definition newDef) {
    final List<String> errorMessage = new ArrayList<>();
    if (newDef == null) {
      errorMessage.add(String.format("Type '%s' has been removed", ((ObjectTypeDefinition)oldDef).getName()));
    }

    final String typeName = ((ObjectTypeDefinition)oldDef).getName();

    if(typeName.equalsIgnoreCase("Query") ) {

    } else if(typeName.equalsIgnoreCase("Mutation") ) {

    } else if(typeName.equalsIgnoreCase("Subscription") ) {

    } else {
      errorMessage.addAll(typeComparator.compare((ObjectTypeDefinition)oldDef, (ObjectTypeDefinition)newDef));
    }
    return errorMessage;
  }
}
