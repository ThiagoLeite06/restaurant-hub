package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.UpdateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UpdateUserUseCase {
    
    private final UserService userService;
    
    public UpdateUserUseCase(UserService userService) {
        this.userService = userService;
    }
    
    public Optional<UserResponse> execute(UUID userId, UpdateUserRequest request) {
        // Atualizar através do Service
        Optional<User> updatedUser = userService.updateById(userId, request);
        
        // Aqui poderia ter lógicas adicionais como:
        // - Log de auditoria da alteração
        // - Notificação de perfil atualizado
        // - Validações específicas de negócio
        
        return updatedUser.map(this::mapToResponse);
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