package dao.db;

import entity.Singer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class SingerDaoImpl implements SingerDao {

	private static final Log logger = LogFactory.getLog(SingerDaoImpl.class);
	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<Singer> findAll() {
		return em.createNamedQuery("singer.findAllWithAlbum", Singer.class).getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public Singer findById(Long id) {
		return em.createNamedQuery("singer.findById", Singer.class).setParameter("id", id).getSingleResult();
	}
	@Override
	public Singer save(Singer singer) {
		if (singer.getId() == null) {
			logger.info("Inserting new singer");
			em.persist(singer);
		} else {
			em.merge(singer);
			logger.info("Updating existing singer");
		}
		return singer;
	}
	@Override
	public void delete(Long id) {
		Singer singer=em.find(Singer.class, id);
		em.remove(singer);
		logger.info("Singer deleted with id: " + singer.getId());
	}
	@Override
	public void delete(Singer singer) {
		em.remove(em.merge(singer));
		logger.info("Singer deleted with id: " + singer.getId());
	}
}
