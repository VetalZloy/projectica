package com.vetalzloy.projectica.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.OnlineUtil;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.UpdateUserJson;
import com.vetalzloy.projectica.web.json.UserJson;

/**
 * Deals with request related with users
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private String nameRegExp = "\\w{2,}-{0,}\\w{0,}";
	private String urlRegExp = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OnlineUtil onlineUtil;
	
	/**
	 * Retrieves user by username or ID, fill model and return name of .jsp file will be displayed
	 * @param usernameOrId - username or ID of necessary user 
	 * @param model - {@code Model} instance will be filled
	 * @return "error" - if user with such username or ID doesn't exist, else - "user"
	 */
	@RequestMapping(path="/{usernameOrId}", method=RequestMethod.GET, produces="text/html")
	public String user(@PathVariable("usernameOrId") String usernameOrId, Model model){
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("user() method was invoked for '{}', by user with username '{}'",
						usernameOrId, currentUsername);		
		
		User user = null;
		try {
			if(isLong(usernameOrId)){
				long id = Long.parseLong(usernameOrId);
				user = userService.getById(id);
				user = userService.getFullByUsername(user.getUsername());
			} else
				user = userService.getFullByUsername(usernameOrId);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during user extraction.", e);
			return "error";
		}
		model.addAttribute("user", user);
		
		if(user.getUsername().equals(currentUsername)) 
			model.addAttribute("currentUser", true);
		else {
			model.addAttribute("online", onlineUtil.isOnline(user.getUsername()));
			model.addAttribute("currentUser", false);
		}
		
		List<Position> closedPositions = user.getPositions()
											 .stream()
											 .filter(p -> p.getFiringDate() != null)
											 .collect(Collectors.toList());
		model.addAttribute("closedPositions", closedPositions);
		
		List<Position> openPositions = new ArrayList<>(user.getPositions());
		openPositions.removeAll(closedPositions);
		Set<Project> currentProjects = openPositions.stream()
													.map(p -> p.getProject())
													.collect(Collectors.toSet());
		model.addAttribute("currentProjects", currentProjects);
		
		return "user";
	}
	
	/**
	 * Retrieves user by username or ID
	 * @param usernameOrId - username or ID of necessary user
	 * @return if such user doesn't exist - BAD_REQUEST, else - JSON of retrieved user
	 */
	@ResponseBody
	@RequestMapping(path="/{usernameOrId}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<UserJson> user(@PathVariable("usernameOrId") String usernameOrId){
		logger.debug("json user() method was invoked for '{}' by '{}'", 
						usernameOrId, SecurityUtil.getCurrentUsername());
		try {
			User u = null;
			if(isLong(usernameOrId)) {
				long id = Long.parseLong(usernameOrId);
				u = userService.getById(id);
			} else 
				u = userService.getByUsername(usernameOrId);
			
			return new ResponseEntity<UserJson>(UserJson.create(u), HttpStatus.OK);
		} catch(UserNotFoundException e) {
			logger.warn("Error happened during retreiving user.", e);
			return new ResponseEntity<UserJson>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Checks whether string is a long
	 * @param s - string will be checked
	 * @return {@code true} if string is a long value. Else - {@code false}
	 */
	private boolean isLong(String s){
		try{
			Long.parseLong(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Retrieves first page of users
	 * @param model - {@code Model} instance will be filled
	 * @return "users" always
	 */
	@RequestMapping(method=RequestMethod.GET, produces="text/html")
	public String users(Model model){
		logger.debug("users() method was invoked by user with username '{}'", 
						SecurityUtil.getCurrentUsername());
		
		List<User> users = userService.getUsersPage(0);
		model.addAttribute("users", users);
		
		return "users";
	}
	
	/**
	 * Retrieves either certain page of users or 
	 * all users which are coincides to {@code usernamePattern} and {@code tags}. 
	 * If {@code page} parameter is empty, then it will be -1, and method will find 
	 * users which are coincides to another two parameters
	 * @param page - page of users will be retrieved(can be empty)
	 * @param usernamePattern - pattern of username of necessary users ("An" => "Ann", "Lana", etc)
	 * (can be empty)
	 * @param tags - tags which should belong to necessary users(can be empty)
	 * @return if all went right - OK and list of retrieved users in form of JSON,
	 * else - BAD_REQUEST
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<UserJson>> users(
											@RequestParam(name="page", defaultValue="0") int page,
											@RequestParam(name="usernamePattern", required=false) String usernamePattern,
											@RequestParam(name="tags[]", required=false) List<String> tags){
		
		if(page > 0) //page parameter is not empty => will retrieve page of users
			return getUsersPage(page);
		else if("".equals(usernamePattern) && tags == null) // all parameter is empty
			return new ResponseEntity<List<UserJson>>(HttpStatus.BAD_REQUEST);
		else //will retrieve users which coincide to this parameters
			return getSimilarUsers(usernamePattern, tags);		
	}
	
	/**
	 * Retrieves users which coincide to method's parameters
	 * @param usernamePattern - pattern of name of necessary vacancies ("An", "ill", etc)
	 * @param tags - list of tags which necessary users should have
	 * @return list of retrieved users in form of JSON
	 */
	private ResponseEntity<List<UserJson>> getSimilarUsers(String usernamePattern, List<String> tags) {
		
		logger.debug("getSimilarUsers() method was invoked by user with username '{}'; usernamePattern '{}'; tags: '{}'",
						SecurityUtil.getCurrentUsername(), usernamePattern, tags);
		
		List<UserJson> list = userService.getSimilars(usernamePattern, tags)
										 .stream()
										 .map(u -> UserJson.create(u))
										 .collect(Collectors.toList());

		return new ResponseEntity<List<UserJson>>(list, HttpStatus.OK);
	}

	/**
	 * Retrieves page of users
	 * @param page - number of page of users will be retrieved
	 * @return list of retrieved users in form of JSON
	 */
	private ResponseEntity<List<UserJson>> getUsersPage(int page){
		logger.debug("getUsersPage() method was invoked by user with username '{}'; page = {}",
				SecurityUtil.getCurrentUsername(), page);

		List<UserJson> list = userService.getUsersPage(page)
										 .stream()
										 .map(u -> UserJson.create(u))
										 .collect(Collectors.toList());
		
		return new ResponseEntity<List<UserJson>>(list, HttpStatus.OK);
	}
	
	/**
	 * This method checks existance of email or username in db.
	 * @param valueToCheck username or email to be checked for existence.<br>
	 * <b>ATTENTION:</b> all dot symbols('.') in email must be replaced by '&2E' (without quotes).
	 * @return 
	 * <b>TRUE</b> if user with such username or email already exists, 
	 * else <b>FALSE</b> will be returned
	 */
	@RequestMapping(path="/check/{valueToCheck}", method=RequestMethod.GET)
	public ResponseEntity<Boolean> checkExistance(@PathVariable("valueToCheck") String valueToCheck){
		logger.debug("chechExistance() method was invoked for value '{}'", valueToCheck);
		
		valueToCheck = valueToCheck.replace("&2E", ".");
		return new ResponseEntity<Boolean>(userService.isExists(valueToCheck), HttpStatus.OK);				
	}
	
	/**
	 * Updates data of current user (username in path just for debug, mb deleted)
	 * @param body- - JSON which has: new name, new surname, new cvLink will be updated
	 * @return if data in body is invalid - {@code BAD_REQUEST},<br>
	 * if current user is not registered - {@code NOT_FOUND},<br> else - {@code OK}
	 */
	@ResponseBody
	@RequestMapping(path="/{username}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody UpdateUserJson body) {
		logger.debug("update() method was invoked by user with username '{}; request body: '{}'",
						SecurityUtil.getCurrentUsername(), body);
		
		boolean valid = validateUpdateData(body.getName(), body.getSurname(), body.getCvLink());
		if(!valid) 
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		
		try {
			userService.update(body.getName(), body.getSurname(), body.getCvLink());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during updating user information.", e);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}		
	}

	/**
	 * Matches parameters with regular expressions
	 * @param name
	 * @param surname
	 * @param cvLink
	 * @return if all parameters matches to regular expressions - {@code true}, 
	 * else - {@code false} 
	 */
	private boolean validateUpdateData(String name, String surname, String cvLink) {		
		
		if(!cvLink.matches(urlRegExp) && !cvLink.equals("")) {
			logger.warn("Bad format of cv link. Username - '{}'", SecurityUtil.getCurrentUsername());
			return false;
		}		
		if(!name.matches(nameRegExp)  && !name.equals("")) {
			logger.warn("Bad format of name. Username - '{}'", SecurityUtil.getCurrentUsername());
			return false;
		}		
		if(!surname.matches(nameRegExp)  && !surname.equals("")) {
			logger.warn("Bad format of surname. Username - '{}'", SecurityUtil.getCurrentUsername());
			return false;
		}		
		
		return true;
	}
	
}
