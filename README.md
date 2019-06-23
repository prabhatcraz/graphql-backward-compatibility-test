A simple tool to test if a given graphql schema is backward compatible with another schema.

## Backward Compatibility Rules
This tool determines backward compatibility on following rules. If any of the below is true, the tool will return false
and print out the missing fields. Check usage below to see the output format of the tool.
* There is a type missing.
* There is a field missing.
* Type of a field is changed.
* A query does not exist any more.
* Signature of the query is changed. 

## Usage


## Future work
Create a maven plugin out of it.
