package com.dualexec.git.state.service;

import java.util.List;

public interface RepositoryService {

	List<Repository> getRepositories();

	Repository getRepository(String repositoryId);

	List<Commit> getCommits(String repositoryId);

	Commit getCommit(String repositoryId, String commitId);

}
