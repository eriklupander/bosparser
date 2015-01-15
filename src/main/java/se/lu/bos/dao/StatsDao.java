package se.lu.bos.dao;

import se.lu.bos.model.Stats;
import se.lu.bos.rest.dto.TinyReport;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-01
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public interface StatsDao {

    boolean exists(String rootFileName);
    Stats save(Stats stats);
    Stats findById(Long id);
    Stats findByRootFileName(String rootFileName);
    List<Stats> getAll();

    int deleteAll();

    List<TinyReport> getTinyReports();

    Stats update(Stats stats);
}
