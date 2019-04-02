package com.collabu.service;

import com.collabu.model.User;
import com.collabu.payload.UserContactDto;

import java.util.Optional;

public interface UserService {

    Optional<User> get(Long id);

    Optional<User> save(User user);

    Optional<User> update(Long userId, User user);

    void delete(Long userId);

    Optional<User> updateUserContact(Long userId, UserContactDto user);
}
