package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import com.martincastroalvarez.hex.hex.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserApplication extends HexagonalApplication implements UserService {
    @Autowired
    private UserRepository userRepository;

    private final List<String> USER_SORT_KEYS = Arrays.asList("id", "name", "email", "role");

    @Override
    public User getUser(Integer userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting user. UserId: %d", userId));
        return userRepository.get(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> listUsers(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing users with query. Query: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", query, page, size, sort, asc));
        return userRepository.get(query, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listUsers(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all users. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return userRepository.get(getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public User updateUser(Integer userId, String name) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Updating user name. UserId: %d, Name: %s", userId, name));
        if (name == null || name.length() < 3 || name.length() > 50)
            throw new InvalidNameException();
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        user.setName(name);
        return userRepository.save(user);
    }

    @Override
    public User deactivateUser(int userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Deactivating user. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        user.setIsActive(false);
        return userRepository.save(user);
    }

    @Override
    public User activateUser(int userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Activating user. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> listManagers(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing managers with query. Query: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", query, page, size, sort, asc));
        return userRepository.get(User.Role.MANAGER, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listManagers(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all managers. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return userRepository.get(User.Role.MANAGER, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listProviders(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing providers with query. Query: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", query, page, size, sort, asc));
        return userRepository.get(User.Role.PROVIDER, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listProviders(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all providers. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return userRepository.get(User.Role.PROVIDER, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listSalesmen(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing salesmen with query. Query: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", query, page, size, sort, asc));
        return userRepository.get(User.Role.SALESMAN, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public List<User> listSalesmen(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all salesmen. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return userRepository.get(User.Role.SALESMAN, getPageRequest(page, size, sort, asc, USER_SORT_KEYS)).getContent();
    }

    @Override
    public User promoteManager(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Promoting user to manager. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() == User.Role.MANAGER)
            throw new InvalidRoleException();
        user.setRole(User.Role.MANAGER);
        return userRepository.save(user);
    }
    
    @Override
    public User demoteManager(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Demoting manager. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != User.Role.MANAGER)
            throw new InvalidRoleException();
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }
    
    @Override
    public User promoteSalesman(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Promoting user to salesman. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() == User.Role.SALESMAN)
            throw new InvalidRoleException();
        user.setRole(User.Role.SALESMAN);
        return userRepository.save(user);
    }
    
    @Override
    public User demoteSalesman(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Demoting salesman. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != User.Role.SALESMAN)
            throw new InvalidRoleException();
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }
    
    @Override
    public User promoteProvider(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Promoting user to provider. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() == User.Role.PROVIDER)
            throw new InvalidRoleException();
        user.setRole(User.Role.PROVIDER);
        return userRepository.save(user);
    }
    
    @Override
    public User demoteProvider(Integer userId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Demoting provider. UserId: %d", userId));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != User.Role.PROVIDER)
            throw new InvalidRoleException();
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }
}
