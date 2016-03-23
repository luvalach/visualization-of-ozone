package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdate;
import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdateKey;

/**
 * DAO for FileUpdate
 */
@Stateless
public class FileUpdateDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(FileUpdate entity) {
		em.persist(entity);
	}

	public boolean deleteById(FileUpdateKey id) {
		FileUpdate entity = em.find(FileUpdate.class, id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public boolean deleteById(Long sourceId, Long stationId) {
		FileUpdate entity = findById(sourceId, stationId);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public FileUpdate findById(FileUpdateKey id) {
		return em.find(FileUpdate.class, id);
	}

	public FileUpdate findById(Long sourceId, Long stationId) {
		try {
			TypedQuery<FileUpdate> q = em.createNamedQuery("FileUpdate.getByIdClass", FileUpdate.class);
			q.setParameter("sourceId", sourceId);
			q.setParameter("stationId", stationId);
			return q.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public FileUpdate update(FileUpdate entity) {
		return em.merge(entity);
	}

	public List<FileUpdate> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<FileUpdate> findAllQuery = em
				.createQuery("SELECT DISTINCT f FROM FileUpdate f ORDER BY f.sourceId, f.stationId", FileUpdate.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public void deleteAll() {
		Query q = em.createNamedQuery("FileUpdate.deleteAll");
		q.executeUpdate();
	}
}
