# 🌐 Guia Completo do Kubernetes Ingress - K8sLab

## 🤔 **O QUE É INGRESS?**

**Ingress** é o "portão de entrada" do seu cluster Kubernetes para tráfego HTTP/HTTPS externo.

### **🏢 Analogia: Shopping Center**
```
Internet → Portaria do Shopping → Lojas específicas
Internet → Ingress Controller  → Services/Pods
```

**Ingress** = **Portaria inteligente** que:
- 🚪 **Recebe visitantes** (requisições HTTP)
- 🗺️ **Lê o mapa** (regras do Ingress)
- 🎯 **Direciona para loja certa** (Service correto)

---

## 🏗️ **ARQUITETURA DO INGRESS**

### **Componentes principais:**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Internet      │───▶│ Ingress          │───▶│ Services        │
│   (Cliente)     │    │ Controller       │    │ (Load Balancer) │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                              │                          │
                              ▼                          ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │ Ingress Resource │    │ Pods            │
                       │ (Regras YAML)    │    │ (Aplicação)     │
                       └──────────────────┘    └─────────────────┘
```

### **1. Ingress Resource (Suas regras)**
```yaml
# Arquivo YAML com regras de roteamento
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: k8slab-ingress
spec:
  rules:
  - host: k8slab.local
    http:
      paths:
      - path: /api
        backend:
          service:
            name: backend-service
```

### **2. Ingress Controller (Quem executa)**
```
Nginx Ingress Controller = Porteiro experiente que:
- Lê suas regras (Ingress Resource)
- Configura roteamento real (Nginx)
- Processa requisições HTTP
- Aplica SSL/TLS, CORS, etc.
```

---

## 🎯 **COMO FUNCIONA - FLUXO COMPLETO**

### **Exemplo prático do nosso K8sLab:**

```
1. 👤 Cliente digita: http://k8slab.local/api/tasks
2. 🌐 DNS resolve: k8slab.local → 127.0.0.1 (localhost)
3. 🚪 Ingress Controller recebe requisição na porta 80
4. 📋 Lê regras do Ingress: "/api" → backend-service:8080
5. ⚖️ Service distribui para um dos Pods do backend
6. 🏃 Pod processa requisição (Spring Boot)
7. 📤 Resposta volta: Pod → Service → Ingress → Cliente
```

### **Roteamento por path:**
```
k8slab.local/          → frontend-service:80  (Angular)
k8slab.local/api/      → backend-service:8080 (Spring Boot)
k8slab.local/admin/    → admin-service:3000   (Futuro)
```

---

## 🔧 **CONFIGURAÇÃO PRÁTICA**

### **1. Instalar Nginx Ingress Controller:**
```bash
# Para Docker Desktop
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# Aguardar ficar pronto
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=120s

# Verificar instalação
kubectl get pods -n ingress-nginx
```

### **2. Criar Ingress Resource:**
```yaml
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: k8slab-ingress
  namespace: k8slab
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    nginx.ingress.kubernetes.io/cors-allow-origin: "*"
    nginx.ingress.kubernetes.io/cors-allow-methods: "GET, POST, PUT, DELETE, OPTIONS"
spec:
  ingressClassName: nginx
  rules:
  - host: k8slab.local
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: backend-service
            port:
              number: 8080
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 80
```

### **3. Configurar DNS local:**
```bash
# Windows (como Administrador)
echo "127.0.0.1 k8slab.local" >> C:\Windows\System32\drivers\etc\hosts

# Linux/Mac
echo "127.0.0.1 k8slab.local" | sudo tee -a /etc/hosts
```

### **4. Aplicar e testar:**
```bash
# Aplicar Ingress
kubectl apply -f k8s/ingress.yaml

# Verificar status
kubectl get ingress -n k8slab
kubectl describe ingress k8slab-ingress -n k8slab

# Testar aplicação
curl http://k8slab.local/api/tasks
# Ou abrir no navegador: http://k8slab.local
```

---

## 📊 **INGRESS vs OUTRAS SOLUÇÕES**

### **Comparação:**

| Método | Prós | Contras | Uso |
|--------|------|---------|-----|
| **Port Forward** | Simples, debug | Manual, uma porta por vez | Desenvolvimento |
| **NodePort** | Expõe diretamente | Portas altas (30000+) | Teste rápido |
| **LoadBalancer** | Automático | Só funciona em cloud | Produção cloud |
| **Ingress** | Flexível, uma porta | Mais complexo | Produção |

### **Exemplo prático:**

#### **Sem Ingress (Port Forward):**
```bash
kubectl port-forward service/frontend-service 3000:80 -n k8slab
kubectl port-forward service/backend-service 8080:8080 -n k8slab
# Acesso: localhost:3000 e localhost:8080
```

#### **Com Ingress:**
```bash
# Apenas um ponto de entrada
# Acesso: k8slab.local (tudo)
```

---

## 🎯 **ANNOTATIONS IMPORTANTES**

### **Rewrite Target:**
```yaml
nginx.ingress.kubernetes.io/rewrite-target: /
# Remove o path da URL antes de enviar para o service
# /api/tasks → /tasks (para o backend)
```

### **CORS:**
```yaml
nginx.ingress.kubernetes.io/cors-allow-origin: "*"
nginx.ingress.kubernetes.io/cors-allow-methods: "GET, POST, PUT, DELETE, OPTIONS"
# Permite requisições de qualquer origem (frontend → backend)
```

### **SSL/TLS:**
```yaml
nginx.ingress.kubernetes.io/ssl-redirect: "true"
# Força HTTPS
```

### **Rate Limiting:**
```yaml
nginx.ingress.kubernetes.io/rate-limit: "100"
# Máximo 100 requests por minuto
```

---

## 🔍 **TROUBLESHOOTING**

### **Problemas comuns:**

#### **1. Ingress sem ADDRESS:**
```bash
# Problema: Ingress Controller não instalado
kubectl get ingress -n k8slab
# ADDRESS vazio

# Solução: Instalar Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml
```

#### **2. 404 Not Found:**
```bash
# Problema: Path ou Service errado
kubectl describe ingress k8slab-ingress -n k8slab
# Verificar Backends

# Solução: Corrigir paths no Ingress
```

#### **3. Connection Refused:**
```bash
# Problema: Service não responde
kubectl get endpoints -n k8slab
# Verificar se Pods estão Ready

# Solução: Verificar Pods e Services
kubectl get pods -n k8slab
kubectl logs deployment/backend-deployment -n k8slab
```

#### **4. DNS não resolve:**
```bash
# Problema: k8slab.local não funciona
nslookup k8slab.local

# Solução: Adicionar ao hosts
echo "127.0.0.1 k8slab.local" >> /etc/hosts
```

### **Comandos de debug:**
```bash
# Ver logs do Ingress Controller
kubectl logs -n ingress-nginx deployment/ingress-nginx-controller

# Ver configuração do Nginx
kubectl exec -n ingress-nginx deployment/ingress-nginx-controller -- cat /etc/nginx/nginx.conf

# Testar conectividade interna
kubectl exec -it <pod-name> -n k8slab -- curl backend-service:8080/api/tasks
```

---

## 🚀 **CASOS DE USO AVANÇADOS**

### **1. Múltiplos domínios:**
```yaml
spec:
  rules:
  - host: app.empresa.com
    http:
      paths:
      - path: /
        backend:
          service:
            name: app-service
  - host: api.empresa.com
    http:
      paths:
      - path: /
        backend:
          service:
            name: api-service
```

### **2. Roteamento por subpath:**
```yaml
spec:
  rules:
  - host: empresa.com
    http:
      paths:
      - path: /app
        backend:
          service:
            name: frontend-service
      - path: /api
        backend:
          service:
            name: backend-service
      - path: /admin
        backend:
          service:
            name: admin-service
```

### **3. SSL automático (cert-manager):**
```yaml
metadata:
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
  - hosts:
    - k8slab.local
    secretName: k8slab-tls
```

---

## 💡 **BOAS PRÁTICAS**

### **1. Organização:**
```bash
# Um Ingress por aplicação/namespace
k8s/
├── ingress-frontend.yaml
├── ingress-api.yaml
└── ingress-admin.yaml
```

### **2. Segurança:**
```yaml
# Sempre usar HTTPS em produção
nginx.ingress.kubernetes.io/ssl-redirect: "true"

# Rate limiting
nginx.ingress.kubernetes.io/rate-limit: "100"

# Whitelist IPs
nginx.ingress.kubernetes.io/whitelist-source-range: "10.0.0.0/8"
```

### **3. Performance:**
```yaml
# Cache estático
nginx.ingress.kubernetes.io/proxy-cache-valid: "200 302 10m"

# Compressão
nginx.ingress.kubernetes.io/enable-gzip: "true"
```

---

## 🎯 **RESUMO EXECUTIVO**

### **O que é Ingress:**
- **Portão de entrada** HTTP/HTTPS do cluster
- **Roteamento inteligente** baseado em host/path
- **Ponto único** de acesso para múltiplos services

### **Quando usar:**
- ✅ **Produção** com múltiplas aplicações
- ✅ **SSL/TLS** necessário
- ✅ **Roteamento complexo** por domínio/path
- ✅ **Load balancing** avançado

### **Quando NÃO usar:**
- ❌ **Desenvolvimento simples** (use port-forward)
- ❌ **Tráfego não-HTTP** (TCP/UDP)
- ❌ **Cluster muito simples** (use NodePort)

### **Comandos essenciais:**
```bash
# Instalar Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# Ver Ingress
kubectl get ingress -A

# Debug
kubectl describe ingress <name> -n <namespace>
kubectl logs -n ingress-nginx deployment/ingress-nginx-controller
```

---

## 🔗 **LINKS ÚTEIS**

- **Documentação oficial**: https://kubernetes.io/docs/concepts/services-networking/ingress/
- **Nginx Ingress**: https://kubernetes.github.io/ingress-nginx/
- **Annotations**: https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration/annotations/
- **Troubleshooting**: https://kubernetes.github.io/ingress-nginx/troubleshooting/

---

**🎉 Agora você domina Ingress no Kubernetes! Use este guia como referência para projetos futuros.**