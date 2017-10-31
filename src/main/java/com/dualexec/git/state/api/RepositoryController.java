package com.dualexec.git.state.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dualexec.git.state.service.Commit;
import com.dualexec.git.state.service.Repository;
import com.dualexec.git.state.service.RepositoryService;

@RestController
@RequestMapping("/git-state/api/v1")
public class RepositoryController {

	@Autowired
	private RepositoryService repositoryService;

	@RequestMapping(value = "/repositories", method = RequestMethod.GET)
	public @ResponseBody List<Repository> getRepositories() {
		List<Repository> repositories = repositoryService.getRepositories();
		repositories.stream().forEach(a -> {
			a.add(ControllerLinkBuilder
					.linkTo(ControllerLinkBuilder.methodOn(RepositoryController.class).getRepository(a.getName()))
					.withSelfRel());
		});
		return repositories;
	}

	@RequestMapping(value = "/repositories", method = RequestMethod.POST)
	public ResponseEntity<String> addRepository(@RequestBody Repository repository) {
		repositoryService.cloneRepository(repository);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.TEXT_PLAIN).body("Created");
	}

	@RequestMapping(value = "/repositories/{repositoryId}", method = RequestMethod.GET)
	public @ResponseBody Repository getRepository(@PathVariable String repositoryId) {
		Repository repository = repositoryService.getRepository(repositoryId);
		repository.add(ControllerLinkBuilder.linkTo(
				ControllerLinkBuilder.methodOn(RepositoryController.class).getCommitsFor(repository.getName(), "", ""))
				.withSelfRel());
		return repository;
	}

	@RequestMapping(value = "/repositories/{repositoryId}/commits", method = RequestMethod.GET)
	public @ResponseBody List<Commit> getCommitsFor(@PathVariable String repositoryId,
			@RequestParam(value = "sinceId", required = false) String sinceId,
			@RequestParam(value = "count", required = false) String count) {
		List<Commit> commits = repositoryService.getCommits(repositoryId);
		commits.stream().forEach(a -> {
			a.add(ControllerLinkBuilder.linkTo(
					ControllerLinkBuilder.methodOn(RepositoryController.class).getCommit(repositoryId, a.getHash()))
					.withSelfRel());
		});
		return commits;
	}

	@RequestMapping(value = "/repositories/{repositoryId}/commits/{commitId}", method = RequestMethod.GET)
	public @ResponseBody Commit getCommit(@PathVariable String repositoryId, @PathVariable String commitId) {
		return repositoryService.getCommit(repositoryId, commitId);
	}

}
