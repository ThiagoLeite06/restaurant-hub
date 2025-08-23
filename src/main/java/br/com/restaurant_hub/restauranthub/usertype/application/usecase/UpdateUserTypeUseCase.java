package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.usertype.application.dto.UpdateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UpdateUserTypeUseCase {
    
    private final UserTypeService userTypeService;
    
    public UpdateUserTypeUseCase(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }
    
    public Optional<UserTypeResponse> execute(Long id, UpdateUserTypeRequest request) {
        Optional<UserType> updatedUserType = userTypeService.updateById(id, request);
        
        // Lógicas adicionais do caso de uso:
        // - Log de auditoria da alteração
        // - Notificação de alteração
        // - Validações de negócio específicas
        
        return updatedUserType.map(this::mapToResponse);
    }
    
    private UserTypeResponse mapToResponse(UserType userType) {
        return new UserTypeResponse(
                userType.getId().toString(),
                userType.getName(),
                userType.getDescription()
        );
    }
}