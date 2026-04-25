package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class)
                .getResultList();
    }

    @Override
    public User getUserById(Long id) {
        List<User> result = entityManager.createQuery(
                        "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void saveOrUpdate(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else if (user.getId() != null) {
            entityManager.merge(user);
        }
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public User findByUsername(String username) {
        List<User> result = entityManager.createQuery(
                        "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username",
                        User.class)
                .setParameter("username", username)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}