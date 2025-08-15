package br.com.restaurant_hub.restauranthub.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String field, String value) {
        super("Usuário com " + field + " '" + value + "' já existe");
    }
}
