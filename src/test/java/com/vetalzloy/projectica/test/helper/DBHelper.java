package com.vetalzloy.projectica.test.helper;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DBHelper extends JdbcDaoSupport {

	@Autowired
	public void init(DataSource ds){
		setDataSource(ds);
	}
	
	public void deleteAll(){
		String baseSQL = "DELETE FROM ";
		String interlocutorDeleteSQL = baseSQL + "interlocutor";
		String usersTagsDeleteSQL = baseSQL + "users_tags";
		String positionssTagsDeleteSQL = baseSQL + "positions_tags";
		String projectDeleteSQL = baseSQL + "project";
		String verificationTokenDeleteSQL = baseSQL + "verification_token";
		String passwordTokenDeleteSQL = baseSQL + "password_token";
		String userDeleteSQL = baseSQL + "user";
		String positionDeleteSQL = baseSQL + "position";
		String requestDeleteSQL = baseSQL + "request";
		String tagDeleteSQL = baseSQL + "tag";
		String dialogMessageDeleteSQL = baseSQL + "dialog_message";
		String chatRoomDeleteSQL = baseSQL + "chat_room";
		String chatMessageDeleteSQL = baseSQL + "chat_message";
		
		getJdbcTemplate().batchUpdate(interlocutorDeleteSQL,
									  usersTagsDeleteSQL,
									  positionssTagsDeleteSQL,
									  chatMessageDeleteSQL,
									  chatRoomDeleteSQL,
									  dialogMessageDeleteSQL,
									  tagDeleteSQL,
									  requestDeleteSQL,
									  positionDeleteSQL,
									  projectDeleteSQL, 
									  verificationTokenDeleteSQL, 
									  passwordTokenDeleteSQL, 
									  userDeleteSQL);
	}
	
}
