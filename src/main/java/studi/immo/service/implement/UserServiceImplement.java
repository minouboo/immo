package studi.immo.service.implement;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import studi.immo.entity.User;
import studi.immo.repository.UserRepository;
import studi.immo.service.UserService;

import java.util.List;

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

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (! (authentication instanceof AnonymousAuthenticationToken)) {
            return this.userRepository.findByUserName(authentication.getName()).orElse(null);
        }
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
