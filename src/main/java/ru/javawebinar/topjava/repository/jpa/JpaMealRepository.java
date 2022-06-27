package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(ref);
            em.persist(meal);
        } else {
/*            if (get(meal.getId(), userId) == null) {      // (em.find(Meal.class, meal.getId()).getUser().getId() != userId)
                                                            // без именнованого запроса, проверка на то, кому принадлежит еда это дополнительный select из базы
                return null;
            } else {
                meal.setUser(ref);
                em.merge(meal);
            }
*/
            if (em.createNamedQuery(Meal.UPDATE).setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories()).setParameter("dateTime", meal.getDateTime())
                    .setParameter("id", meal.getId()).setParameter("userId", userId).executeUpdate() == 0) return null;
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id).setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
//        Meal meal = em.find(Meal.class, id);                          //какой вариант лучше использовать?
//        if (meal!=null && meal.getUser().getId()==userId) return meal;
//        else return null;
        return DataAccessUtils.singleResult(em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("id", id).setParameter("userId", userId)
                .getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL, Meal.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN, Meal.class).setParameter("userId", userId)
                .setParameter("start", startDateTime).setParameter("end", endDateTime).getResultList();
    }
}