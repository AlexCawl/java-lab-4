package org.alexcawl.javalab4.dao;

public enum NodeStatus {
    OK,
    NOT_FOUND,      // HttpStatusException
    EMPTY_CONTENT,  // <body></body>
    UNABLE_TO_LOAD,        // MalformedURLException, DataIntegrityViolationException, UnsupportedMimeTypeException, IllegalArgumentException
    ERROR
}
