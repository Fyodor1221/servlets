package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static String method;
  private static String path;
  private static long id;
  private static AnnotationConfigApplicationContext context;
  private static PostController controller;
  private static PostService service;
  private static PostRepository repository;


  @Override
  public void init() {
    context = new AnnotationConfigApplicationContext("ru.netology");
    controller = context.getBean(PostController.class);
    service = context.getBean(PostService.class);
    repository = context.getBean(PostRepository.class);
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

