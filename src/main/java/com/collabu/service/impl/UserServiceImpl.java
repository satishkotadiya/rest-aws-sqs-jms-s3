package com.collabu.service.impl;

import com.collabu.model.User;
import com.collabu.payload.UserContactDto;
import com.collabu.repository.UserRepository;
import com.collabu.service.NotificationService;
import com.collabu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  public UserServiceImpl(UserRepository userRepository, NotificationService notificationService, ObjectMapper objectMapper) {
    this.userRepository = userRepository;
    this.notificationService = notificationService;
    this.objectMapper = objectMapper;
  }

  @Override
  public Optional<User> get(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> save(User user) {
    return Optional.ofNullable(
        userRepository.save(user)
    );
  }

  @Override
  public Optional<User> update(Long userId, User user) {
    Optional<User> optUser = userRepository.findById(userId);

    if (optUser.isPresent()) {
      User updateUser = optUser.get();
      if (StringUtils.isNotEmpty(user.getName())) {
        updateUser.setName(user.getName());
      }
      if (StringUtils.isNotEmpty(user.getUsername())) {
        updateUser.setUsername(user.getUsername());
      }
      if (StringUtils.isNotEmpty(user.getPassword())) {
        updateUser.setPassword(user.getPassword());
      }
      if (StringUtils.isNotEmpty(user.getEmail())) {
        updateUser.setEmail(user.getEmail());
      }
      if (StringUtils.isNotEmpty(user.getMobileNo())) {
        updateUser.setMobileNo(user.getMobileNo());
      }
      return Optional.ofNullable(userRepository.save(updateUser));
    }
    throw new RuntimeException("User not registered with Id : " + userId);
  }

  @Override
  public Optional<User> updateUserContact(Long userId, UserContactDto userContactDto) throws JsonProcessingException {
    Optional<User> optUser = userRepository.findById(userId);

    if (optUser.isPresent()) {
      User updateUser = optUser.get();
      if (StringUtils.isNotEmpty(userContactDto.getEmail())) {
        updateUser.setEmail(userContactDto.getEmail());
      }
      if (StringUtils.isNotEmpty(userContactDto.getMobileNo())) {
        updateUser.setMobileNo(userContactDto.getMobileNo());
      }

      // send message to aws sqs with updated user object
      notificationService.sendMessage(objectMapper.writeValueAsString(updateUser));

      return Optional.ofNullable(userRepository.save(updateUser));
    }
    throw new RuntimeException("User not registered with Id : " + userId);
  }

  @Override
  public void delete(Long userId) {
    if (get(userId).isPresent()) {
      userRepository.deleteById(userId);
    } else {
      throw new RuntimeException("User not registered with Id : " + userId);
    }
  }
}
