package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final ConcurrentHashMap<Long, Post> repository = new ConcurrentHashMap<>();
  private final AtomicLong count = new AtomicLong();

  public List<Post> all() {
    return new ArrayList<>(repository.values());
  }

  public Optional<Post> getById(long id) {
    Post post = repository.get(id);
    if (post == null) {
      return Optional.empty();
    }
    return Optional.of(post);
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(count.getAndIncrement());
      repository.put(post.getId(), post);
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
    repository.remove(id);
  }
}
