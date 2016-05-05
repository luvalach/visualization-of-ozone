package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.BaseEntity;

public class GenericDao<E extends BaseEntity, PK extends Serializable> {

	protected Class<E> entityClass;
	protected Class<PK> primaryKeyClass;

	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	protected EntityManager em;

	public GenericDao(Class<E> entityClass, Class primaryKeyClass) {
		this.entityClass = entityClass;
		this.primaryKeyClass = primaryKeyClass;
	}

	public E create(E t) {
		em.persist(t);
		return t;
	}

	public E findById(final PK id) {
		return em.find(entityClass, id);
	}

	public E update(E t) {
		return em.merge(t);
	}

	public void delete(E t) {
		t = em.merge(t);
		em.remove(t);
	}

	public boolean deleteById(PK id) {
		E entity = findById(id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public void deleteAll() {
		StringBuilder queryString = new StringBuilder("DELETE FROM ");
		queryString.append(entityClass.getSimpleName()).append(" e");
		Query query = em.createQuery(queryString.toString());
		query.executeUpdate();
	}

	public List<E> listAll() {
		return listAll(null, null);
	}

	public List<E> listAll(Integer startPosition, Integer maxResult) {
		StringBuilder queryString = new StringBuilder("SELECT e FROM ");
		queryString.append(entityClass.getSimpleName()).append(" e");
		TypedQuery<E> query = em.createQuery(queryString.toString(), entityClass);

		if (startPosition != null) {
			query.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			query.setMaxResults(maxResult);
		}
		return query.getResultList();
	}

	public List<PK> getAllIds() {
		StringBuilder queryString = new StringBuilder("SELECT e.id FROM ");
		queryString.append(entityClass.getSimpleName()).append(" e");
		TypedQuery<PK> query = em.createQuery(queryString.toString(), primaryKeyClass);
		return query.getResultList();
	}
}
