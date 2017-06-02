package com.synchronous.service;

import com.synchronous.models.User;

import java.util.List;

public interface UserService {

    public User findByUserName(String userName);

    public User save(User user);

    public void delete(User user);

    public List<User> findAll();

    public User findOne(long id);
}
