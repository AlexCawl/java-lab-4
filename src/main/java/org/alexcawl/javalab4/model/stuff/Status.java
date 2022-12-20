package org.alexcawl.javalab4.model.stuff;

public enum Status {
    OK,
    NOT_FOUND,      // HttpStatusException
    EMPTY_CONTENT,  // <body></body>
    UNABLE_TO_LOAD,        // MalformedURLException, DataIntegrityViolationException, UnsupportedMimeTypeException, IllegalArgumentException
    ERROR
}
