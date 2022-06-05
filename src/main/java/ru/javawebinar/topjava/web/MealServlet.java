package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CrudRepository;
import ru.javawebinar.topjava.dao.InMemoryMealsRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String ADD_OR_EDIT = "mealForm.jsp";
    private static final String LIST_MEALS = "meals.jsp";
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private CrudRepository<Meal> repository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.repository = new InMemoryMealsRepository();
    }

    private List<MealTo> getMealsTo() {
        return MealsUtil.filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, User.CALORIES_PER_DAY);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        switch (action == null ? "" : action) {
            case "delete":
                log.debug("delete Meal");
                repository.delete(getId(request));
                log.debug("redirect to meals");
                response.sendRedirect("/topJava/meals");
                return;
            case "edit":
                log.debug("forward to mealForm");
                forward = ADD_OR_EDIT;
                Meal meal = repository.getById(getId(request));
                request.setAttribute("meal", meal);
                break;
            case "add":
                log.debug("forward to mealForm");
                forward = ADD_OR_EDIT;
                request.setAttribute("meal", new Meal(null, "", 0));
                break;
            default:
                log.debug("forward to meals");
                forward = LIST_MEALS;
                request.setAttribute("dateFormatter", dateTimeFormatter);
                request.setAttribute("meals", getMealsTo());
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Integer id = getId(request);
        Meal meal = new Meal(localDateTime, description, calories);
        if (id == null) {
            log.debug("add new Meal");
            repository.add(meal);
        } else {
            log.debug("update Meal");
            meal.setId(id);
            repository.update(meal);
        }
        log.debug("redirect to meals");
        response.sendRedirect("/topJava/meals");
    }

    private Integer getId(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id.isEmpty()) return null;
        return Integer.parseInt(id);
    }
}
