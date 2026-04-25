package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }


    @Transactional
    @Override
    public void saveOrUpdate(User user) {
        if (user.getId() != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
            User existing = userDao.getUserById(user.getId());
            user.setPassword(existing.getPassword());
        } else if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.saveOrUpdate(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}