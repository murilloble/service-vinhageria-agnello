# Vinheria Agnello - Simulação em Java (Spring Boot)

Este repositório contém duas aplicações Java Spring Boot simples:
- product-service (porta 3001): expõe /login e /products (JWT)
- order-service (porta 3002): expõe /orders (valida JWT e consulta product-service)

Arquitetura e arquivos:
- pom.xml (parent multi-module)
- product-service/ (maven module)
- order-service/ (maven module)
- nginx/ (config TLS terminator)
- dnsmasq/ (simulação DHCP/DNS)
- jenkins/Jenkinsfile (pipeline de exemplo)
- docker-compose.yml (orquestra todos os serviços)

Instruções rápidas:
1. Gere certificados em ./nginx: `sh nginx/generate_cert.sh`
2. Suba stack (requer Docker & Docker Compose): `docker-compose up -d --build`
3. Acesse Jenkins: http://localhost:8080
4. Exemplos:
   - Obter token (quando product-service estiver rodando):
     curl -s -X POST http://localhost:3001/login -H 'Content-Type: application/json' -d '{"user":"murillo"}' | jq
   - Chamar orders via nginx TLS (ignorar validação do cert autoassinado):
     curl -k -H "Authorization: Bearer <TOKEN>" https://localhost:8443/order/orders
