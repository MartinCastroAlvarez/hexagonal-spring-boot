package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Integer>, UserRepository {
    @Override
    default Optional<User> get(Integer id) {
        return findById(id).map(UserMapper::toUser);
    }

    @Override
    default Optional<User> get(String email) {
        return findByEmail(email).map(UserMapper::toUser);
    }

    @Override
    default User save(User user) {
        return UserMapper.toUser(save(UserMapper.toUserEntity(user)));
    }

    @Override
    default Page<User> get(Pageable pageable) {
        return findAll(pageable).map(UserMapper::toUser);
    }

    @Override
    @Query("SELECT u FROM UserEntity u WHERE u.role = ?1")
    default Page<User> get(User.Role role, Pageable pageable) {
        return findByRole(role, pageable).map(UserMapper::toUser);
    }

    @Override
    @Query("SELECT u FROM UserEntity u WHERE u.name LIKE %?1% OR u.email LIKE %?1%")
    default Page<User> get(String query, Pageable pageable) {
        return findByQuery(query, pageable).map(UserMapper::toUser);
    }

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(?1)")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.role = ?1")
    Page<UserEntity> findByRole(User.Role role, Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.name LIKE %?1% OR u.email LIKE %?1%")
    Page<UserEntity> findByQuery(String query, Pageable pageable);
}
