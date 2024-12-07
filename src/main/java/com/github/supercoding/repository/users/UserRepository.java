package com.github.supercoding.repository.users;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findUserById(Integer userId);
}
