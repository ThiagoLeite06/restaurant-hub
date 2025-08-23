package br.com.restaurant_hub.restauranthub.usertype.application.service;

import br.com.restaurant_hub.restauranthub.exception.InvalidUserDataException;
import br.com.restaurant_hub.restauranthub.exception.UserNotFoundException;
import br.com.restaurant_hub.restauranthub.usertype.application.dto.*;
import br.com.restaurant_hub.restauranthub.usertype.domain.entity.UserType;
import br.com.restaurant_hub.restauranthub.usertype.infrastructure.repository.UserTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
public class UserTypeServiceImpl implements UserTypeService {
    
    private final UserTypeRepository userTypeRepository;
    
    public UserTypeServiceImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }
    
    @Override
    public UserType createUserType(CreateUserTypeRequest request) {
        if (userTypeRepository.existsByName(request.name())) {
            throw new InvalidUserDataException("Tipo de usuário já existente");
        }
        
        UserType userType = new UserType();
        userType.setName(request.name());
        userType.setDescription(request.description());
        
        return userTypeRepository.save(userType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserType> findById(Long id) {
        return userTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserType> findAll(Integer page, Integer pageSize, String orderBy) {
        PageRequest pageRequest = getPageRequest(page, pageSize, orderBy);
        return userTypeRepository.findAll(pageRequest);
    }

    @Override
    public Optional<UserType> updateById(Long id, UpdateUserTypeRequest request) {
        return userTypeRepository.findById(id)
                .map(userType -> {
                    updateFieldIfPresent(request.name(), userType::setName);
                    updateFieldIfPresent(request.description(), userType::setDescription);
                    return userTypeRepository.save(userType);
                });
    }
    
    @Override
    public Boolean deleteById(Long id) {
        Boolean exists = userTypeRepository.existsById(id);
        if (exists) {
            userTypeRepository.deleteById(id);
        }
        return exists;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(String name) {
        return userTypeRepository.existsByName(name);
    }
    
    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        Sort.Direction direction = "asc".equalsIgnoreCase(orderBy) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, pageSize, Sort.by(direction, "name"));
    }

    private void updateFieldIfPresent(String value, Consumer<String> setter) {
        if (StringUtils.hasText(value)) {
            setter.accept(value);
        }
    }
}