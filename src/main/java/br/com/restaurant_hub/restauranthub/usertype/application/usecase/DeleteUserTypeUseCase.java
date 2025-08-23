package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteUserTypeUseCase {
    
    private final UserTypeService userTypeService;
    
    public DeleteUserTypeUseCase(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }
    
    public Boolean execute(Long id) {
        Boolean deleted = userTypeService.deleteById(id);
        
        if (deleted) {

        }
        
        return deleted;
    }
}