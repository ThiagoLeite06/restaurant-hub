package br.com.restaurant_hub.restauranthub.service.Impl;

import br.com.restaurant_hub.restauranthub.controller.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.controller.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.entity.UserEntity;
import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.controller.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.repository.UserRepository;
import br.com.restaurant_hub.restauranthub.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(CreateUserRequest dto) {
        var entity = new UserEntity();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        entity.setAddress(dto.address());
        entity.setLogin(dto.login());
        entity.setUserType(dto.userType());

        return userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Integer page, Integer pageSize, String orderBy) {
        var pageRequest = getPageRequest(page, pageSize, orderBy);

        return userRepository.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        var direction = Sort.Direction.DESC;
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        return PageRequest.of(page, pageSize, direction, "createdAt");
    }

    @Override
    public boolean deleteById(Long id) {
        var exists = userRepository.existsById(id);

        if (exists) {
            userRepository.deleteById(id);
        }

        return false;
    }

    @Override
    public Optional<UserEntity> updateById(Long id, UpdateUserRequest dto) {
        return userRepository.findById(id)
                .map(user -> {
                    updateFieldIfPresent(dto.name(), user::setName);
                    updateFieldIfPresent(dto.email(), user::setEmail);
                    updateFieldIfPresent(dto.address(), user::setAddress);
                    return userRepository.save(user);
                });
    }

    private void updateFieldIfPresent(String value, Consumer<String> setter) {
        if (StringUtils.hasText(value)) {
            setter.accept(value);
        }
    }

    @Override
    public UserResponse findByLogin(String login) {
        if (!StringUtils.hasText(login)) {
            throw new InvalidUserDataException("Login não pode ser vazio");
        }

        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("login", login));

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getAddress());
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        if (userId == null) {
            throw new InvalidUserDataException("ID do usuário não pode ser nulo");
        }

        if (!StringUtils.hasText(currentPassword)) {
            throw new InvalidUserDataException("Senha atual é obrigatória");
        }

        if (!StringUtils.hasText(newPassword)) {
            throw new InvalidUserDataException("Nova senha é obrigatória");
        }

        UserEntity user = findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id", userId.toString()));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidUserDataException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}
