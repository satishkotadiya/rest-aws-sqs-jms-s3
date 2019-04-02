package com.collabu.resource;

import com.collabu.utils.ErrorHandler;
import com.collabu.model.User;
import com.collabu.payload.UserContactDto;
import com.collabu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/uesrs")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        Optional<User> user = userService.get(userId);
        if (user.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(user.get());
        }
        log.info("User with id {} not found from database.", userId);
        return new ResponseEntity<>(new ErrorHandler("User not found with id : " + userId),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@RequestBody User user,
                                           UriComponentsBuilder componentsBuilder) {
        Optional<User> save = userService.save(user);
        if (save.isPresent()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(componentsBuilder.path("/api/v1/users/{id}")
                    .buildAndExpand(save.get().getId())
                    .toUri());
            return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                           @RequestBody User user) {
        try {
            Optional<User> update = userService.update(userId, user);
            if (update.isPresent()) {
                return ResponseEntity
                        .ok()
                        .body(update.get());
            }
        } catch (RuntimeException e) {
            log.error("Unable to update. User with id {} not found.", userId);
            return new ResponseEntity(new ErrorHandler("Unable to update. User with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        try {
            log.info("Fetching & Deleting User with id {}", userId);
            userService.delete(userId);
        } catch (RuntimeException e) {
            log.error("Unable to delete. User with id {} not found.", userId);
            return new ResponseEntity(new ErrorHandler("Unable to delete. User with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> patchUser(@PathVariable Long userId,
                                       @RequestBody UserContactDto userContactDto) throws JsonProcessingException {
        Optional<User> optUser = userService.updateUserContact(userId, userContactDto);
        return ResponseEntity
                .ok()
                .body(optUser.get());
    }

    @GetMapping("/{userId}/contact")
    public ResponseEntity<?> getUserContact(@PathVariable Long userId) {
        Optional<User> user = userService.get(userId);
        return ResponseEntity.ok().body("");
    }
}