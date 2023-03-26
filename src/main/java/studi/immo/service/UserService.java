package studi.immo.service;

import studi.immo.entity.User;

import java.util.List;

public interface UserService {

    User saveUser (User user);

    User getCurrentUser ();

    List<User> getAllUser ();

    User getUserById (Long id);

    void deleteUserById (Long id);
}
