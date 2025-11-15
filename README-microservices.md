# K8sLab - Microserviços

## 🚀 Estrutura Criada

```
k8slab/
├── backend/
│   ├── Dockerfile                 ✅ Já existia
│   └── docker-compose.yml         ✅ Criado (deploy independente)
├── frontend/
│   ├── Dockerfile                 ✅ Criado
│   ├── nginx.conf                 ✅ Criado (SPA routing)
│   └── docker-compose.yml         ✅ Criado (deploy independente)
├── docker-compose.yml             ✅ Atualizado (orquestração completa)
├── nginx-gateway.conf             ✅ Criado (API Gateway)
└── nginx.conf                     ✅ Original (só backend)
```

## 🎯 Como Usar

### Deploy Independente - Backend
```bash
cd backend
docker-compose up
# Acesso: http://localhost:8080
```

### Deploy Independente - Frontend  
```bash
cd frontend
docker-compose up
# Acesso: http://localhost:3000
```

### Deploy Completo (Orquestração)
```bash
# Na raiz do projeto
docker-compose up
# Frontend: http://localhost:3000
# Backend: http://localhost:8080  
# Gateway: http://localhost:80 (roteia tudo)
```

## 🔄 Roteamento do Gateway (porta 80)

- `http://localhost/` → Frontend (Angular)
- `http://localhost/api/` → Backend (Spring Boot)

## 💡 Vantagens

✅ **Deploys independentes** por time
✅ **Tecnologias diferentes** evoluem separadamente  
✅ **Escalabilidade** individual
✅ **Rollback** independente
✅ **Desenvolvimento** isolado