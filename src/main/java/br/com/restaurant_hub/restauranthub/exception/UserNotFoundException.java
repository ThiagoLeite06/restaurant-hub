package br.com.restaurant_hub.restauranthub.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Long id) {
        super("Usuário com ID " + id + " não encontrado");
    }
    
    public UserNotFoundException(String field, String value) {
        super("Usuário com " + field + " '" + value + "' não encontrado");
    }
}
