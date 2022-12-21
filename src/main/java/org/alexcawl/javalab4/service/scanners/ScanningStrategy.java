package org.alexcawl.javalab4.service.scanners;

import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.WebResource;

import java.util.List;

public interface ScanningStrategy {
    SearchArea getArea();
    List<WebResource> analyze(String key);
}
