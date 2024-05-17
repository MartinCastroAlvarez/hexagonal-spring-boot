package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.UserDTO;
import com.martincastroalvarez.hex.hex.domain.models.User;

public class UserMapper {
    public static User toUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setPasswordHash(userEntity.getPasswordHash());
        user.setLastLoginDate(userEntity.getLastLoginDate());
        user.setSignUpDate(userEntity.getSignUpDate());
        user.setIsActive(userEntity.getIsActive());
        if (userEntity.getRole() == null)
            user.setRole(User.Role.USER);
        else
            user.setRole(userEntity.getRole());
        return user;
    }

    public static UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setLastLoginDate(user.getLastLoginDate());
        userEntity.setSignUpDate(user.getSignUpDate());
        userEntity.setIsActive(user.getIsActive());
        userEntity.setRole(user.getRole());
        return userEntity;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setLastLoginDate(user.getLastLoginDate());
        userDTO.setSignUpDate(user.getSignUpDate());
        userDTO.setIsActive(user.getIsActive());
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }
}
