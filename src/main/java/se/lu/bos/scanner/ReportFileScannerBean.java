package se.lu.bos.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.lu.bos.dao.StatsDao;
import se.lu.bos.model.Stats;
import se.lu.bos.parser.Parser;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-01
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReportFileScannerBean implements ReportFileScanner {

    private static final Logger log = LoggerFactory.getLogger(ReportFileScannerBean.class);

    public static final String DEFAULT_SCAN_FOLDER = "c:\\java\\bos-logs";

    private final BasicReportFileFilter fileFilter = new BasicReportFileFilter();

    @Autowired
    StatsDao statsDao;

    @Autowired
    private Environment env;
    private String reportsFolder;

    @PostConstruct
    public void init() {
        this.reportsFolder = env.getProperty("reports.directory", DEFAULT_SCAN_FOLDER);
        log.info("Set directory '" + this.reportsFolder + "' as reports folder");
    }


    public int scan() {
        log.info("Start scheduled read of reports directory");

        int scanCount = 0;

        File[] files = new File(reportsFolder).listFiles(fileFilter);
        if(files == null ||files.length == 0) {
            log.warn("Specified report directory '" + reportsFolder + "' is empty or does not exist.");
            return 0;
        }
        for(File f : files) {
            String rootFileName = parseRootName(f);
            if(!statsDao.exists(clean(rootFileName))) {
                try {
                    Stats stats = new Parser().buildStatsFromRootFileName(reportsFolder, rootFileName);
                    log.info("Built new stats object: " + stats.toString());
                    stats = statsDao.save(stats);
                    log.info("Saved with ID: " + stats.getId());
                    scanCount++;
                } catch (IOException e) {
                    log.error("Exception occured scanning root file '" + rootFileName + "': " + e.getMessage());
                }
            }
        }
        return scanCount;
    }

    public String parseRootName(File f) {
        return f.getName().substring(0, f.getName().length() - 7);

    }

    private String clean(String rootName) {
        return rootName.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
