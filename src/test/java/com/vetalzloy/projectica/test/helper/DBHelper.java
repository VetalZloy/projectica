package com.vetalzloy.projectica.test.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DBHelper extends JdbcDaoSupport {

	private static final RowMapper<String> TABLES_ROW_MAPPER = new TablesRowMapper();
	
	@Autowired
	public void init(DataSource ds){
		setDataSource(ds);
	}
	
	public void deleteAll(){
		
		getJdbcTemplate().execute("SET REFERENTIAL_INTEGRITY FALSE");
		
		List<String> tables = getJdbcTemplate().query("SHOW TABLES", TABLES_ROW_MAPPER);
		
		String[] queries = tables.stream()
								 .map(s -> "TRUNCATE TABLE " + s)
								 .collect(Collectors.toList())
								 .toArray(new String[]{});
		
		getJdbcTemplate().batchUpdate(queries);
		
		getJdbcTemplate().execute("SET REFERENTIAL_INTEGRITY TRUE");
	}

	private static class TablesRowMapper implements RowMapper<String>{
		@Override
		public String mapRow(ResultSet rs, int arg1) throws SQLException {
			return rs.getString(1);
		}		
	}
	
}
