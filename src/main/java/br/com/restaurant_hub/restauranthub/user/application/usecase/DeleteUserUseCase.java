package br.com.restaurant_hub.restauranthub.user.application.usecase;

import br.com.restaurant_hub.restauranthub.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteUserUseCase {
    
    private final UserService userService;
    
    public DeleteUserUseCase(UserService userService) {
        this.userService = userService;
    }
    
    public Boolean execute(UUID userId) {
        // Validações específicas de negócio antes de deletar:
        // - Verificar se usuário tem pedidos pendentes
        // - Verificar se é dono de restaurante ativo
        // - Outros checks de integridade referencial
        
        Boolean deleted = userService.deleteById(userId);
        
        if (deleted) {
            // Lógicas pós exclusão:
            // - Log de auditoria
            // - Cleanup de dados relacionados
            // - Notificação de conta removida
            // - Invalidar tokens/sessões
        }
        
        return deleted;
    }
}