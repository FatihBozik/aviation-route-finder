package com.fatihbozik.aviationroutefinder.rest.advice;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class BindingErrorsResponse {
    private final List<BindingError> bindingErrors = new ArrayList<>();

    public BindingErrorsResponse() {
        this(null);
    }

    public BindingErrorsResponse(Integer id) {
        this(null, id);
    }

    public BindingErrorsResponse(Integer pathId, Integer bodyId) {
        boolean onlyBodyIdSpecified = pathId == null && bodyId != null;
        if (onlyBodyIdSpecified) {
            addBodyIdError(bodyId, "must not be specified");
        }
        boolean bothIdsSpecified = pathId != null && bodyId != null;
        if (bothIdsSpecified && !pathId.equals(bodyId)) {
            addBodyIdError(bodyId, String.format("does not match pathId: %d", pathId));
        }
    }

    private void addBodyIdError(Integer bodyId, String message) {
        BindingError error = new BindingError();
        error.setObjectName("body");
        error.setFieldName("id");
        error.setFieldValue(bodyId.toString());
        error.setErrorMessage(message);
        addError(error);
    }

    private void addError(BindingError bindingError) {
        this.bindingErrors.add(bindingError);
    }

    public void addAllErrors(BindingResult bindingResult) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            BindingError error = new BindingError();
            error.setObjectName(fieldError.getObjectName());
            error.setFieldName(fieldError.getField());
            error.setFieldValue(String.valueOf(fieldError.getRejectedValue()));
            error.setErrorMessage(fieldError.getDefaultMessage());
            addError(error);
        }
    }

    @Override
    public String toString() {
        return "BindingErrorsResponse [bindingErrors=" + bindingErrors + "]";
    }

    protected static class BindingError {

        private String objectName;
        private String fieldName;
        private String fieldValue;
        private String errorMessage;

        public BindingError() {
            this.objectName = "";
            this.fieldName = "";
            this.fieldValue = "";
            this.errorMessage = "";
        }

        protected void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        protected void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        protected void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        protected void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return "BindingError [objectName=" + objectName + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue
                   + ", errorMessage=" + errorMessage + "]";
        }

    }
}
