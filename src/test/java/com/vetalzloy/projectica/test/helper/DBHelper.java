package com.vetalzloy.projectica.test.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.UserDAO;

@Repository
@Transactional
public class DBHelper extends JdbcDaoSupport {

	private static final RowMapper<String> TABLES_ROW_MAPPER = new TablesRowMapper();
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	public void init(DataSource ds){
		setDataSource(ds);
	}
	
	public void truncate(){
		
		getJdbcTemplate().execute("SET REFERENTIAL_INTEGRITY FALSE");
		
		List<String> tables = getJdbcTemplate().query("SHOW TABLES", TABLES_ROW_MAPPER);		
		String[] queries = tables.stream()
								 .map(s -> "TRUNCATE TABLE " + s)
								 .collect(Collectors.toList())
								 .toArray(new String[]{});
		
		getJdbcTemplate().batchUpdate(queries);
		
		getJdbcTemplate().execute("SET REFERENTIAL_INTEGRITY TRUE");
	}
	
	public User createDefaultUser() {
		return createUser("VetalZloy", "vetalzloy@gmail.com", "111");
	}
	
	@Transactional
	public User createUser(String username, String email, String password) {
		User user = new User(username, email, password);
		user.setEnabled(true);
		userDAO.saveOrUpdate(user);
		return user;
	}
	

	private static class TablesRowMapper implements RowMapper<String>{
		@Override
		public String mapRow(ResultSet rs, int arg1) throws SQLException {
			return rs.getString(1);
		}		
	}
	
}
