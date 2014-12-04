package se.lu.bos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.lu.bos.dao.StatsDao;
import se.lu.bos.model.Stats;
import se.lu.bos.rest.dto.TinyReport;
import se.lu.bos.scanner.ReportFileScanner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-01
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/rest/view")
public class DataServiceBean {

    private static final Logger log = LoggerFactory.getLogger(DataServiceBean.class);

    @Autowired
    StatsDao statsDao;

    @Autowired
    ReportFileScanner reportFileScanner;

    @RequestMapping(method = RequestMethod.GET, value = "/reports", produces = "application/json")
    public ResponseEntity<List<Stats>> getAll() {
        return new ResponseEntity(statsDao.getAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/tinyreports", produces = "application/json")
    public ResponseEntity<List<TinyReport>> getAllTiny() {

        return new ResponseEntity(statsDao.getTinyReports(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reports/{id}", produces = "application/json")
    public ResponseEntity<Stats> findById(@PathVariable Long id) {
        return new ResponseEntity(statsDao.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reports", produces = "application/json")
    public ResponseEntity<Stats> scanForReports() {
        int scannedReports = reportFileScanner.scan();
        return new ResponseEntity("Scanned " + scannedReports + " mission reports", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/reports", produces = "application/json")
    public ResponseEntity<Stats> deleteReports() {
        int deletedReports = statsDao.deleteAll();
        return new ResponseEntity("Deleted " + deletedReports + " mission reports", HttpStatus.OK);
    }

}
