package sagar.mehar.camstore.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Mountain on 09-11-18.
 */

public class SupportedFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        if ((name.endsWith(".jpg") || name.endsWith(".png")) || dir.isDirectory())
            return true;
        else
            return false;
    }


}
