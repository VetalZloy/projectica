package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.UserDAO;


/**
 * This class created for providing normal work of transactions
 * @author Vetal
 *
 */
@Repository
@Transactional
public class UserDAOProxy implements UserDAO {
	
	@Autowired
	@Qualifier("userDAOImpl")
	private UserDAO userDAO;
	
	@Override
	public List<User> getUsersPage(int start, int amount) {
		return userDAO.getUsersPage(start, amount);
	}

	@Override
	public User getByUsername(String username) {
		return userDAO.getByUsername(username);
	}

	@Override
	public void saveOrUpdate(User user) {
		userDAO.saveOrUpdate(user);
	}

	@Override
	public User getByEmail(String email) {
		return userDAO.getByEmail(email);
	}

	@Override
	public User getByVerificationToken(String token) {
		return userDAO.getByVerificationToken(token);
	}

	@Override
	public User getByPasswordToken(String token) {
		return userDAO.getByPasswordToken(token);
	}

	@Override
	public void deleteUsersWithExpiredVerificationToken() {
		userDAO.deleteUsersWithExpiredVerificationToken();

	}

	@Override
	public void deletePasswordToken(User u) {
		userDAO.deletePasswordToken(u);
	}

	@Override
	public void deleteExpiredPasswordTokens() {
		userDAO.deleteExpiredPasswordTokens();
	}

	@Override
	public List<User> getSimilars(String usernamePattern, List<String> tags) {
		return getSimilars(usernamePattern, tags);
	}

}
