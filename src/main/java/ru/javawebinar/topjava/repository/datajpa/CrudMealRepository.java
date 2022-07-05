package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query(name = Meal.DELETE)         //because deleteBy in DataJpa at first make get (select) from database
    int deleteByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
    Meal getByIdAndUserId(int id, int userId);

//    Meal getByIdAndUser(int id, User user);

    @Query(name = Meal.ALL_SORTED)
    List<Meal> getAllByUserId(@Param("userId") int userId);

//    List<Meal> getAllByUser(User user, Sort sort);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> getBetweenHalfOpen(@Param("startDateTime") LocalDateTime start,
                                  @Param("endDateTime") LocalDateTime end, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m INNER JOIN FETCH m.user u WHERE m.id=:id AND m.user.id=:userId")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);
}
