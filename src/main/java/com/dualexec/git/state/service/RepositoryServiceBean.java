package com.dualexec.git.state.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dualexec.git.state.GitStateRepositories;
import com.dualexec.git.state.exception.ResourceNotFoundException;

@Service
public class RepositoryServiceBean implements RepositoryService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Repository> getRepositories() {
		Path baseRepositoryPath = GitStateRepositories.getInstance().getBaseRepositoryPath();
		List<Repository> repositories = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(baseRepositoryPath)) {
			for (Path path : directoryStream) {
				Repository repository = new Repository(path.getFileName().toString());
				repositories.add(repository);
			}
		} catch (IOException e) {
			logger.error("Failed to list all repositories.", e);
			throw new ResourceNotFoundException(0L, "repositories not found");
		}
		return repositories;
	}

	@Override
	public Repository getRepository(String id) {
		Path baseRepositoryPath = GitStateRepositories.getInstance().getBaseRepositoryPath();
		if (Files.exists(baseRepositoryPath.resolve(id))) {
			return new Repository(id);
		}
		throw new ResourceNotFoundException(0L, "repository not found");
	}

	@Override
	public List<Commit> getCommits(String repositoryId) {
		List<Commit> commits = new ArrayList<>();
		Path baseRepositoryPath = GitStateRepositories.getInstance().getBaseRepositoryPath();
		Path repositoryPath = baseRepositoryPath.resolve(repositoryId + "/" + ".git");
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try (org.eclipse.jgit.lib.Repository repository = builder.setGitDir(repositoryPath.toFile()).readEnvironment()
				.findGitDir().build()) {
			try (Git git = new Git(repository)) {
				ObjectId head = repository.resolve(Constants.MASTER);
				// Iterable<RevCommit> logs = git.log().add(head).setMaxCount(1).call();
				Iterable<RevCommit> logs = git.log().add(head).call();
				for (RevCommit rev : logs) {
					Date date = new Date(rev.getCommitTime() * 1000l);
					DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
					String formatted = format.format(date);
					Commit commit = new Commit(rev.getCommitTime() * 1000l, formatted, rev.getName(),
							rev.getAuthorIdent().getName(), rev.getFullMessage());
					commits.add(commit);
				}
			} catch (RevisionSyntaxException | GitAPIException e) {
				logger.error("Failed to list all commits.", e);
				throw new ResourceNotFoundException(0L, "failed to load commit");
			}
		} catch (IOException e) {
			logger.error("Failed to list all commits.", e);
			throw new ResourceNotFoundException(0L, "failed to load commit");
		}
		return commits;
	}

	@Override
	public Commit getCommit(String repositoryId, String commitId) {
		Optional<Commit> findFirst = getCommits(repositoryId).stream().filter(a -> a.getHash().equals(commitId))
				.findFirst();
		try {
			return findFirst.get();
		} catch (NoSuchElementException e) {
			logger.error("Could not find commitId in repository.", e);
			throw new ResourceNotFoundException(0L, "failed to load commit");
		}
	}

}
