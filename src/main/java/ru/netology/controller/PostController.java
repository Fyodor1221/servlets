package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
  private static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private static final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    write(service.all(), response);
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    //deserialize request & serialize response
    write(service.getById(id), response);
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    final var post = gson.fromJson(body, Post.class);
    write(service.save(post), response);
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    //deserialize request & serialize response
    service.removeById(id);
    write("Post " + id + " has been removed", response);
  }

  private void write (Object data, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gson.toJson(data));
  }
}
