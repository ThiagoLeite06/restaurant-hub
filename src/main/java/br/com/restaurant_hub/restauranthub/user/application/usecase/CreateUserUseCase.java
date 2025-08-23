package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.CreateUserRequest;
import br.com.restaurant_hub.restauranthub.user.application.dto.UserResponse;
import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import br.com.restaurant_hub.restauranthub.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserUseCase {
    
    private final UserService userService;
    
    public CreateUserUseCase(UserService userService) {
        this.userService = userService;
    }
    
    public UserResponse execute(CreateUserRequest request) {
        // Criar usuário através do Service
        User user = userService.createUser(request);
        
        // Aqui poderia ter lógicas adicionais como:
        // - Envio de email de boas-vindas
        // - Log de auditoria
        // - Notificações
        
        // Converter para Response
        return mapToResponse(user);
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