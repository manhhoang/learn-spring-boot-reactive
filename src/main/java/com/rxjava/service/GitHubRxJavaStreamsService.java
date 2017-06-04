package com.rxjava.service;

import com.completablefuture.model.GitHubRepo;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class GitHubRxJavaStreamsService {

    private final WebTarget target = ClientBuilder.newClient().target("https://api.github.com/");

    public void findRepoByUser(String user) {
        Observable<GitHubRepo> gitHubRepoObservable = Observable.create(emitter -> {
            try {
                Future<List<GitHubRepo>> gitHubRepos = reposAsync(user);
                for (GitHubRepo gitHubRepo : gitHubRepos.get()) {
                    emitter.onNext(gitHubRepo);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        Disposable disposable = gitHubRepoObservable.subscribe(gitHubRepo -> System.out.println(gitHubRepo.getName()));
        disposable.dispose();
    }

    public Future<List<GitHubRepo>> reposAsync(String user) {
        return target
                .path("users/{user}/repos")
                .resolveTemplate("user", user)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .async()
                .get(new GenericType<List<GitHubRepo>>() { });
    }

    public static void main(String[] args) {
        GitHubRxJavaStreamsService gitHubRxJavaService = new GitHubRxJavaStreamsService();
        gitHubRxJavaService.findRepoByUser("test");
    }
}
