package com.vetalzloy.projectica.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.ProjectJson;
import com.vetalzloy.projectica.web.json.UpdateProjectJson;

/**
 * Deals with requests related with projects
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {	

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private ProjectService projectService;
	
	/**
	 * Retrieves first page of projects and returns name of .jsp file will be displayed
	 * @param model - {@code Model} instance will be filed
	 * @return "projects" always
	 */
	@RequestMapping(method=RequestMethod.GET, produces="text/html")
	public String projects(Model model){		
		logger.debug("projects() method was invoked by user with usernmae '{}'", 
				SecurityUtil.getCurrentUsername());
		
		List<Project> page = projectService.getProjectsPage(0);		
		List<ProjectJson> projects = page.stream()
										 .map(p -> ProjectJson.create(p))
										 .collect(Collectors.toList());
		
		model.addAttribute("projects", projects);
		return "projects";
	}

	/**
	 * Retrieves page of projects and returns it
	 * @param page - number of page will be retrieved
	 * @return list of retrieved projects in form of JSON
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<ProjectJson>> getProjetsPage(@RequestParam("page") int page){
		logger.debug("getProjetsPage() method was invoked by user wth username '{}' and page = {}",
						SecurityUtil.getCurrentUsername(), page);
		
		List<Project> list = projectService.getProjectsPage(page);		
		List<ProjectJson> projects = list.stream()
										 .map(p -> ProjectJson.create(p))
										 .collect(Collectors.toList());
		
		return new ResponseEntity<List<ProjectJson>>(projects, HttpStatus.OK);
	}
	
	/**
	 * Retrieves projects which have names which coincide to {@code pattern}
	 * @param pattern - pattern of name of necessary projects
	 * @return list of retrieved projects in form of JSON
	 */
	@ResponseBody
	@RequestMapping(path="/{pattern}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<ProjectJson>> getSimilarProjects(@PathVariable("pattern") String pattern) {
		logger.debug("getSimilarProjects() method was invoked by user wtih username '{}'; pattern '{}'", 
				SecurityUtil.getCurrentUsername(), pattern);
		
		List<Project> list = projectService.getSimilarProjects(pattern);
		List<ProjectJson> projects = list.stream()
										 .map(p -> ProjectJson.create(p))
										 .collect(Collectors.toList());

		return new ResponseEntity<List<ProjectJson>>(projects, HttpStatus.OK);
	}
	
	/**
	 * Retrieves project by id, fills model and returns name of .jsp file will be displayed
	 * @param projectId - id of necessary project
	 * @param model - {@code Model} instance will be filled
	 * @return if such project doesn't exist - "error", else - "project"
	 */
	@RequestMapping(path="/{projectId}", method=RequestMethod.GET, produces="text/html")
	public String getProjectById(@PathVariable("projectId") int projectId, Model model){		
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("getProjectById() method was invoked by user with username '{}'; for projectId = {}", 
						currentUsername, projectId);
		
		Project project = null;
		try {
			project = projectService.getFullById(projectId);
		} catch (ProjectNotFoundException e) {
			logger.warn("Error happened during projects retrieving.", e);
			return "error";
		}
		model.addAttribute("project", project);
		
		Set<Position> openPositions = project.getPositions()
											 .stream()
											 .filter(p -> p.getUser() == null)
											 .collect(Collectors.toSet());
		model.addAttribute("openPositions", openPositions);
		
		Map<User, Set<Position>> participants = new HashMap<>();
		Set<Position> currentPositions = project
										.getPositions()
										.stream()
										.filter(p -> p.getHiringDate() != null && p.getFiringDate() == null)
										.collect(Collectors.toSet());		
		for(Position position: currentPositions){
			if(participants.get(position.getUser()) == null)
				participants.put(position.getUser(), new HashSet<>(Arrays.asList(position)));
			participants.get(position.getUser()).add(position);
		}
		model.addAttribute("participants", participants);
		
		Set<Position> closedPositions = project
										.getPositions()
										.stream()
										.filter(p -> p.getFiringDate() != null)
										.collect(Collectors.toSet());
		model.addAttribute("closedPositions", closedPositions);		
		
		if(currentUsername.equals(project.getCreator().getUsername()))
			model.addAttribute("creator", true);
		else
			model.addAttribute("creator", false);
		
		boolean participant = currentPositions
							  .stream()
							  .anyMatch(p -> p.getUser().getUsername().equals(currentUsername));
		model.addAttribute("participant", participant);	
		
		return "project";
	}
	
	/**
	 * Creates new project and redirects to new project's page
	 * @param name - desirable name of new project
	 * @param creatorPosition - name of position which will be belong to current user (creator)
	 * @param description - description of new project
	 * @return "index" - if current user is not registered,
	 * "error" - if something another went wrong,
	 * else redirects to just created project's page
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String createProject(@RequestParam(name="name") String name, 
								@RequestParam(name="position") String creatorPosition,
								@RequestParam(name="description") String description){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("createProject() method was invoked by user with name '{}'; project name '{}' ", 
				currentUsername, name);
		
		if("anonymousUser".equals(currentUsername)) {
			logger.warn("Creating project with name {} by unregistered user", name);
			return "index";
		}
		
		try {
			Project project = projectService.createProject(name, creatorPosition, description);
			return "redirect:/projects/" + project.getId();
		} catch (UserNotFoundException | ProjectAlreadyExistsException | ExternalResourceAccessException e) {
			logger.warn("Error happened during extracting user.", e);
			return "error";
		}		
	}
	
	/**
	 * Updates project data.
	 * @param positionId - id of position will be updated
	 * @param body - request body which has new name and new description
	 * @return BAD_REQUEST if something went wrong, else - OK
	 */
	@ResponseBody
	@RequestMapping(path="/{projectId}", method=RequestMethod.PUT, produces="application/json")
	public ResponseEntity<Void> update(@PathVariable("projectId") int projectId, 
									   @RequestBody UpdateProjectJson body){
		
		logger.debug("update() method was invoked by user with username, fro project with id = {}",
						SecurityUtil.getCurrentUsername(), projectId);
		
		try {
			projectService.update(projectId, body.getName(), body.getDescription());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (EntityNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during updating project data.", e);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Checks existence of project with such {@code projectName}
	 * @param projectName - name of project, whose existence should be checked
	 * @return TRUE - project exists, else - FALSE
	 */
	@ResponseBody
	@RequestMapping(path="/check/{projectName}", method=RequestMethod.GET)
	public ResponseEntity<Boolean> checkExistance(@PathVariable("projectName") String projectName){
		
		logger.debug("checkExistance() method was invoked by user with username '{}'; project name to check '{}'",
				SecurityUtil.getCurrentUsername(), projectName);
		
		return new ResponseEntity<Boolean>(projectService.isExist(projectName), HttpStatus.OK);
	}
	
}