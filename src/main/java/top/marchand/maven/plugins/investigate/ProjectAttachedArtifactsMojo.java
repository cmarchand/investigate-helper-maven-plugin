package top.marchand.maven.plugins.investigate;


import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

@Mojo(name = "attached-artifacts")
public class ProjectAttachedArtifactsMojo extends AbstractMojo {
  @Inject
  MavenProject mavenProject;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    ArrayList<Artifact> artifacts = new ArrayList<>(mavenProject.getAttachedArtifacts());
    artifacts.add(mavenProject.getArtifact());

    artifacts.stream()
        .filter(Objects::nonNull)
        .map(ProjectAttachedArtifactsMojo::artifactToString)
        .forEach(getLog()::info);
  }

  private static String artifactToString(Artifact artifact) {
    StringJoiner stringJoiner = new StringJoiner(":");
    Stream.of(
        artifact.getGroupId(),
        artifact.getArtifactId(),
        artifact.getVersion(),
        artifact.getType(),
        artifact.getClassifier()
    )
        .filter(Objects::nonNull)
        .forEach(stringJoiner::add);

    return stringJoiner + " -> " + artifact.getFile();
  }
}
