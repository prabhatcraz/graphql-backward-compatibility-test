package com.z.graphql.tool;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    /**
     * An overall status of validation of the schema.
     */
    private boolean isBackwardCompatible;
    private List<String> errors;
    private List<String> warning;

    public ValidationResult () {
        isBackwardCompatible = true;
        errors = new ArrayList<>();
        warning = new ArrayList<>();
    }

    public void setBackwardCompatible(boolean backwardCompatible) {
        isBackwardCompatible = backwardCompatible;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setWarning(List<String> warning) {
        this.warning = warning;
    }

    public boolean isBackwardCompatible() {
        return isBackwardCompatible;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarning() {
        return warning;
    }
}
