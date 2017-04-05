package com.vetalzloy.projectica.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vetalzloy.projectica.service.RequestService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.SetEstimationJson;

/**
 * Deals with requests for positions
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/positions/{positionId}/request")
public class RequestController {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestController.class);
	
	@Autowired
	private RequestService requestService;
	
	/**
	 * Creates request from current user
	 * @param positionId - id of position for which request should be created
	 * @param usersAdditions - some comments from current user, why position should be gotten to him
	 * @return NOT_FOUND if position or current user doesn't exist, 
	 * or if current user already requested this position
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> createRequest(@PathVariable long positionId, 
												@RequestBody String usersAdditions){		
		logger.debug("createRequest() method was invoked for positionId = {} by user with username '{}'",
					 positionId, SecurityUtil.getCurrentUsername());
		
		try {
			requestService.createRequest(positionId, usersAdditions);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during request creating", e);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}		
	}
	
	//Request estimation is not avaible in jsp now. MB will be deleted in time.
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<String> setEstimation(@PathVariable long positionId, 
												@RequestBody SetEstimationJson body){
			
		logger.debug("setEstimation() method was invoked for positionId = {}, and body = {}",
				positionId, body);
		
		try{
			requestService.setEstimationByPositionId(positionId, 
													 body.getUsername(), 
													 body.getEstimation());
			return new ResponseEntity<String>("Estimation seted up successfully", HttpStatus.OK);
		} catch(AccessDeniedException e){
			logger.warn("Exception was thrown during setting estiamtion.", e);
			return new ResponseEntity<String>("Estimation setting was failed", HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			logger.warn("Exception was thrown during setting estiamtion.", e);
			return new ResponseEntity<String>("Estimation setting was failed", HttpStatus.BAD_REQUEST);
		}		
		
	}
	
}
