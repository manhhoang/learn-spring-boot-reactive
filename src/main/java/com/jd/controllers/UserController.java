package com.jd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.models.User;
import com.jd.models.UserDao;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author Manh Hoang
 */
@Controller
public class UserController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * /create --> Create a new user and save it in the database.
	 * 
	 * @param username
	 *            User's username
	 * @param password
	 *            User's password
	 * @return A string describing if the user is succesfully created or not.
	 */
	@RequestMapping("/create-user")
	@ResponseBody
	public String create(String username, String password) {
		User user = null;
		try {
			user = new User(username, password);
			userDao.save(user);
		} catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "User succesfully created! (id = " + user.getId() + ")";
	}

	/**
	 * /delete --> Delete the user having the passed id.
	 * 
	 * @param id
	 *            The id of the user to delete
	 * @return A string describing if the user is succesfully deleted or not.
	 */
	@RequestMapping("/delete-user")
	@ResponseBody
	public String delete(long id) {
		try {
			User user = new User(id);
			userDao.delete(user);
		} catch (Exception ex) {
			return "Error deleting the user:" + ex.toString();
		}
		return "User succesfully deleted!";
	}

	/**
	 * /get-by-username --> Return the id for the user having the passed
	 * username.
	 * 
	 * @param username
	 *            The username to search in the database.
	 * @return The user id or a message error if the user is not found.
	 */
	@RequestMapping("/get-by-username")
	@ResponseBody
	public String getByUsernamel(String username) {
		String userId;
		try {
			User user = userDao.findByUsername(username);
			userId = String.valueOf(user.getId());
		} catch (Exception ex) {
			return "User not found";
		}
		return "The user id is: " + userId;
	}

	/**
	 * /update --> Update the username and the password for the user in the
	 * database having the passed id.
	 * 
	 * @param id
	 *            The id for the user to update.
	 * @param username
	 *            The new username.
	 * @param password
	 *            The new password.
	 * @return A string describing if the user is succesfully updated or not.
	 */
	@RequestMapping("/update-user")
	@ResponseBody
	public String updateUser(long id, String username, String password) {
		try {
			User user = userDao.findOne(id);
			user.setUsername(username);
			user.setPassword(password);
			userDao.save(user);
		} catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";
	}

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Autowired
	private UserDao userDao;

} // class UserController