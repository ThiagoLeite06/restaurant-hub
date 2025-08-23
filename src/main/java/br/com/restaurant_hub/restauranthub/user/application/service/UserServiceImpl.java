package br.com.restaurant_hub.restauranthub.user.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.user.application.dto.*;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTypeService userTypeService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserTypeService userTypeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userTypeService = userTypeService;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new InvalidUserDataException("Email já está em uso");
        }
        
        if (userRepository.existsByLogin(request.login())) {
            throw new InvalidUserDataException("Login já está em uso");
        }
        
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setAddress(request.address());
        
        if (StringUtils.hasText(request.userTypeId())) {
            Long userTypeId = Long.parseLong(request.userTypeId());
            UserType userType = userTypeService.findById(userTypeId)
                    .orElseThrow(() -> new InvalidUserDataException("Tipo de usuário não encontrado"));
            user.setUserType(userType);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Integer page, Integer pageSize, String orderBy) {
        PageRequest pageRequest = getPageRequest(page, pageSize, orderBy);
        return userRepository.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = Sort.Direction.DESC;
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        return PageRequest.of(page, pageSize, direction, "createdAt");
    }

    @Override
    public Boolean deleteById(UUID id) {
        Boolean exists = userRepository.existsById(id);
        if (exists) {
            userRepository.deleteById(id);
        }
        return exists;
    }

    @Override
    public Optional<User> updateById(UUID id, UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    updateFieldIfPresent(request.name(), user::setName);
                    updateFieldIfPresent(request.address(), user::setAddress);
                    return userRepository.save(user);
                });
    }

    private void updateFieldIfPresent(String value, Consumer<String> setter) {
        if (StringUtils.hasText(value)) {
            setter.accept(value);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByLogin(String login) {
        if (!StringUtils.hasText(login)) {
            throw new InvalidUserDataException("Login não pode ser vazio");
        }

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("login", login));

        return mapToResponse(user);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        if (userId == null) {
            throw new InvalidUserDataException("ID do usuário não pode ser nulo");
        }

        if (!StringUtils.hasText(currentPassword)) {
            throw new InvalidUserDataException("Senha atual é obrigatória");
        }

        if (!StringUtils.hasText(newPassword)) {
            throw new InvalidUserDataException("Nova senha é obrigatória");
        }

        User user = findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id", userId.toString()));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidUserDataException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getAddress(),
                user.getUserType() != null ? user.getUserType().getId().toString() : null,
                user.getUserType() != null ? user.getUserType().getName() : null,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isEnabled()
        );
    }
}