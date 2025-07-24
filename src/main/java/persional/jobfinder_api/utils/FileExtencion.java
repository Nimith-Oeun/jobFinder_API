/*
    * this file used for getting file extension from file name.
 */

package persional.jobfinder_api.utils;

public class FileExtencion {

    public static String getExtension(String file) {
        int index = file.lastIndexOf('.');
        if (index < 0 ){
            return null;
        }
        return file.substring(index + 1);
    }
}