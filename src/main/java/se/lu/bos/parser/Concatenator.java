package se.lu.bos.parser;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-11-30
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Concatenator {

    public Date getFirstFileDate(String baseFolder, String baseFileName) {
        File dir = new File(baseFolder);
        for(File f : dir.listFiles()) {
            if(f.getName().startsWith(baseFileName)) {
                return new Date(f.lastModified());
            }
        }
        return new Date();
    }

    // missionReport(2014-11-30_17-14-34)
    public String buildFromBaseFileName(String baseFolder, String baseFileName) throws IOException {
        File dir = new File(baseFolder);
        StringBuilder buf = new StringBuilder();
        BufferedReader br = null;
        // TODO sort log files first.
        List<File> fileList = Arrays.asList(dir.listFiles());
        Collections.sort(fileList, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return new Long(o1.lastModified()).compareTo(new Long(o2.lastModified()));
            }
        });
        for(File f : fileList) {
            if(f.getName().startsWith(baseFileName)) {
                br = new BufferedReader(new FileReader(f));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    buf.append(sb.toString());
                } finally {
                    br.close();
                }
            }

        }
        return buf.toString();
    }

}
