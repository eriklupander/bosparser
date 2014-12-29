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
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
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

import java.util.Collections;
import java.util.Comparator;
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
//
//    @RequestMapping(method = RequestMethod.GET, value = "/total", produces = "application/json")
//    public ResponseEntity<TotalReport> getTotal() {
//
//        List<Stats> reports = statsDao.getAll();
//
//        TotalReport totalReport = new TotalReport();
//        totalReport.setMissions(reports.size());
//        totalReport.setMissionsSurvived(reports.stream().filter(tr -> tr.getFinalState() == State.ALIVE).count());
//        totalReport.setMissionsDestroyed(reports.stream().filter(tr -> tr.getFinalState() == State.DESTROYED).count());
//
//        int flightTimeSeconds = reports.stream().mapToInt(tr -> TimeUtil.toSeconds(tr.getTotalDuration())).sum();
//        totalReport.setTotalFlightTimeSeconds(flightTimeSeconds);
//        totalReport.setTotalFlightTime(TimeUtil.fromSeconds(flightTimeSeconds));
//
//        TreeMap<Object,Long> kills = reports.stream()
//                .flatMap(tr -> tr.getKills().stream())
//                .sorted((GameObject g1, GameObject g2) -> g1.getType().compareTo(g2.getType()))
//                .filter(k -> k.getParentId() == -1)
//                .collect(Collectors.groupingBy(GameObject::getType, TreeMap::new, Collectors.counting()));
//        totalReport.setKillsByTargetType(kills);
//        Object[] objects = kills.entrySet().stream()
//                .sorted((Map.Entry<Object, Long> e1, Map.Entry<Object, Long> e2) -> e2.getValue().compareTo(e1.getValue()))
//                .toArray();
//        totalReport.setNumberOfKillsByTargetTypeSorted(objects);
//        totalReport.setKillsByTargetType(kills);
//
//
//        TreeMap missionsPerPlane = reports.stream()
//                .collect(Collectors.groupingBy(Stats::getPilotPlane, TreeMap::new, Collectors.counting()));
//        totalReport.setSortiesPerPlaneType(missionsPerPlane);
//
//
//        Map<String, List<Stats>> statsPerPlane = reports.stream()
//                .collect(Collectors.groupingBy((Stats s) -> s.getPilotPlane()));
//        Map<String, Long> map = new HashMap<>();
//        statsPerPlane.forEach((String s, List<Stats> list) -> {
//            map.put(s, list.stream().flatMap(tr -> tr.getKills().stream()).count());
//        });
//        totalReport.setKillsInPlaneType(map);
//
//
//        TreeMap < Object, Long > hits = reports.stream()
//        .flatMap(tr -> tr.getHits().stream())
//        .sorted((Hit g1, Hit g2) -> g1.getAmmo().compareTo(g2.getAmmo()))
//        .collect(Collectors.groupingBy(Hit::getAmmo, TreeMap::new, Collectors.counting()));
//
//        totalReport.setHitsByAmmoType(hits);
//        Object[] sortedHits = hits.entrySet().stream()
//                .sorted((Map.Entry<Object, Long> e1, Map.Entry<Object, Long> e2) -> e2.getValue().compareTo(e1.getValue()))
//                .toArray();
//        totalReport.setNumberOfHitsByAmmoTypeSorted(sortedHits);
//
//
//        return new ResponseEntity(totalReport, HttpStatus.OK);
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/totalrx", produces = "application/json")
    public ResponseEntity<TotalReport> getTotalRx() {
        List<Stats> reports = statsDao.getAll();
        TotalReport totalReport = buildAggregateReportUsingRx(reports);
        return new ResponseEntity(totalReport, HttpStatus.OK);
    }


    TotalReport buildAggregateReportUsingRx(List<Stats> reports) {

        final TotalReport totalReport = new TotalReport();


        totalReport.setMissions(reports.size());

        // Count number of missions ending in state ALIVE
        Observable.from(reports)
                .filter(new Func1<Stats, Boolean>() {
                    @Override
                    public Boolean call(Stats stats) {
                        return stats.getFinalState() == State.ALIVE;
                    }
                }).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                totalReport.setMissionsSurvived(integer.longValue());
            }
        });

        // Count number of missions ending in state DESTROYED
        Observable.from(reports)
                .filter(new Func1<Stats, Boolean>() {
                    @Override
                    public Boolean call(Stats stats) {
                        return stats.getFinalState() == State.DESTROYED;
                    }
                }).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                totalReport.setMissionsDestroyed(integer.longValue());
            }
        });

        // Count and then sum the total mission duration
        Observable<Integer> durationObservable = Observable.from(reports).map(new Func1<Stats, Integer>() {
            @Override
            public Integer call(Stats stats) {
                return TimeUtil.toSeconds(stats.getTotalDuration());
            }
        });
        rx.observables.MathObservable.sumInteger(durationObservable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer flightTimeSeconds) {
                totalReport.setTotalFlightTimeSeconds(flightTimeSeconds);
                totalReport.setTotalFlightTime(TimeUtil.fromSeconds(flightTimeSeconds));
            }
        });

        // Group kills by type, filter out non-root kills and set count
        Observable<GroupedObservable<Object, GameObject>> grouped = Observable.from(reports).flatMap(new Func1<Stats, Observable<GameObject>>() {
            @Override
            public Observable<GameObject> call(Stats stats) {
                return Observable.from(stats.getKills());
            }
        }).filter(new Func1<GameObject, Boolean>() {
            @Override
            public Boolean call(GameObject o) {
                return o.getParentId() == -1;
            }
        }).groupBy(new Func1<Object, Object>() {
            @Override
            public Object call(Object o) {
                return ((GameObject) o).getType();
            }
        });

        grouped.forEach(new Action1<GroupedObservable<Object, GameObject>>() {

            @Override
            public void call(GroupedObservable<Object, GameObject> objectGameObjectGroupedObservable) {
                final Object key = objectGameObjectGroupedObservable.getKey();
                objectGameObjectGroupedObservable.count().subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer integer) {
                        totalReport.getKillsByTargetType().put(key, integer.longValue());
                    }
                });
            }
        });

        // Group sorties by plane flown and count
        Observable.from(reports)
                .groupBy(new Func1<Stats, Object>() {
                    @Override
                    public Object call(Stats o) {
                        return o.getPilotPlane();
                    }
                }).forEach(new Action1<GroupedObservable<Object, Stats>>() {
            @Override
            public void call(GroupedObservable<Object, Stats> objectStatsGroupedObservable) {
                final Object key = objectStatsGroupedObservable.getKey();
                objectStatsGroupedObservable.count().subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer integer) {
                        totalReport.getSortiesPerPlaneType().put(key, integer.longValue());
                    }
                });
            }
        });



        // Group kills by each plane they were won in
        Observable<GroupedObservable<Object, Stats>> group = Observable.from(reports).groupBy(new Func1<Stats, Object>() {
            @Override
            public Object call(Stats o) {
                return o.getPilotPlane();
            }
        });
        group.subscribe(new Action1<GroupedObservable<Object, Stats>>() {
            @Override
            public void call(GroupedObservable<Object, Stats> objectStatsGroupedObservable) {
                final Object key = objectStatsGroupedObservable.getKey();

                // Start group aggregation by flatmapping the kill lists into a stream of GameObjects
                objectStatsGroupedObservable.flatMap(new Func1<Stats, Observable<GameObject>>() {
                    @Override
                    public Observable<GameObject> call(Stats stats) {
                        return Observable.from(stats.getKills());
                    }

                // Filter out all non-root ones (e.g. don't count crew and turrets)
                }).filter(new Func1<GameObject, Boolean>() {
                    @Override
                    public Boolean call(GameObject gameObject) {
                        return gameObject.getParentId() == -1;
                    }
                })

                // Finally, count number of items left in stream and associate to key and write to result.
                .count().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer cnt) {
                        totalReport.getKillsInPlaneType().put((String) key, cnt.longValue());
                    }
                });

            }
        });

        // Group hits by ammo type and count
        Observable<Hit> hitObservable = Observable.from(reports)
                .flatMap(new Func1<Stats, Observable<Hit>>() {
                    @Override
                    public Observable<Hit> call(Stats stats) {
                        return Observable.from(stats.getHits());
                    }
                });

        hitObservable.groupBy(new Func1<Hit, Object>() {
            @Override
            public Object call(Hit hit) {
                return hit.getAmmo();
            }
        }).subscribe(new Action1<GroupedObservable<Object, Hit>>() {
            @Override
            public void call(GroupedObservable<Object, Hit> groupedHits) {
                final Object key = groupedHits.getKey();
                groupedHits.count().subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer cnt) {
                        totalReport.getHitsByAmmoType().put(key, cnt.longValue());
                    }
                });
            }
        });

        // Simply count all kills by flatmapping the kills, filter out non-roots and count.
        Observable.from(reports).flatMap(new Func1<Stats, Observable<GameObject>>() {
            @Override
            public Observable<GameObject> call(Stats stats) {
                return Observable.from(stats.getKills());
            }
        }).filter(new Func1<GameObject, Boolean>() {
            @Override
            public Boolean call(GameObject gameObject) {
                return gameObject.getParentId() == -1;
            }
        }).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                totalReport.setKills(integer);
            }
        });

        // Count all hits. This one includes hits on crew and turrets
        Observable.from(reports).flatMap(new Func1<Stats, Observable<Hit>>() {
            @Override
            public Observable<Hit> call(Stats stats) {
                return Observable.from(stats.getHits());
            }
        }).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                totalReport.setHits(integer);
            }
        });


        return totalReport;
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

    // Spring setter, also for unit testing
    public void setStatsDao(StatsDao statsDao) {
        this.statsDao = statsDao;
    }
}
