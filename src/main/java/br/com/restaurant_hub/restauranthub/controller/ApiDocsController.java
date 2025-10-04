package br.com.restaurant_hub.restauranthub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiDocsController {

    @GetMapping("/api-docs")
    public ResponseEntity<Map<String, Object>> getApiDocs() {
        Map<String, Object> apiDocs = new HashMap<>();
        
        // Informações básicas da API
        Map<String, Object> info = new HashMap<>();
        info.put("title", "RestaurantHub API");
        info.put("version", "1.0.0");
        info.put("description", "Sistema de Gestão Compartilhada para Restaurantes");
        
        // Endpoints principais
        Map<String, Object> paths = new HashMap<>();
        
        // Auth endpoints
        Map<String, Object> authLogin = new HashMap<>();
        authLogin.put("summary", "Fazer login");
        authLogin.put("description", "Autentica um usuário e retorna um token JWT");
        
        Map<String, Object> authRegister = new HashMap<>();
        authRegister.put("summary", "Registrar usuário");
        authRegister.put("description", "Registra um novo usuário no sistema");
        
        paths.put("/api/auth/login", Map.of("post", authLogin));
        paths.put("/api/auth/register", Map.of("post", authRegister));
        
        // User endpoints
        Map<String, Object> userList = new HashMap<>();
        userList.put("summary", "Listar usuários");
        userList.put("description", "Retorna uma lista paginada de usuários");
        
        Map<String, Object> userCreate = new HashMap<>();
        userCreate.put("summary", "Criar usuário");
        userCreate.put("description", "Cria um novo usuário no sistema");
        
        paths.put("/api/user", Map.of("get", userList, "post", userCreate));
        
        apiDocs.put("openapi", "3.0.1");
        apiDocs.put("info", info);
        apiDocs.put("paths", paths);
        
        return ResponseEntity.ok(apiDocs);
    }
    
    @GetMapping("/swagger-ui")
    public ResponseEntity<String> getSwaggerUI() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>RestaurantHub API Documentation</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; }
                    .endpoint { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
                    .method { font-weight: bold; color: #fff; padding: 5px 10px; border-radius: 3px; }
                    .get { background-color: #61affe; }
                    .post { background-color: #49cc90; }
                    .put { background-color: #fca130; }
                    .delete { background-color: #f93e3e; }
                </style>
            </head>
            <body>
                <h1>RestaurantHub API Documentation</h1>
                <p>Sistema de Gestão Compartilhada para Restaurantes</p>
                
                <h2>🔐 Autenticação</h2>
                <div class="endpoint">
                    <span class="method post">POST</span> <strong>/api/auth/register</strong>
                    <p>Registra um novo usuário no sistema</p>
                    <pre>
            {
              "name": "João Silva",
              "email": "joao@test.com", 
              "login": "joao.silva",
              "password": "password123",
              "address": "Rua das Flores, 123",
              "userTypeId": "1"
            }
                    </pre>
                </div>
                
                <div class="endpoint">
                    <span class="method post">POST</span> <strong>/api/auth/login</strong>
                    <p>Autentica um usuário e retorna um token JWT</p>
                    <pre>
            {
              "login": "joao.silva",
              "password": "password123"
            }
                    </pre>
                </div>
                
                <h2>👥 Usuários</h2>
                <div class="endpoint">
                    <span class="method get">GET</span> <strong>/api/user</strong>
                    <p>Lista usuários com paginação</p>
                    <p>Parâmetros: page, pageSize, orderBy</p>
                </div>
                
                <div class="endpoint">
                    <span class="method get">GET</span> <strong>/api/user/{id}</strong>
                    <p>Busca usuário por ID</p>
                </div>
                
                <div class="endpoint">
                    <span class="method post">POST</span> <strong>/api/user</strong>
                    <p>Cria um novo usuário (requer autenticação)</p>
                </div>
                
                <h2>🏪 Restaurantes</h2>
                <div class="endpoint">
                    <span class="method get">GET</span> <strong>/api/restaurants</strong>
                    <p>Lista restaurantes</p>
                </div>
                
                <div class="endpoint">
                    <span class="method post">POST</span> <strong>/api/restaurants</strong>
                    <p>Cria um restaurante (apenas usuários OWNER)</p>
                </div>
                
                <h2>📋 Tipos de Usuário</h2>
                <div class="endpoint">
                    <span class="method get">GET</span> <strong>/api/user-type</strong>
                    <p>Lista tipos de usuário disponíveis</p>
                </div>
                
                <h2>🍽️ Itens do Cardápio</h2>
                <div class="endpoint">
                    <span class="method get">GET</span> <strong>/api/menu-items</strong>
                    <p>Lista itens do cardápio</p>
                </div>
                
                <div class="endpoint">
                    <span class="method post">POST</span> <strong>/api/menu-items</strong>
                    <p>Cria um item do cardápio</p>
                </div>
                
                <h2>🔑 Como usar:</h2>
                <ol>
                    <li>Registre-se usando <code>POST /api/auth/register</code></li>
                    <li>Faça login usando <code>POST /api/auth/login</code></li>
                    <li>Copie o token da resposta</li>
                    <li>Use o token no header: <code>Authorization: Bearer SEU_TOKEN</code></li>
                    <li>Agora você pode acessar os endpoints protegidos!</li>
                </ol>
            </body>
            </html>
            """;
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);
    }
}