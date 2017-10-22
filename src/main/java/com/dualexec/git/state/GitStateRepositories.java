package com.dualexec.git.state;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GitStateRepositories {

	private Path baseRepositoryPath = Paths.get("p:\\test-git-repositories");

	private static class HelperHolder {
		public static GitStateRepositories helper = new GitStateRepositories();
	}

	public static GitStateRepositories getInstance() {
		return HelperHolder.helper;
	}

	public Path getBaseRepositoryPath() {
		return baseRepositoryPath;
	}

	public void setBaseRepositoryPath(Path baseRepositoryPath) {
		this.baseRepositoryPath = baseRepositoryPath;
	}

}
