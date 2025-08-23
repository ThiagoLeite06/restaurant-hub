package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.dto.ChangePasswordRequest;
import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ChangePasswordUseCase {
    
    private final UserService userService;
    
    public ChangePasswordUseCase(UserService userService) {
        this.userService = userService;
    }
    
    public void execute(UUID userId, ChangePasswordRequest request) {
        // Validações específicas de negócio poderiam ir aqui
        // Por exemplo: validar complexidade da senha, histórico de senhas, etc.
        
        userService.changePassword(
                userId,
                request.currentPassword(),
                request.newPassword()
        );
        
        // Lógicas adicionais pós alteração de senha:
        // - Log de auditoria de segurança
        // - Invalidar todas as sessões ativas
        // - Enviar notificação por email
        // - Alert de segurança se necessário
    }
}