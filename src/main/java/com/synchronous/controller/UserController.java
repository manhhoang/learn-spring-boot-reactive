package com.synchronous.controller;

import com.synchronous.exception.ApiException;
import com.synchronous.model.User;
import com.synchronous.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User create(@RequestBody User user) throws ApiException {
		User newUser;
		try {
			newUser = userService.save(user);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to save user");
		}
		return newUser;
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody String delete(@PathVariable("id") long id) throws ApiException {
		try {
			User user = new User(id);
			userService.delete(user);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to delete user");
		}
		return "User succesfully deleted!";
	}

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody User getByUsername(@PathVariable("username") String username) throws ApiException {
		User user;
		try {
			user = userService.findByUserName(username);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to find user");
		}
		return user;
	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody User updateUser(@RequestBody User user) throws ApiException {
		User updatedUser;
		try {
			User foundUser = userService.findOne(user.getId());
			foundUser.setUsername(user.getUsername());
			foundUser.setPassword(user.getPassword());
			updatedUser = userService.save(foundUser);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to save user");
		}
		return updatedUser;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<User>> getAllHeroes() throws ApiException {
		List<User> users;
		try {
			users = userService.findAll();
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to find all users");
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

}