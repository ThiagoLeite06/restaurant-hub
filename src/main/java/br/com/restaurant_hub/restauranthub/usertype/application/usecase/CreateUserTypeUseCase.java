package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.usertype.application.dto.CreateUserTypeRequest;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserTypeUseCase {
    
    private final UserTypeService userTypeService;
    
    public CreateUserTypeUseCase(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }
    
    public UserTypeResponse execute(CreateUserTypeRequest request) {
        UserType userType = userTypeService.createUserType(request);
        return mapToResponse(userType);
    }
    
    private UserTypeResponse mapToResponse(UserType userType) {
        return new UserTypeResponse(
                userType.getId().toString(),
                userType.getName(),
                userType.getDescription()
        );
    }
}