package com.jd.controllers;

import com.jd.exception.ApiException;
import com.jd.models.User;
import com.jd.repository.UserRepository;
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
	private UserRepository userRepository;

	@RequestMapping(value = "/user/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User create(@RequestBody User user) throws ApiException {
		User newUser;
		try {
			newUser = userRepository.save(user);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to save user");
		}
		return newUser;
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody String delete(@PathVariable("id") long id) throws ApiException {
		try {
			User user = new User(id);
			userRepository.delete(user);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to delete user");
		}
		return "User succesfully deleted!";
	}

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody User getByUsername(@PathVariable("username") String username) throws ApiException {
		User user;
		try {
			user = userRepository.findByUsername(username);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to find user");
		}
		return user;
	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody User updateUser(@RequestBody User user) throws ApiException {
		User updatedUser;
		try {
			User foundUser = userRepository.findOne(user.getId());
			foundUser.setUsername(user.getUsername());
			foundUser.setPassword(user.getPassword());
			updatedUser = userRepository.save(foundUser);
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to save user");
		}
		return updatedUser;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<User>> getAllHeroes() throws ApiException {
		List<User> users;
		try {
			users = (List<User>) userRepository.findAll();
		} catch (Exception ex) {
			throw new ApiException("100", "Fail to find all users");
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

}