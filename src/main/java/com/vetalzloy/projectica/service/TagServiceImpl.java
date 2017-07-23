package com.vetalzloy.projectica.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.TagDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;
import com.vetalzloy.projectica.service.exception.TagNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class TagServiceImpl implements TagService {

	private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PositionService positionService;

	@Override
	public Tag getByTag(String tag) throws TagNotFoundException {
		logger.debug("Extracting tag = '{}'...", tag);
		Tag t = tagDAO.get(tag);
		if(t == null) 
			throw new TagNotFoundException("Tag '" + tag + "' doesn't exist");
		return tagDAO.get(tag);
	}
	
	@Override
	public List<Tag> getSimilar(String tag) {
		logger.debug("Extracting tags like '{}' ...", tag);
		return tagDAO.getSimilar(tag);
	}

	@Override
	public Tag create(String tag) {
		logger.debug("Creating tag '{}'", tag);
		Tag t = new Tag(tag);
		tagDAO.saveOrUpdate(t);
		return t;
	}
	
	@Override
	public void attachToUser(String tag) throws UserNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		User user = userService.getByUsername(currentUsername);
		if(user == null) 
			throw new UserNotFoundException("User with username " + currentUsername + " doesn't exist.");
		
		logger.debug("Checking tag '{}' existance ...", tag);
		Tag t = tagDAO.get(tag);
		if(t == null){
			logger.info("Tag '{}' doesn't exist => creating this tag...", tag);
			t = new Tag(tag);
			tagDAO.saveOrUpdate(t);
		}
		logger.info("Tag '{}' exists => putting to user tags.", t);
		
		user.getTags().add(t);
	}

	@Override
	public void detachFromUser(String tag) throws EntityNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();

		logger.debug("Checking user with username {} existence ...", currentUsername);
		User user = userService.getByUsername(currentUsername);
		if(user == null) 
			throw new UserNotFoundException("User with username " + currentUsername + " doesn't exist.");
		
		logger.debug("Checking tag '{}' existence ...", tag);
		Tag t = tagDAO.get(tag);
		if(t == null)
			throw new TagNotFoundException("Tag '" + tag + "' doesn't exist.");
		
		logger.debug("Detaching tag '{}' from user with username '{}'", tag, currentUsername);
		user.getTags().remove(t);
	}

	@Override
	public void attachToPosition(long positionId, String tag) throws PositionNotFoundException, AccessDeniedException {
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		Position position = positionService.getById(positionId);
		if(position == null)
			throw new PositionNotFoundException(positionId);
		
		String projectCreatorUsername = position.getProject().getCreator().getUsername();
		if(! currentUsername.equals(projectCreatorUsername))
			throw new AccessDeniedException("User '" + currentUsername + 
					"' is not creator of project with positionId = " + positionId);
		
		Tag t = tagDAO.get(tag);
		if(t == null){
			logger.info("Tag '{}' doesn't exist ==> creating this tag...");
			t = new Tag(tag);
			tagDAO.saveOrUpdate(t);
		}
		
		logger.info("Tag '{}' exists => putting to position tags.", t);
		position.getTags().add(t);
	}

	@Override
	public void detachFromPosition(long positionId, String tag) throws EntityNotFoundException, AccessDeniedException {
		String currentUsername = SecurityUtil.getCurrentUsername();

		Position position = positionService.getById(positionId);
		if(position == null)
			throw new PositionNotFoundException(positionId);
		
		String creatorUsername = position.getProject().getCreator().getUsername();
		if(! currentUsername.equals(creatorUsername))
			throw new AccessDeniedException("User '" + currentUsername + 
					"' is not creator of project with positionId = " + positionId);
		
		Tag t = tagDAO.get(tag);
		if(t == null)
			throw new TagNotFoundException("Tag '" + tag + "' doesn't exist.");
		
		
		logger.info("Tag '{}' exists => detaching from position tags.", t);
		position.getTags().remove(t);
	}
}
