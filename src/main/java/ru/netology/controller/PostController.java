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
  private HttpServletResponse response = null;
  private static final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all() throws IOException {
    write(service.all());
  }

  public void getById(long id) throws IOException {
    //deserialize request & serialize response
    write(service.getById(id));
  }

  public void save(Reader body) throws IOException {
    final var post = gson.fromJson(body, Post.class);
    write(service.save(post));
  }

  public void removeById(long id) throws IOException {
    //deserialize request & serialize response
    service.removeById(id);
    write("Post " + id + " has been removed");
  }

  public void setResponse (HttpServletResponse response) {
    this.response = response;
    response.setContentType(APPLICATION_JSON);
  }

  private void write (Object data) throws IOException {
    response.getWriter().print(gson.toJson(data));
  }
}
