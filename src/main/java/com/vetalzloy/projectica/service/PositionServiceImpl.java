package com.vetalzloy.projectica.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;
import com.vetalzloy.projectica.service.exception.TagNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

	private static final Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private PositionDAO positionDAO;
	
	@Autowired
	private RequestService requestService;
	
	@Override
	public Position getFullById(long positionId) throws PositionNotFoundException {
		
		logger.debug("Extracting position with id = {} ...", positionId);
		Position position = positionDAO.getFullById(positionId);
		
		if(position == null){
			throw new PositionNotFoundException("Position with id = " + positionId + " doesn't exist");
		}
		
		return position;
	}

	@Override
	public List<Position> getVacanciesPage(int page) {		
		int amount = 25; //To change this value => to change batch size on Project class (under @Entity)
		logger.debug("Retreiving page with {} free positions from {} ...", amount, amount*page);
		return positionDAO.getFreePositionsPage(page*amount, amount);
	}


	@Override
	public List<Position> getSimilarVacancies(String namePattern, List<String> tags) {
		if((tags == null || tags.size() == 0) && (namePattern == null || namePattern.equals("")))
			return new ArrayList<>();
		
		if(tags == null) tags = new ArrayList<>();
		if(namePattern == null) namePattern = "";
		
		logger.debug("Retreiving vacancies like '{}' with definite tags.", namePattern);	
		
		return positionDAO.getFreeSimilarPositions(namePattern, tags);
	}

	@Override
	public Position getById(long positionId) throws PositionNotFoundException {
		logger.debug("Retrieving position by id {} ...", positionId);
		Position position = positionDAO.getById(positionId);
		if(position == null)
			throw new PositionNotFoundException("Position with id = " + positionId + " doesn't exist");
		
		return position;
	}

	@Override
	public void putUser(long positionId, String username) 
							throws EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		logger.debug("Checking data validity...");
		
		//checking request existence
		requestService.getByPositionIdAndUsername(positionId, username);
		
		Position p = positionDAO.getById(positionId);
		if(p == null) throw new PositionNotFoundException(positionId); //just for confidence :)
		
		User user = userService.getByUsername(username);
		if(user == null) //just for confidence :)
			throw new UserNotFoundException("User with usename = " + username + " doesn't exist"); 
		
		//checking whether current user is creator of necessary project
		String currentUser = SecurityUtil.getCurrentUsername();
		String positionCreator = p.getProject().getCreator().getUsername();
		if(! currentUser.equals(positionCreator)) 
			throw new AccessDeniedException("Trying to set position executive"
											+ ", positionId = " + positionId
											+ ", new username = " + username
											+ ", by user with username = " + currentUser);
		
		if(p.getUser() != null) 
			throw new AccessDeniedException("Trying to change current position executive,"
									+ " positionId = " + positionId + ", new username = " + username);
		
		logger.info("Data checked successfull for positionId {} and username {}", positionId, username);
		p.setUser(user);
		p.setHiringDate(LocalDateTime.now());
		positionDAO.saveOrUpdate(p);
		
		Project project = projectService.getFullById(p.getProject().getId());
		for(ChatRoom room: project.getChatRooms())
			chatService.addUsersToChatRoom(room, p.getUser());
	}

	@Override
	public void close(long positionId, boolean estimation, String comment) 
							throws AccessDeniedException, EntityNotFoundException, ExternalResourceAccessException {
		logger.debug("Closing position {}...", positionId);
		
		Position position = positionDAO.getById(positionId);
		if(position == null) throw new PositionNotFoundException(positionId);
		
		//checking whether current user is creator of necessary project
		String currentUsername = SecurityUtil.getCurrentUsername();
		String creatorUsername = position.getProject().getCreator().getUsername();
		if(! currentUsername.equals(creatorUsername)){
			throw new AccessDeniedException(currentUsername + " tried to close position with id = " + positionId);
		}
		
		//checking whether creator tries to close himself position
		String positionOwnerUsername = position.getUser().getUsername();
		if(creatorUsername.equals(positionOwnerUsername)) 
			throw new AccessDeniedException("Creator '"+creatorUsername+"' tried to close himself position with id = " + positionId);
		
		
		if(position.getFiringDate() == null) 
			position.setFiringDate(LocalDateTime.now());
		
		position.setEstimation(estimation);
		position.setComment(comment);
		positionDAO.saveOrUpdate(position);
		
		Project project = projectService.getFullById(position.getProject().getId());
		for(ChatRoom room: project.getChatRooms())
			chatService.removeUsersFromChatRoom(room, position.getUser());

	}

	@Override
	public Position createPosition(int projectId, String name, String description, List<String> tags)
			throws EntityNotFoundException, AccessDeniedException {

		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Creating position with name = '{}' for project with id = {} by user with username = '{}'...", 
				name, projectId, currentUsername);
		
		User user = userService.getByUsername(currentUsername);
		if(user == null)
			throw new UserNotFoundException("User with username " + currentUsername + " doesn't exist.");
		
		//checking whether currant user is creator of necessary project
		Project project = projectService.getById(projectId);		
		if(! project.getCreator().getUsername().equals(currentUsername))
			throw new AccessDeniedException("Trying to create new position for project with id = " + 
											projectId + " by user with username = " + currentUsername);
		
		Position position = new Position(name, description, project);
		if(tags != null)
			for(String tag: tags)
				try{
					//if tag exists
					position.getTags().add(tagService.getByTag(tag));
				} catch(TagNotFoundException e) {
					//if tag doesn't exist
					position.getTags().add(tagService.create(tag));
				}			
			
		
		
		positionDAO.saveOrUpdate(position);
		
		return position;
	}

	@Override
	public void update(long positionId, String name, String description) throws EntityNotFoundException, AccessDeniedException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Updating data for position with id = {} by user with username '{}' ...", 
						positionId,	currentUsername);
		
		User currentUser = userService.getByUsername(currentUsername);		
		if(currentUser == null) 
			throw new UserNotFoundException("User with username '" + currentUsername + "' is not registered");
		
		Position p = positionDAO.getById(positionId);
		if(p == null) 
			throw new PositionNotFoundException(positionId);
		
		String creatorUsername = p.getProject().getCreator().getUsername();
		if(! currentUsername.equals(creatorUsername))
			throw new AccessDeniedException("Trying to update data of position with id = "+positionId+", by user with username "+currentUsername);
	
		p.setName(name);
		p.setDescription(description);
		
		positionDAO.saveOrUpdate(p);
	}

}
