package com.vetalzloy.projectica.web.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.service.TagService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

/**
 * Deals with tags
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/tag")
public class TagController {
	
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);
	
	@Autowired
	private TagService tagService;
	
	/**
	 * Retrieves tags which are similar to {@code tag} 
	 * @param tag - pattern of necessary tag ("Java" => "JavaScript", "Java", "Java9", etc)
	 * @return list of retrieved tags in form of JSON
	 */
	@ResponseBody
	@RequestMapping(path="/{tag}", method=RequestMethod.GET)
	public ResponseEntity<List<String>> getSimilar(@PathVariable("tag") String tag){
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("getSimilar() method was invoked for tag {} by user with username '{}'", tag, username); 
		List<Tag> list = tagService.getSimilar(tag);
		
		List<String> resultList = list.stream()
									  .map(t -> t.getTag())
									  .collect(Collectors.toList());
				
		return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
	}
	
	/**
	 * Attaches {@code tag} to current user
	 * @param tag - tag will be attached
	 * @return NOT_FOUND - if current user is not registered,
	 * else - OK
	 */
	@ResponseBody
	@RequestMapping(method={RequestMethod.PUT, RequestMethod.POST})
	public ResponseEntity<Void> attachTagToUser(@RequestBody String tag){
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("attachTagToUser() method was invoked for tag '{}' by user with username '{}'", tag, username); 
		try {
			tagService.attachToUser(tag);
			return new ResponseEntity<>(HttpStatus.OK);	
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during tag putting.", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}			
	}

	/**
	 * Attaches {@code tag} from posiotion
	 * @param tag - tag will be attached
	 * @param positionId - id of position which will contain such {@code tag}
	 * @return NOT_FOUND if current user or such position doesn't exist, or if 
	 * current user is not creator of project which contain such position,<br>
	 * else OK will be returned
	 */
	@ResponseBody
	@RequestMapping(path="/{positionId}", method={RequestMethod.PUT, RequestMethod.POST})
	public ResponseEntity<Void> attachTagToPosition(@RequestBody String tag, 
												 @PathVariable("positionId") long positionId){
		
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("attachTagToPosition() method was invoked for tag '{}' for position {} by user wtih username '{}'", 
				tag, positionId, username); 
		try {
			tagService.attachToPosition(positionId, tag);
			return new ResponseEntity<>(HttpStatus.OK);	
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during tag putting.", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}			
	}
	
	/**
	 * This method detaches tag from EXACTLY CURRENT USER. 
	 * It means doesn't matter from whose page method was invoked.
	 * @param tag - tag will be detached
	 * @return OK - if tag was detached successfully, else NOT_FOUND
	 */
	@ResponseBody
	@RequestMapping(path="/{tag}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> detachTagFromUser(@PathVariable("tag") String tag){
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("detachTagFromUser() method was invoked for tag '{}' by user with username '{}'", 
						tag, username);
		
		try {
			tagService.detachFromUser(tag);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			logger.warn("Error happened during tag detaching.", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	/**
	 * Detaches tag from position
	 * @param tag - tag will be detached
	 * @param positionId - id of position will lose such {@code tag}
	 * @return NOT_FOUND if current user or such position doesn't exist, or if 
	 * current user is not creator of project which contain such position,<br>
	 * else OK will be returned
	 */
	@RequestMapping(path="/{positionId}/{tag}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> detachTagFromPosition(@PathVariable("tag") String tag, 
													  @PathVariable("positionId") long positionId){
		
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("detachTagFromPosition() method was invoked for tag '{}' for position with positionId = {} by user with username '{}'",
						tag, positionId, username);
		
		try {
			tagService.detachFromPosition(positionId, tag);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during tag detaching.", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
}
