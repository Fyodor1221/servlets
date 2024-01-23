package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static PostController controller;
  private String method;
  private String path;
  private long id;


  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      path = req.getRequestURI();
      method = req.getMethod();

      controller.setResponse(resp);

      if (path.matches("/api/posts/\\d+")) {
        id = Long.parseLong(path.substring(path.lastIndexOf("/")));

        if (method.equals("GET")) {
          // easy way
          controller.getById(id);
          return;
        }

        if (method.equals("DELETE")) {
          // easy way
          controller.removeById(id);
          return;
        }
      }
      // primitive routing
      if (method.equals("GET") && path.equals("/api/posts")) {
        controller.all();
        return;
      }

      if (method.equals("POST") && path.equals("/api/posts")) {
        controller.save(req.getReader());
        return;
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

