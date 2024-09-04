package vk.loginsample.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vk.loginsample.models.User;
import vk.loginsample.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Integer insertUserService(String newUser, String newPassword) {
        String hashedpassword = passwordEncoder.encode(newPassword); // hash the password
        User user = new User(null, newUser, hashedpassword);
        Integer rowsupdated = userRepository.insertUser(user);
        return rowsupdated;
    }

    public Boolean validateUserService(String username, String password) {
        // Fetch the user from the database
        User userfound = userRepository.findUser(username);
        // Validate Password with passwordEncoder
        Boolean passwordValid = passwordEncoder.matches(password, userfound.getHashedPassword());
        return passwordValid;
    }

}
