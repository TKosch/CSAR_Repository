package org.opentosca.csarrepo.model.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.opentosca.csarrepo.exception.PersistenceException;
import org.opentosca.csarrepo.model.CsarFile;
import org.opentosca.csarrepo.model.Plan;
import org.opentosca.csarrepo.model.Plan.PlanId;

/**
 * Class to avoid direct access of the hibernate active records for CSAR plan.
 * 
 * @author Dennis Przytarski
 *
 */
public class CsarPlanRepository {

	/**
	 * Returns a csar plan for the given id.
	 * 
	 * @param id
	 * @return csarPlan
	 * @throws PersistenceException
	 *             upon problems committing the underlying transaction
	 */
	public Plan getById(long id) throws PersistenceException {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Plan csarPlan = null;
		try {
			tx = session.beginTransaction();
			csarPlan = (Plan) session.get(Plan.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
		return csarPlan;
	}

	/**
	 * Gets all CSAR plans.
	 * 
	 * @return List of CSAR plans.
	 * @throws PersistenceException
	 *             upon problems committing the underlying transaction
	 */
	public List<Plan> getAll() throws PersistenceException {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Plan> csarPlanList = null;
		try {
			tx = session.beginTransaction();
			csarPlanList = session.createQuery("from CsarPlan").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
		return csarPlanList;
	}

	/**
	 * @param csarPlan
	 *            to be stored
	 * @return id of the saved csar plan
	 * @throws PersistenceException
	 *             upon problems committing the underlying transaction
	 */
	public PlanId save(Plan csarPlan) throws PersistenceException {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(csarPlan);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
		return csarPlan.getId();
	}

	/**
	 * @param csarPlan
	 * @throws Exception
	 *             upon problems committing the underlying transaction
	 */
	public void delete(Plan csarPlan) throws PersistenceException {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(csarPlan);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}
	
	/**
	 * counts the number of available instances
	 * 
	 * @return instance count
	 * @throws PersistenceException
	 * 						upon problems committing the underlying transaction
	 */
	public long count() throws PersistenceException {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		long count = 0;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Plan.class);
			criteria.setProjection(Projections.rowCount());
			count = (Long) criteria.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			if(tx != null) {
				tx.rollback();
			}
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
		
		return count;
	}

}
