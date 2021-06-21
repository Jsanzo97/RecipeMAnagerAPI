package database.dao;

import io.ebean.Finder;
import models.User;
import operationResults.ErrorResult;
import operationResults.ErrorType;
import operationResults.OkResult;
import operationResults.OperationResult;
import play.i18n.Messages;

public class UserDao {

    private final Finder<Long, User> find = new Finder<>(User.class);

    public OperationResult logIn (String username, String password, Messages messages) {
        User userFound = findUserByUsername(username);

        if (userFound == null) {
            return new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION"));
        } else if (!userFound.getPassword().equals(password)) {
            return new ErrorResult(ErrorType.WRONG_PASSWORD, messages.at("WRONG_PASSWORD_ERROR_DESCRIPTION"));
        } else {
            return new OkResult(messages.at("OK_OPERATION_RESULT"));
        }
    }

    public OperationResult signUp(String username, String password, Messages messages) {
        User userFound = findUserByUsername(username);

        if (userFound == null) {
            User newUser = new User(username, password);
            newUser.save();
            return new OkResult(messages.at("OK_OPERATION_RESULT"));
        } else {
            return new ErrorResult(ErrorType.USER_WITH_USERNAME_ALREADY_EXISTS, messages.at("USERNAME_ALREADY_EXISTS_ERROR_DESCRIPTION"));
        }
    }

    public User findUserByUsername(String username) {
        return find
            .query()
            .where()
            .eq("username", username)
            .findOne();
    }
}
