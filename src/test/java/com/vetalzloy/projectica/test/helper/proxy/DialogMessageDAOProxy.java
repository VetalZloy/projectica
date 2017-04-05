package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.service.dao.DialogMessageDAO;

@Transactional
public class DialogMessageDAOProxy implements DialogMessageDAO {

	@Autowired
	@Qualifier("dialogMessageDAOImpl")
	private DialogMessageDAO dialogMessageDAO;
	
	@Override
	public void saveOrUpdate(DialogMessage message) {
		dialogMessageDAO.saveOrUpdate(message);
	}

	@Override
	public List<DialogMessage> getFirstPage(int amount, long userId1, long userId2) {
		return dialogMessageDAO.getFirstPage(amount, userId1, userId2);
	}

	@Override
	public List<DialogMessage> getPageBeforeEarliest(long earliestMessageId, int amount, long userId1, long userId2) {
		return dialogMessageDAO.getPageBeforeEarliest(earliestMessageId, amount, userId1, userId2);
	}

}
