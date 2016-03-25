package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.File;
import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdateKey;

/**
 * DAO for File
 */
@Stateless
public class FileDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(File entity) {
		em.persist(entity);
	}

	public boolean deleteById(FileUpdateKey id) {
		File entity = em.find(File.class, id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public boolean deleteById(Long sourceId, Long stationId) {
		File entity = findById(sourceId, stationId);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public File findByFileName(String fileName) {
		return em.find(File.class, fileName);
	}

	public File findById(Long sourceId, Long stationId) {
		try {
			TypedQuery<File> q = em.createNamedQuery("File.getByIdClass", File.class);
			q.setParameter("sourceId", sourceId);
			q.setParameter("stationId", stationId);
			return q.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public File update(File entity) {
		return em.merge(entity);
	}

	public List<File> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<File> findAllQuery = em.createQuery("SELECT DISTINCT f FROM File f ORDER BY f.sourceId, f.stationId",
				File.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public void deleteAll() {
		Query q = em.createNamedQuery("File.deleteAll");
		q.executeUpdate();
	}
}
