package br.com.restaurant_hub.restauranthub.usertype.application.usecase;

import br.com.restaurant_hub.restauranthub.usertype.application.dto.UserTypeResponse;
import br.com.restaurant_hub.restauranthub.usertype.application.service.UserTypeService;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FindAllUserTypesUseCase {
    
    private final UserTypeService userTypeService;
    
    public FindAllUserTypesUseCase(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }
    
    public Page<UserTypeResponse> execute(Integer page, Integer pageSize, String orderBy) {
        Page<UserType> userTypes = userTypeService.findAll(page, pageSize, orderBy);
        return userTypes.map(this::mapToResponse);
    }
    
    private UserTypeResponse mapToResponse(UserType userType) {
        return new UserTypeResponse(
                userType.getId().toString(),
                userType.getName(),
                userType.getDescription()
        );
    }
}