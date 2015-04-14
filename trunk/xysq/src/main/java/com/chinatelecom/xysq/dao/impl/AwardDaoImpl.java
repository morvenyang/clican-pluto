package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardHistory;

public class AwardDaoImpl extends BaseDao implements AwardDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Award> findAllAwards() {
		return (List<Award>)this.getHibernateTemplate().find("from Award");
	}

	@Override
	public void deleteAward(Award award) {
		this.getHibernateTemplate().delete(award);
	}

	@Override
	public void saveAward(Award award) {
		this.getHibernateTemplate().saveOrUpdate(award);
	}

	@Override
	public void saveAwardHistory(AwardHistory awardHistory) {
		this.getHibernateTemplate().saveOrUpdate(awardHistory);
	}

	@Override
	public void resetLottery() {
		this.getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery("update User set lottery = 3");
				return query.executeUpdate();
			}
		});
	}

}
