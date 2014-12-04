package se.lu.bos.scanner;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-01
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class BasicReportFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File directory, String fileName) {
        if (fileName.endsWith("[0].txt")) {
            return true;
        }
        return false;
    }
}

