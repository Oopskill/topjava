package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dao = new MealDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        String id = request.getParameter("id");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime time = LocalDateTime.parse(request.getParameter("date"));

        if (id == null || id.isEmpty()){
            dao.addMeal(time,description,calories);
        }
        else {
            Meal updMeal = new Meal(time, description, calories);
            dao.updateMeal(Integer.parseInt(id),updMeal);
        }
        request.setAttribute("meals",dao.getAllMeals());
        response.sendRedirect("/meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        request.setAttribute("meals", dao.getAllMeals());
        request.setAttribute("dao", dao);

        String id = request.getParameter("id");
        String action = request.getParameter("action");

        if (request.getParameterMap().size() == 0){
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
        else {
            switch (action){
                case "addMeal":
                case "update":
                    request.getRequestDispatcher("/updMeal.jsp").forward(request, response);
                    break;
                case "delete":
                    dao.deleteMeal(Integer.parseInt(id));
                    response.sendRedirect("/meals");
                    break;
            }
        }
    }
}
