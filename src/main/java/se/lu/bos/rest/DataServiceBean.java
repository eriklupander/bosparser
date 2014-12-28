package se.lu.bos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.lu.bos.dao.StatsDao;
import se.lu.bos.model.GameObject;
import se.lu.bos.model.Hit;
import se.lu.bos.model.State;
import se.lu.bos.model.Stats;
import se.lu.bos.rest.dto.TinyReport;
import se.lu.bos.rest.dto.TotalReport;
import se.lu.bos.scanner.ReportFileScanner;
import se.lu.bos.scanner.ReportFileScannerBean;
import se.lu.bos.util.TimeUtil;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    Environment env;

    private static Comparator<? super TinyReport> tinyReportComparator = new Comparator<TinyReport>() {
        @Override
        public int compare(TinyReport o1, TinyReport o2) {
            return o2.getCreated().compareTo(o1.getCreated());
        }
    };

    @RequestMapping(method = RequestMethod.GET, value = "/reports", produces = "application/json")
    public ResponseEntity<List<Stats>> getAll() {
        return new ResponseEntity(statsDao.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/total", produces = "application/json")
    public ResponseEntity<TotalReport> getTotal() {

        List<Stats> reports = statsDao.getAll();

        TotalReport totalReport = new TotalReport();
        totalReport.setNumberOfMissions(reports.size());
        totalReport.setNumberOfMissionsSurvived(reports.stream().filter(tr -> tr.getFinalState() == State.ALIVE).count());
        totalReport.setNumberOfMissionsDestroyed(reports.stream().filter(tr -> tr.getFinalState() == State.DESTROYED).count());

        int flightTimeSeconds = reports.stream().mapToInt(tr -> TimeUtil.toSeconds(tr.getTotalDuration())).sum();
        totalReport.setTotalFlightTimeSeconds(flightTimeSeconds);
        totalReport.setTotalFlightTime(TimeUtil.fromSeconds(flightTimeSeconds));

        TreeMap<Object,Long> kills = reports.stream()
                .flatMap(tr -> tr.getKills().stream())
                .sorted((GameObject g1, GameObject g2) -> g1.getType().compareTo(g2.getType()))
                .collect(Collectors.groupingBy(GameObject::getType, TreeMap::new, Collectors.counting()));
        totalReport.setNumberOfKillsByTargetType(kills);
        Object[] objects = kills.entrySet().stream()
                .sorted((Map.Entry<Object, Long> e1, Map.Entry<Object, Long> e2) -> e2.getValue().compareTo(e1.getValue()))
                .toArray();
        totalReport.setNumberOfKillsByTargetTypeSorted(objects);
        totalReport.setNumberOfKillsByTargetType(kills);


        TreeMap missionsPerPlane = reports.stream()
                .collect(Collectors.groupingBy(Stats::getPilotPlane, TreeMap::new, Collectors.counting()));
        totalReport.setNumberOfSortiesPerPlaneType(missionsPerPlane);


        Map<String, List<Stats>> statsPerPlane = reports.stream()
                .collect(Collectors.groupingBy((Stats s) -> s.getPilotPlane()));
        Map<String, Long> map = new HashMap<>();
        statsPerPlane.forEach((String s, List<Stats> list) -> {
            map.put(s, list.stream().flatMap(tr -> tr.getKills().stream()).count());
        });
        totalReport.setNumberOfKillsInPlaneType(map);


        TreeMap < Object, Long > hits = reports.stream()
        .flatMap(tr -> tr.getHits().stream())
        .sorted((Hit g1, Hit g2) -> g1.getAmmo().compareTo(g2.getAmmo()))
        .collect(Collectors.groupingBy(Hit::getAmmo, TreeMap::new, Collectors.counting()));
        totalReport.setNumberOfHitsByAmmoType(hits);
        Object[] sortedHits = hits.entrySet().stream()
                .sorted((Map.Entry<Object, Long> e1, Map.Entry<Object, Long> e2) -> e2.getValue().compareTo(e1.getValue()))
                .toArray();
        totalReport.setNumberOfHitsByAmmoTypeSorted(sortedHits);


        return new ResponseEntity(totalReport, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/tinyreports", produces = "application/json")
    public ResponseEntity<List<TinyReport>> getAllTiny() {

        List<TinyReport> reports = statsDao.getTinyReports();
        Collections.sort(reports, tinyReportComparator);
        return new ResponseEntity(reports, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reports/{id}", produces = "application/json")
    public ResponseEntity<Stats> findById(@PathVariable Long id) {
        return new ResponseEntity(statsDao.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reports", produces = "application/json")
    public ResponseEntity<Stats> scanForReports() {
        int scannedReports = reportFileScanner.scan();
        return new ResponseEntity("Scanned " + scannedReports + " mission reports from " + env.getProperty("reports.directory", ReportFileScannerBean.DEFAULT_SCAN_FOLDER), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/reports", produces = "application/json")
    public ResponseEntity<Stats> deleteReports() {
        int deletedReports = statsDao.deleteAll();
        return new ResponseEntity("Deleted " + deletedReports + " mission reports", HttpStatus.OK);
    }

}
