package com.vetalzloy.projectica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Request;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.RequestDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.RequestNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
	
	@Autowired
	private RequestDAO requestDAO;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private UserService userService;

	@Override
	public Request getByPositionIdAndUsername(long positionId, String username) 
												throws RequestNotFoundException {
		logger.debug("Retreving request from user with username '{}' for position with id = {}",
						username, positionId);
		Request req = requestDAO.getByPositionIdAndUsername(positionId, username);
		if(req == null)
			throw new RequestNotFoundException(positionId, username);
		
		return req;
	}

	@Override
	public Request createRequest(long positionId, String usersAdditions) 
								throws EntityNotFoundException, AccessDeniedException {
		String currentUsername = SecurityUtil.getCurrentUsername();		
		logger.debug("Creating request for positionId = {} and username {}", positionId, currentUsername);
		
		//retrieving and checking existence
		User user = userService.getByUsername(currentUsername);
		
		//retrieving and checking existence
		Position position = positionService.getById(positionId);
		
		//checking whether current user have already requested this position
		boolean userExistanse = position.getRequests()
									 	.stream()
									 	.map(r -> r.getUser().getUsername())
									 	.anyMatch(u -> u.equals(currentUsername));
		if(userExistanse)
			throw new AccessDeniedException("Trying to create more than one request by user with username "
												+ currentUsername + " for position with id = " + positionId);
		
		Request req = new Request(position, user);
		req.setUserAdditions(usersAdditions);
		
		requestDAO.saveOrUpdate(req);
		
		return req;
	}
	
	@Override
	public void setEstimationByPositionId(long positionId, String username, int estimation) throws AccessDeniedException, EntityNotFoundException {
		
		Request req = requestDAO.getByPositionIdAndUsername(positionId, username);
		if(req == null)
			throw new RequestNotFoundException(positionId, username);		
		
		//checking whether current user is creator of necessary project
		String currentUsername = SecurityUtil.getCurrentUsername();
		Position position = req.getPosition();		
		String creatorUsername = position.getProject()
										 .getCreator()
										 .getUsername();		
		if(! currentUsername.equals(creatorUsername))
			throw new AccessDeniedException("User " + currentUsername + 
										" is not creator of project with positionId = " + positionId);
		
		req.setEstimation(estimation);		
		requestDAO.saveOrUpdate(req);
	}

}
