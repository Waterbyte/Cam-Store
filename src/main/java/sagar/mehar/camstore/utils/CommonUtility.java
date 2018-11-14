package sagar.mehar.camstore.utils;

import java.io.File;

/**
 * Created by Mountain on 08-11-18.
 */

public class CommonUtility {


    public static boolean createDir(String path, String name) {
        try {
            File folder = new File(path, name);
            return !folder.exists() && folder.mkdir();
        } catch (Exception e) {
            return false;
        }
    }


}
