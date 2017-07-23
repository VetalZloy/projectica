package com.vetalzloy.projectica.service.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.BigIntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.User;

@Repository
public class UserDAOImpl implements UserDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getSimilars(String usernamePattern, List<String> tags) {
		String baseHQL = "FROM User u WHERE u.username LIKE :usernamePattern ";
		List<User> list;
		if(tags.size() == 0) {
			//retrieves users only by namePattern
			list = sessionFactory.getCurrentSession()
								 .createQuery(baseHQL)
								 .setParameter("namePattern", "%"+usernamePattern+"%")
								 .list();
		} else {
			
			//This query retrieves ids of users which have ALL of necessary tags
			List<BigInteger> ids = sessionFactory
								   .getCurrentSession()
								   .createSQLQuery("SELECT ut.user_id "
								   				 + "FROM tag t "
								   				 + "INNER JOIN users_tags ut ON t.tag_id = ut.tag_id "
								   				 + "WHERE t.tag IN (:tags) "
								   				 + "GROUP BY ut.user_id "
								   				 + "HAVING count(*) = :tagsSize")
								   .setParameterList("tags", tags)
								   .setParameter("tagsSize", tags.size())
								   .list();
			
			logger.debug("Ids of users with such tags extracted. Size = {}", ids.size());
			
			if(ids.size() == 0){
				list = new ArrayList<>();
			} else {			
				
				//Retrieve positions with retrieved before ids
				list = sessionFactory.getCurrentSession()
								 	 .createQuery(baseHQL + "AND u.id IN (:ids)")
								 	 .setParameter("usernamePattern", "%"+usernamePattern+"%")
								 	 .setParameterList("ids", ids, BigIntegerType.INSTANCE)
								 	 .list();
			}
		}

		logger.info("Users with username like '{}' and definite tags were extracted; list size = {}",
						usernamePattern, list.size());
		return list;
	}
	
	@Override
	public User getByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.createQuery("FROM User WHERE username = :username")
								  .setParameter("username", username)
								  .uniqueResult();
		
		if(user == null)
			logger.info("User with username {} not exist", username);
		else		
			logger.info("User with username {} was extracted successfully. {}",
						username, user);
		return user;
	}

	@Override
	public void saveOrUpdate(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);		
		logger.info("User {} was saved or updated succesfully", user);
	}

	@Override
	public User getByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.createQuery("FROM User WHERE email = :email")
								  .setParameter("email", email)
								  .uniqueResult();
		
		if(user == null)
			logger.info("User with email {} not exist", email);
		else		
			logger.info("User with email {} was extracted successfully. {}",
							email, user);
		return user;
	}	

	@Override
	public User getById(long userId) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.createQuery("FROM User WHERE userId = :userId")
								  .setParameter("userId", userId)
								  .uniqueResult();
		
		if(user == null)
			logger.info("User with id = '{}' not exist", userId);
		else		
			logger.info("User with id = '{}' was extracted successfully. {}",
						userId, user);
		return user;
	}

	@Override
	public User getByVerificationToken(String token) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.createQuery("FROM User WHERE userId = "
												+ "(SELECT user.userId "
												 + "FROM VerificationToken "
												 + "WHERE token = :token)")
								  .setParameter("token", token)
								  .uniqueResult();

		if(user == null)
			logger.info("User with verificationToken {} not exist", token);
		else		
			logger.info("User with verificationToken {} was extracted successfully. {}",
						token, user);
		return user;
	}

	@Override
	public User getByPasswordToken(String token) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.createQuery("FROM User WHERE userId = "
											 + "(SELECT user.userId "
											  + "FROM PasswordToken "
											  + "WHERE passwordToken = :token)")
								  .setParameter("token", token)
								  .uniqueResult();

		if(user == null)
			logger.info("User with passwordToken {} not exist", token);	
		else
			logger.info("User with passwordToken {} was extracted successfully. {}",
						token, user);
		return user;
	}

	@Override
	public void deleteUsersWithExpiredVerificationToken() {
		Session session = sessionFactory.getCurrentSession();
		int amount = session.createQuery("DELETE FROM User "
									   + "WHERE enabled = FALSE "
									   + "AND userId IN (SELECT user.userId "
										 			  + "FROM VerificationToken "
										 			  + "WHERE expireDate < :now)")
							.setParameter("now", LocalDateTime.now())
							.executeUpdate();
		
		logger.info("Users with expiredVerificationToken were deleted. Amount = {}", amount);
	}
	
	/*
	 * Here we cannot just invoke session.delete(...), because we have some problem with exactly this property.
	 * @see com.vetalzloy.projectica.model.User
	 */
	@Override
	public void deletePasswordToken(User u) {
		sessionFactory.getCurrentSession()
					  .createQuery("DELETE FROM PasswordToken WHERE user.userId = :id")
					  .setParameter("id", u.getUserId())
					  .executeUpdate();
		logger.info("Password token for user {} was deleted", u);
	}

	@Override
	public void deleteExpiredPasswordTokens() {
		Session session = sessionFactory.getCurrentSession();
		int amount = session.createQuery("DELETE FROM PasswordToken "
									   + "WHERE expireDate < :now")
							.setParameter("now", LocalDateTime.now())
							.executeUpdate();
		
		logger.info("Expired password tokens were deleted. Amount = {}", amount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersPage(int start, int amount) {
		Session session = sessionFactory.getCurrentSession();
		List<User> list = session.createQuery("FROM User WHERE enabled = true")
								 .setFirstResult(start)
								 .setMaxResults(amount)
								 .list();
		
		logger.info("Users from {} to {} were extracted, result list size = {}", 
				start, start+amount, list.size());
		return list;
	}

	@Override
	public User getFullByUsername(String username) {
		User user = getByUsername(username);
		if(user == null) return null;
		
		logger.info("Reloading corresponding created projects for user with username {}", username);
		Hibernate.initialize(user.getCreatedProjects());
		
		logger.info("Reloading corresponding tags for user with username {}", username);
		Hibernate.initialize(user.getTags());
		
		logger.info("Reloading corresponding positions for user with username {}", username);
		Hibernate.initialize(user.getPositions());
		
		return user;
	}

}
