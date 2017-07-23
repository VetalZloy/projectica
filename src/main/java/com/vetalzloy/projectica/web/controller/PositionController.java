package com.vetalzloy.projectica.web.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.service.PositionService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.ClosePositionJson;
import com.vetalzloy.projectica.web.json.CreatePositionJson;
import com.vetalzloy.projectica.web.json.PositionJson;
import com.vetalzloy.projectica.web.json.UpdatePositionJson;

/**
 * Deals with all requests pointed on positions(and, thereby, vacancies)
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/positions")
public class PositionController {
	
	private static final Logger logger = LoggerFactory.getLogger(PositionController.class);
	
	@Autowired
	private PositionService positionService;
	
	/**
	 * Retrieves first page of vacancies and return name of .jsp file will be displayed
	 * @param model - {@code Model} instance will be filled by vacancies
	 * @return "positions" always
	 */
	@RequestMapping(method=RequestMethod.GET, produces="text/html")
	public String vacancies(Model model){		
		logger.debug("vacancies() method was invoked by user with username '{}'", 
						SecurityUtil.getCurrentUsername());
		
		List<PositionJson> list = positionService.getVacanciesPage(0)
												 .stream().map(pos -> PositionJson.create(pos))
												 .collect(Collectors.toList());		
		model.addAttribute("vacancies", list);
		return "positions";
	}
	
	/**
	 * Retrieves either certain page of vacancies or 
	 * all vacancies which are coincides to {@code namePattern} and {@code tags}. 
	 * If {@code page} parameter is empty, then it will be -1, and method will find 
	 * positions which are coincides to another two parameters
	 * @param page - page of vacancies will be retrieved(can be empty)
	 * @param namePattern - pattern of name of necessary vacancies ("developer", "modeler", etc)
	 * (can be empty)
	 * @param tags - tags which should belong to necessary vacancies(can be empty)
	 * @return if all went right - OK and list of retrieved vacancies in form of JSON,
	 * else - BAD_REQUEST
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<PositionJson>> vacancies(
							@RequestParam(name="page", defaultValue="-1") int page, 
							@RequestParam(name="namePattern", required=false) String namePattern,
							@RequestParam(name="tags[]", required=false) List<String> tags) {
		
		if(page != -1) //page parameter is not empty => will retrieve page of vacancies
			return getVacanciesPage(page);
		else if("".equals(namePattern) && tags == null) // all parameter is empty
			return new ResponseEntity<List<PositionJson>>(HttpStatus.BAD_REQUEST);
		else //will retrieve vacancies which coincide to this parameters
			return getSimilarVacancies(namePattern, tags);
		
	}
	
	/**
	 * Retrieves page of vacancies
	 * @param page - number of page of vacancies will be retrieved
	 * @return list of retrieved vacancies in form of JSON
	 */
	private ResponseEntity<List<PositionJson>> getVacanciesPage(int page){
		logger.debug("getVacanciesPage() method was invoked by user with username '{}'; page = {}",
				SecurityUtil.getCurrentUsername(), page);
		
		List<PositionJson> list = positionService.getVacanciesPage(page)
												 .stream().map(pos -> PositionJson.create(pos))
												 .collect(Collectors.toList());
		
		return new ResponseEntity<List<PositionJson>>(list, HttpStatus.OK);
	}
	
	/**
	 * Retrieves vacancies which coincide to method parameters
	 * @param namePattern - pattern of name of necessary vacancies ("developer", "modeler", etc)
	 * @param tags - list of tags which necessary vacancies should have
	 * @return list of retrieved vacancies in form of JSON
	 */
	private ResponseEntity<List<PositionJson>> getSimilarVacancies(String namePattern, List<String> tags){
		
		logger.debug("getSimilarVacancies() method was invoked by user with username '{}'; namePattern '{}'; tags '{}'.",
				SecurityUtil.getCurrentUsername(), namePattern, tags);
		
		List<PositionJson> list = positionService.getSimilarVacancies(namePattern, tags)
												 .stream()
												 .map(pos -> PositionJson.create(pos))
												 .collect(Collectors.toList());		
		return new ResponseEntity<List<PositionJson>>(list, HttpStatus.OK);
	}
	
	/**
	 * Retrieves position by id, fill model and returns name of .jsp file will be displayed
	 * @param positionId - id of position will be retrieved
	 * @param model - {@code Model} instance will be filled by position data
	 * @return "error" if position with such {@code positionId} doesn't exist,
	 * else - "position"
	 */
	@RequestMapping(path="/{positionId}", method=RequestMethod.GET)
	public String getPositionById(@PathVariable("positionId") long positionId, Model model){
		logger.debug("getPositionById() method was invoked for positionId = {}", positionId);
		
		Position position = null;
		try {
			position = positionService.getFullById(positionId);
		} catch (PositionNotFoundException e) {
			logger.warn("Error happened during position extracting.", e);
			return "error";
		}		
		model.addAttribute("position", position);		
		
		//TODO remove this block and update view
		if(position.getUser() != null) {// if it's not free (not vacancy)
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String hiringDate = position.getHiringDate().format(formatter);
			model.addAttribute("hiringDate", hiringDate);
			
			if(position.getFiringDate() != null) { //if it's closed
				String firingDate = position.getFiringDate().format(formatter);
				model.addAttribute("firingDate", firingDate);
			}
		}		
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		String creatorUsername = position.getProject().getCreator().getUsername();
		if(currentUsername.equals(creatorUsername))
			model.addAttribute("creator", true);
		else
			model.addAttribute("creator", false);
		
		model.addAttribute("creatorPosition", false);
		if(position.getUser() != null){
			String positionOwnerUsername = position.getUser().getUsername();
			if(creatorUsername.equals(positionOwnerUsername))
				model.addAttribute("creatorPosition", true);
		}		
		
		//checking whether current user requested this position
		boolean userRequest = position.getRequests()
									  .stream()
									  .map(r -> r.getUser().getUsername())
									  .anyMatch(u -> u.equals(currentUsername));
		
		String status;		
		if(position.getUser() == null) {
			status = "Free";
			if(userRequest) status = "Requested";
		}
		else if(position.getFiringDate() == null) status = "Active";
		else status = "Closed";
		
		model.addAttribute("status", status);
		
		return "position";
	}
	
	/**
	 * Creates new position and returns id of created position
	 * @param body - JSON which has: 
	 * <ul>
	 * <li>id of project which will contain new position</li>
	 * <li>desirable name of new position</li>
	 * <li>description of new position</li>
	 * <li>tags for new position</li>
	 * </ul>
	 * @return if something went wrong - 0 and BAD_REQUEST,
	 * else id of just created position and OK
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Long> createPosition(@RequestBody CreatePositionJson body){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("createPosition() was invoked by user with username = '{}'; request body = {}",
				currentUsername, body);		
		
		try {
			Position pos = positionService.createPosition(body.getProjectId(), 
													 	  body.getName(), 
													 	  body.getDescription(), 
													 	  body.getTags());
			return new ResponseEntity<Long>(pos.getId(), HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during creating position by user with username = '{}' from request body = {}", 
					currentUsername, body, e);
			return new ResponseEntity<Long>(0L, HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * Updates position data.
	 * @param positionId - id of position will be updated
	 * @param body - request body which has new name and new description
	 * @return BAD_REQUEST if something went wrong, else - OK
	 */
	@ResponseBody
	@RequestMapping(path="/{positionId}", method=RequestMethod.PUT, produces="application/json")
	public ResponseEntity<Void> update(@PathVariable("positionId") long positionId, 
						 			   @RequestBody UpdatePositionJson body){
		
		logger.debug("update() method was invoked by user with username '{}'; for position with id = {}",
						SecurityUtil.getCurrentUsername(), positionId);		
		
		try {
			positionService.update(positionId, body.getName(), body.getDescription());			
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during position updating.", e);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Puts user with to position and returns result of operation
	 * @param username - username of user will be put to position
	 * @param positionId - id of position which will has user as executive
	 * @return NOT_FOUND - if current user is not creator of project which has such position 
	 * or if user with such {@code username} or position with such {@code positionId} 
	 * doesn't exist,<br>
	 * else OK will be returned
	 */
	@RequestMapping(path="/{positionId}/{username}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Void> putUserToPosition(@PathVariable("positionId") long positionId,
												  @PathVariable("username") String username){
		logger.debug("putUserToPosition() method was invoked for username = {} and positionID = {}",
						username, positionId);
		try {
			positionService.putUser(positionId, username);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException | ExternalResourceAccessException e) {
			logger.warn("Error happened during setting user into position.", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	/**
	 * Closes position and returns result of operation
	 * @param body - JSON which contains estimation and comment of executive's work
	 * @param positionId - id of position will be closed
	 * @return NOT_FOUND - if current user is not creator of project which has such position 
	 * or if user with such {@code username} or position with such {@code positionId} 
	 * doesn't exist,<br>
	 * else OK will be returned
	 */
	@RequestMapping(path="/{positionId}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Void> closePosition(@RequestBody ClosePositionJson body, 
											  @PathVariable long positionId){
		logger.debug("closePosition() method was invoked for positionId = {} by user with username '{}'",
					  positionId, SecurityUtil.getCurrentUsername());
		
		try {
			positionService.close(positionId, body.isEstimation(), body.getComment());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AccessDeniedException | EntityNotFoundException | ExternalResourceAccessException e) {
			logger.warn("Error happened during closing position.", e);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}		
	}	
}
