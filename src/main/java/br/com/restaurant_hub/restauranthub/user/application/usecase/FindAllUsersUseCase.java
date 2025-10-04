package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import br.com.restaurant_hub.restauranthub.user.infrastructure.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class FindAllUsersUseCase {
    
    private final UserRepository userRepository;

    public FindAllUsersUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Page<UserResponse> execute(Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = "asc".equalsIgnoreCase(orderBy) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, "createdAt"));
        return userRepository.findAll(pageable).map(this::mapToResponse);
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