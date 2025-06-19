package com.scm.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepository;
import com.scm.services.UserService;

@Service
public class UserServicesImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // auto generate id
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // user.setProfilePic("default URL");

        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User updatedUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setEmailVerified(user.isEmailVerified());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setAbout(user.getAbout());
        updatedUser.setProvider(user.getProvider());
        updatedUser.setProviderUserId(user.getProviderUserId());
        updatedUser.setEnabled(user.isEnabled());
        updatedUser.setPhoneNumber(user.getPhoneNumber());
        updatedUser.setPhoneVerified(user.isPhoneVerified());
        updatedUser.setProfilePic(user.getProfilePic());

        User save = userRepository.save(updatedUser);

        return Optional.ofNullable(save);

    }

    @Override
    public void deleteUser(String id) {
        User retreivedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepository.delete(retreivedUser);
    }

    @Override
    public boolean isUserExist(String id) {
        User retreivedUser = userRepository.findById(id).orElse(null);

        return (retreivedUser != null) ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
