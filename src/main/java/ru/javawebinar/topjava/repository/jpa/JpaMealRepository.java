package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        User userRef = em.getReference(User.class, userId);
        meal.setUser(userRef);
        if (meal.isNew()){
            em.persist(meal);
            return meal;
        } else {
            int mealId = (int) em.createQuery("select m.user.id from Meal m where m.id=:id")
                    .setParameter("id", meal.getId())
                    .getSingleResult();
            if (mealId == userId){
                return em.merge(meal);
            }
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createQuery("delete from Meal m where m.id=:id and m.user.id=:userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        try {
                return (Meal) em.createQuery("select m from Meal m where m.id=:id and m.user.id=:userId")
                        .setParameter("id", id)
                        .setParameter("userId", userId)
                        .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("select m from Meal m where m.user.id=:userId order by m.dateTime desc ")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("select m from Meal m where m.user.id=:userId" +
                " and m.dateTime >=:startDateTime and m.dateTime <:endDateTime" +
                " order by m.dateTime desc ")
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}