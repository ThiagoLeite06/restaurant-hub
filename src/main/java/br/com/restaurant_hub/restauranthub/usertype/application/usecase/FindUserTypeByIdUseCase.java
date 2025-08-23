package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindUserTypeByIdUseCase {
    
    private final UserTypeService userTypeService;
    
    public FindUserTypeByIdUseCase(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }
    
    public Optional<UserTypeResponse> execute(Long id) {
        return userTypeService.findById(id)
                .map(this::mapToResponse);
    }
    
    private UserTypeResponse mapToResponse(UserType userType) {
        return new UserTypeResponse(
                userType.getId().toString(),
                userType.getName(),
                userType.getDescription()
        );
    }
}