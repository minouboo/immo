package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.User;
import studi.immo.repository.UserRepository;
import studi.immo.service.UserService;

@Service
public class UserServiceImplement implements UserService {

    private UserRepository userRepository;

    public UserServiceImplement (UserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
