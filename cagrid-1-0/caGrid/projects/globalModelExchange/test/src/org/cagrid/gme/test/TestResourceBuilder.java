package org.cagrid.gme.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.domain.XMLSchema;


public class TestResourceBuilder {

    private TestResourceBuilder() {

    }


    public static XMLSchema createSchema(URI namespace, File schemaFile) throws FileNotFoundException, IOException {
        return XSDUtil.createSchema(namespace, schemaFile);
    }


    public static XMLSchema createSchema(URI namespace, List<File> schemaFiles) throws FileNotFoundException,
        IOException {
        return XSDUtil.createSchema(namespace, schemaFiles);
    }
}
