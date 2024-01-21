package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PostRepository {
  private final ConcurrentHashMap.KeySetView<Post,Boolean> repository = ConcurrentHashMap.newKeySet();
  private long count;

  public List<Post> all() {
    return new ArrayList<>(repository);
  }

  public Optional<Post> getById(long id) {
    for (Post post : repository) {
      if (post.getId() == id) {
        return Optional.of(post);
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(count + 1);
      repository.add(post);
      count++;
    }
    else {
      var recentPost = getById(post.getId());
      if (recentPost.isPresent()) {
        recentPost.get().setContent(post.getContent());
      }
      else {
        post.setId(0);
        this.save(post);
      }
    }
    return post;
  }

  public void removeById(long id) {
    repository.removeIf(post -> post.getId() == id);
  }
}
