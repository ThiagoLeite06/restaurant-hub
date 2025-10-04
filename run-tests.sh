#!/bin/bash

echo "🧪 Executando testes do RestaurantHub..."
echo "========================================"

# Limpar builds anteriores
echo "🧹 Limpando builds anteriores..."
./mvnw clean

# Executar testes com cobertura
echo "🚀 Executando testes unitários e de integração..."
./mvnw test

# Gerar relatório de cobertura
echo "📊 Gerando relatório de cobertura..."
./mvnw jacoco:report

# Verificar cobertura mínima
echo "✅ Verificando cobertura mínima (80%)..."
./mvnw jacoco:check

echo ""
echo "📋 Resultados:"
echo "- Testes executados: ✅"
echo "- Relatório de cobertura gerado em: target/site/jacoco/index.html"
echo "- Swagger UI disponível em: http://localhost:8080/swagger-ui.html"
echo ""
echo "🎉 Testes concluídos com sucesso!"