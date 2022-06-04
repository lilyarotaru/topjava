package ru.javawebinar.topjava.web;


import org.slf4j.Logger;
import ru.javawebinar.topjava.DAO.InMemoryRepository;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final InMemoryRepository repository = new InMemoryRepository();

    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "meal-form.jsp";
    private static final String LIST_MEALS = "meals.jsp";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            log.debug("forward to meals");
            forward = LIST_MEALS;
            request.setAttribute("meals", repository.getAll());
        } else if (action.equalsIgnoreCase("delete")) {
            log.debug("delete Meal");
            int mealId = Integer.parseInt(request.getParameter("id"));
            repository.delete(mealId);
            forward = LIST_MEALS;
            request.setAttribute("meals", repository.getAll());
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("forward to meal-form");
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = repository.getById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("add")) {
            log.debug("forward to meal-form");
            forward = INSERT_OR_EDIT;
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime ltd = LocalDateTime.parse(request.getParameter("dateTime"), dtf);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        int id = Integer.parseInt(request.getParameter("id"));
        if (id == -1) {
            log.debug("add new Meal to Repository");
            Meal meal = new Meal(ltd, description, calories);
            repository.add(meal);
        } else {
            log.debug("update Meal");
            Meal meal = new Meal(ltd, description, calories);
            repository.update(id, meal);
        }
        log.debug("forward to meals");
        request.setAttribute("meals", repository.getAll());
        request.getRequestDispatcher(LIST_MEALS).forward(request, response);
    }
}
