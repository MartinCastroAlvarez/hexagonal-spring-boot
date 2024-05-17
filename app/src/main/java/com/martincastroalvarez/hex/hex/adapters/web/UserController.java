package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.adapters.dto.UserDTO;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends HexagonalController {
    @Autowired
    private UserService userService;

    @GetMapping("/auth/profile")
    public UserDTO getProfile() {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting profile of user with username=%s", user.getUsername()));
        return UserMapper.toUserDTO(user);
    }

    @GetMapping("/users/{userId}")
    public UserDTO getUser(@PathVariable Integer userId) {
        logger.info(String.format("Getting user with id=%s", userId));
        User user = userService.getUser(userId);
        logger.info(String.format("User found: %s", user));
        return UserMapper.toUserDTO(user);
    }

    @GetMapping("/users")
    public List<UserDTO> listUsers(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc,
        @RequestParam(required = false) String query
    ) {
        logger.info(String.format("Listing users with page=%s, size=%s, sort=%s, asc=%s, query=%s", page, size, sort, asc, query));
        List<User> users;
        if (query != null && !query.isEmpty())
            users = userService.listUsers(query, page, size, sort, asc);
        else
            users = userService.listUsers(page, size, sort, asc);
        logger.info(String.format("Found %s users", users.size()));
        return users.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/managers")
    public List<UserDTO> listManagers(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc,
        @RequestParam(required = false) String query
    ) {
        logger.info(String.format("Listing managers with page=%s, size=%s, sort=%s, asc=%s, query=%s", page, size, sort, asc, query));
        List<User> managers = query != null ? userService.listManagers(query, page, size, sort, asc) : userService.listManagers(page, size, sort, asc);
        logger.info(String.format("Found %s managers", managers.size()));
        return managers.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/salesmen")
    public List<UserDTO> listSalesmen(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc,
        @RequestParam(required = false) String query
    ) {
        logger.info(String.format("Listing salesmen with page=%s, size=%s, sort=%s, asc=%s, query=%s", page, size, sort, asc, query));
        List<User> salesmen = query != null ? userService.listSalesmen(query, page, size, sort, asc) : userService.listSalesmen(page, size, sort, asc);
        logger.info(String.format("Found %s salesmen", salesmen.size()));
        return salesmen.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/providers")
    public List<UserDTO> listProviders(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc,
        @RequestParam(required = false) String query
    ) {
        logger.info(String.format("Listing providers with page=%s, size=%s, sort=%s, asc=%s, query=%s", page, size, sort, asc, query));
        List<User> providers = query != null ? userService.listProviders(query, page, size, sort, asc) : userService.listProviders(page, size, sort, asc);
        logger.info(String.format("Found %s providers", providers.size()));
        return providers.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @RequestBody UserRequest request) {
        logger.info(String.format("Updating user with id=%s", userId));
        User user = userService.updateUser(userId, request.getName());
        logger.info(String.format("User updated: %s", user));
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @PostMapping("/users/{userId}/status")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Integer userId) {
        logger.info(String.format("Activating user with id=%s", userId));
        User user = userService.activateUser(userId);
        logger.info(String.format("User activated: %s", user));
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @DeleteMapping("/users/{userId}/status")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer userId) {
        logger.info(String.format("Deactivating user with id=%s", userId));
        userService.deactivateUser(userId);
        logger.info(String.format("User deactivated: %s", userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/managers/{userId}")
    public ResponseEntity<UserDTO> promoteManager(@PathVariable Integer userId) {
        logger.info(String.format("Promoting user with id=%s to manager", userId));
        User user = userService.promoteManager(userId);
        logger.info(String.format("User promoted: %s", user));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDTO(user));
    }

    @DeleteMapping("/managers/{userId}")
    public ResponseEntity<Void> demoteManager(@PathVariable Integer userId) {
        logger.info(String.format("Demoting user with id=%s from manager", userId));
        userService.demoteManager(userId);
        logger.info(String.format("User demoted: %s", userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/salesmen/{userId}")
    public ResponseEntity<UserDTO> promoteSalesman(@PathVariable Integer userId) {
        logger.info(String.format("Promoting user with id=%s to salesman", userId));
        User user = userService.promoteSalesman(userId);
        logger.info(String.format("User promoted: %s", user));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDTO(user));
    }

    @DeleteMapping("/salesmen/{userId}")
    public ResponseEntity<Void> demoteSalesman(@PathVariable Integer userId) {
        logger.info(String.format("Demoting user with id=%s from salesman", userId));
        userService.demoteSalesman(userId);
        logger.info(String.format("User demoted: %s", userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/providers/{userId}")
    public ResponseEntity<UserDTO> promoteProvider(@PathVariable Integer userId) {
        logger.info(String.format("Promoting user with id=%s to provider", userId));
        User user = userService.promoteProvider(userId);
        logger.info(String.format("User promoted: %s", user));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDTO(user));
    }

    @DeleteMapping("/providers/{userId}")
    public ResponseEntity<Void> demoteProvider(@PathVariable Integer userId) {
        logger.info(String.format("Demoting user with id=%s from provider", userId));
        userService.demoteProvider(userId);
        logger.info(String.format("User demoted: %s", userId));
        return ResponseEntity.noContent().build();
    }
}

class UserRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
