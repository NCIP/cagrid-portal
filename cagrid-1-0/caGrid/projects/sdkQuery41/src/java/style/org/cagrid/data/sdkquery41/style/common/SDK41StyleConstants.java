package org.cagrid.data.sdkquery41.style.common;

import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.File;

public interface SDK41StyleConstants {

    public static final String STYLE_NAME = "caCORE SDK v 4.1";
    public static final String STYLE_DIR = ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
        + File.separator + "data" + File.separator + "styles" + File.separator + "cacore41";
    
}
