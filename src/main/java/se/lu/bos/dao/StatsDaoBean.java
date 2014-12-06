package se.lu.bos.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.lu.bos.model.Stats;
import se.lu.bos.rest.dto.TinyReport;
import se.lu.bos.util.TimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-01
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StatsDaoBean implements StatsDao {

    private static final Logger log = LoggerFactory.getLogger(StatsDaoBean.class);

    @PersistenceContext
    EntityManager em;


    @Override
    public boolean exists(String rootFileName) {
        Integer n = em.createQuery("SELECT s FROM Stats s WHERE s.rootFileName = :rootFileName")
                .setParameter("rootFileName", rootFileName)
                .getResultList()
                .size();

        return n > 0;
    }

    @Override
    @Transactional
    public Stats save(Stats stats) {
        return em.merge(stats);
    }

    @Override
    public Stats findById(Long id) {
        Stats s = em.find(Stats.class, id);
        if(s.getKills() != null) s.getKills().size();
        return s;
    }

    @Override
    public Stats findByRootFileName(String rootFileName) {
        try {
            Stats stats = em.createQuery("SELECT s FROM Stats s WHERE s.rootFileName = :rootFileName", Stats.class)
                    .setParameter("rootFileName", rootFileName).getSingleResult();
            return stats;
        } catch (NoResultException e) {
            return null;
        } catch(NonUniqueResultException e) {
            log.error("More than one report found for root file: " + rootFileName);
            return null;
        }
    }

    @Override
    public List<Stats> getAll() {
        return em.createQuery("SELECT s FROM Stats s ORDER BY s.created DESC", Stats.class).getResultList();
    }

    @Override
    @Transactional
    public int deleteAll() {
        List<Stats> resultList = em.createQuery("SELECT s FROM Stats s", Stats.class).getResultList();
        for(Stats s : resultList) {
            em.remove(s);
            em.flush();
        }
        return resultList.size();
    }

    @Override
    public List<TinyReport> getTinyReports() {
        List<TinyReport> list = new ArrayList<TinyReport>();
        List<Object[]> resultList = em.createQuery("SELECT s.id, s.missionName, s.reportFileDate FROM Stats s").getResultList();
        if(resultList != null) {
            for(Object[] row : resultList) {
                TinyReport tr = new TinyReport();
                tr.setId((Long) row[0]);
                tr.setTitle((String) row[1]);
                tr.setCreated(TimeUtil.parseDate((Date) row[2]));
                list.add(tr);
            }
        }
        return list;
    }
}
