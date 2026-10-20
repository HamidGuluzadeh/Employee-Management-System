package com.auspify_tech.employee_management_system.exception;

public class ResourceAlreadyExistsException extends ConflictException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
