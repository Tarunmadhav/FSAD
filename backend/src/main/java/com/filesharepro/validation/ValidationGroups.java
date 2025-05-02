package com.filesharepro.validation;

public final class ValidationGroups {
    private ValidationGroups() {}

    public interface Create {}
    public interface Update {}
    public interface Delete {}
    public interface Share {}
    
    public interface FileOperations extends Create, Update, Delete {}
    public interface SharingOperations extends Create, Delete {}
}