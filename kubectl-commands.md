# 🚀 Comandos kubectl Essenciais - K8sLab

## 🤔 **O QUE SÃO MANIFESTS?**

**Manifests** são arquivos YAML que descrevem como você quer que seus recursos sejam no Kubernetes.

**Analogia**: São como "plantas de construção" que dizem ao K8s:
- Quantos apartamentos (Pods) construir
- Como conectar a eletricidade (Services)
- Onde fica a portaria (Ingress)

**Por que aplicar?**
- ✅ **Declarativo**: Você diz "quero isso" e K8s faz acontecer
- ✅ **Versionado**: Mudanças ficam no Git
- ✅ **Reproduzível**: Mesmo resultado em qualquer cluster
- ✅ **Auditável**: Histórico de todas as mudanças

---

## 📋 **1. APLICAR MANIFESTS (Deploy)**

### O que significa "aplicar"?
**Aplicar** = Dizer ao Kubernetes "faça minha aplicação ficar assim"
- Se não existe → **Cria**
- Se existe diferente → **Atualiza**
- Se já está igual → **Não faz nada**

### Aplicar todos os manifests:
```bash
# Aplicar todos os arquivos da pasta k8s/
kubectl apply -f k8s/
# 📝 O que faz: Lê todos os .yaml da pasta e cria/atualiza recursos no cluster
# 🎯 Resultado: Namespace + Deployments + Services + Ingress criados

# Aplicar arquivo específico
kubectl apply -f k8s/namespace.yaml
# 📝 O que faz: Cria apenas o namespace "k8slab"
# 🎯 Resultado: Ambiente isolado criado para nossa aplicação

kubectl apply -f k8s/backend-deployment.yaml
# 📝 O que faz: Cria o Deployment que gerencia os Pods do backend
# 🎯 Resultado: 2 Pods do Spring Boot rodando
```

### Verificar se foi aplicado:
```bash
# Ver todos os recursos no namespace
kubectl get all -n k8slab
# 📝 O que faz: Lista TUDO que existe no namespace k8slab
# 🎯 Mostra: Pods, Services, Deployments, ReplicaSets
# 💡 Use para: Visão geral completa da aplicação

# Ver recursos específicos
kubectl get pods -n k8slab
# 📝 O que faz: Lista apenas os Pods (containers rodando)
# 🎯 Mostra: Nome, Status (Running/Pending/Error), Restarts, Age
# 💡 Use para: Ver se containers estão rodando

kubectl get services -n k8slab
# 📝 O que faz: Lista os Services (load balancers internos)
# 🎯 Mostra: Nome, Tipo, IP interno, Portas
# 💡 Use para: Ver como acessar os Pods

kubectl get deployments -n k8slab
# 📝 O que faz: Lista os Deployments (gerenciadores de Pods)
# 🎯 Mostra: Nome, Réplicas desejadas vs disponíveis, Age
# 💡 Use para: Ver se tem o número certo de Pods
```

---

## 🔍 **2. VISUALIZAR RECURSOS (Get/Describe)**

### Por que visualizar recursos?
**Monitoramento** é essencial para saber:
- ✅ **Status**: Está rodando? Com erro?
- ✅ **Performance**: Usando muita CPU/memória?
- ✅ **Problemas**: Por que não está funcionando?
- ✅ **Capacidade**: Precisa escalar?

### Listar recursos:
```bash
# Todos os pods
kubectl get pods -n k8slab
# 📝 O que faz: Lista containers rodando no namespace
# 🎯 Mostra: backend-deployment-abc123 (Running), frontend-deployment-def456 (Running)
# 💡 Use para: Ver se aplicação está rodando

# Pods com mais detalhes
kubectl get pods -n k8slab -o wide
# 📝 O que faz: Mostra IP, Node onde está rodando, imagem usada
# 🎯 Mostra: IP interno (10.244.1.5), Node (docker-desktop), Image (k8slab-backend:latest)
# 💡 Use para: Debug de rede e localização dos Pods

# Ver em tempo real (watch)
kubectl get pods -n k8slab -w
# 📝 O que faz: Fica "assistindo" mudanças nos Pods em tempo real
# 🎯 Mostra: Quando Pod é criado, morre, reinicia
# 💡 Use para: Acompanhar deploys e troubleshooting

# Todos os services
kubectl get svc -n k8slab
# 📝 O que faz: Lista "portarias" que distribuem tráfego para Pods
# 🎯 Mostra: backend-service (ClusterIP: 10.96.1.100:8080)
# 💡 Use para: Ver como acessar internamente os Pods

# Todos os deployments
kubectl get deploy -n k8slab
# 📝 O que faz: Lista "síndicos" que gerenciam Pods
# 🎯 Mostra: backend-deployment (2/2 ready) = 2 Pods rodando de 2 desejados
# 💡 Use para: Ver se tem réplicas suficientes
```

### Detalhes completos:
```bash
# Detalhes de um pod específico
kubectl describe pod <nome-do-pod> -n k8slab
# 📝 O que faz: Mostra TUDO sobre um Pod específico
# 🎯 Mostra: Eventos, recursos usados, volumes, rede, status dos containers
# 💡 Use para: Debug quando Pod não inicia ou tem problemas
# 🔧 Exemplo de saída: "Failed to pull image" = problema na imagem Docker

# Detalhes do deployment
kubectl describe deployment backend-deployment -n k8slab
# 📝 O que faz: Mostra configuração e histórico do Deployment
# 🎯 Mostra: Estratégia de update, condições, eventos de scaling
# 💡 Use para: Ver histórico de atualizações e configurações

# Detalhes do service
kubectl describe service backend-service -n k8slab
# 📝 O que faz: Mostra como Service está roteando tráfego
# 🎯 Mostra: Endpoints (IPs dos Pods), seletor, portas
# 💡 Use para: Debug de conectividade entre Services e Pods
```

---

## 📊 **3. LOGS E DEBUG**

### Por que ver logs?
**Logs** são como "caixa preta" do avião:
- 🐛 **Debug**: Por que deu erro?
- 📈 **Monitoramento**: Como está a performance?
- 🔍 **Auditoria**: O que aconteceu?
- 🚨 **Alertas**: Detectar problemas

### Ver logs:
```bash
# Logs de um pod específico
kubectl logs <nome-do-pod> -n k8slab
# 📝 O que faz: Mostra saída do console da aplicação (System.out.println, console.log)
# 🎯 Mostra: Logs do Spring Boot, erros de inicialização, requests HTTP
# 💡 Use para: Ver se aplicação iniciou corretamente

# Logs em tempo real (follow)
kubectl logs -f <nome-do-pod> -n k8slab
# 📝 O que faz: Fica "grudado" no log, mostrando novas linhas em tempo real
# 🎯 Mostra: Requests chegando, erros acontecendo agora
# 💡 Use para: Acompanhar aplicação funcionando, debug em tempo real

# Logs de todos os pods de um deployment
kubectl logs -f deployment/backend-deployment -n k8slab
# 📝 O que faz: Mostra logs de TODOS os Pods do backend misturados
# 🎯 Mostra: Logs dos 2 Pods do backend juntos
# 💡 Use para: Ver comportamento geral da aplicação

# Logs das últimas 50 linhas
kubectl logs --tail=50 <nome-do-pod> -n k8slab
# 📝 O que faz: Mostra apenas as 50 linhas mais recentes
# 🎯 Mostra: Últimos eventos, sem poluição de logs antigos
# 💡 Use para: Foco nos problemas recentes
```

### Executar comandos dentro do pod:
```bash
# Entrar no pod (bash)
kubectl exec -it <nome-do-pod> -n k8slab -- bash
# 📝 O que faz: "SSH" para dentro do container, como se fosse uma VM
# 🎯 Resultado: Terminal dentro do Pod, pode navegar, editar arquivos
# 💡 Use para: Debug avançado, ver arquivos, testar conectividade
# ⚠️ Cuidado: Mudanças são perdidas se Pod reiniciar

# Executar comando específico
kubectl exec <nome-do-pod> -n k8slab -- ls -la
# 📝 O que faz: Executa comando específico sem entrar no Pod
# 🎯 Resultado: Lista arquivos do diretório atual do container
# 💡 Use para: Comandos rápidos, verificações pontuais

# Ver arquivos do nginx
kubectl exec <nome-do-pod-frontend> -n k8slab -- ls /usr/share/nginx/html/
# 📝 O que faz: Lista arquivos que o Nginx está servindo
# 🎯 Mostra: index.html, main.js, styles.css (arquivos do Angular)
# 💡 Use para: Verificar se build do frontend foi copiado corretamente
```

---

## ⚖️ **4. ESCALAR APLICAÇÃO**

### Por que escalar?
**Scaling** = Ajustar capacidade conforme demanda:
- 📈 **Mais tráfego**: Adicionar réplicas
- 📉 **Menos tráfego**: Remover réplicas (economizar recursos)
- 🚨 **Alta disponibilidade**: Múltiplas réplicas (se uma falha, outras continuam)
- ⚡ **Performance**: Distribuir carga entre várias instâncias

### Alterar número de réplicas:
```bash
# Escalar backend para 5 réplicas
kubectl scale deployment backend-deployment --replicas=5 -n k8slab
# 📝 O que faz: Muda de 2 para 5 Pods do backend rodando
# 🎯 Resultado: 3 novos Pods são criados automaticamente
# 💡 Use para: Aumentar capacidade quando tráfego cresce
# ⏱️ Tempo: ~30 segundos para Pods ficarem prontos

# Escalar frontend para 1 réplica
kubectl scale deployment frontend-deployment --replicas=1 -n k8slab
# 📝 O que faz: Reduz de 3 para 1 Pod do frontend
# 🎯 Resultado: 2 Pods são terminados graciosamente
# 💡 Use para: Economizar recursos em ambiente de desenvolvimento
# ⚠️ Cuidado: Menos réplicas = menor disponibilidade

# Ver o resultado
kubectl get pods -n k8slab
# 📝 O que faz: Confirma quantos Pods estão rodando agora
# 🎯 Mostra: 5 Pods backend + 1 Pod frontend
# 💡 Use para: Verificar se scaling funcionou
```

---

## 🔄 **5. ATUALIZAÇÕES (Rolling Update)**

### O que é Rolling Update?
**Rolling Update** = Atualização sem downtime:
- 🔄 **Gradual**: Substitui Pods um por vez
- ⚡ **Zero downtime**: Aplicação nunca para
- 🛡️ **Seguro**: Se nova versão falha, para o processo
- 🔙 **Reversível**: Pode voltar versão anterior

**Analogia**: Como reformar loja sem fechar - reforma uma seção por vez

### Atualizar imagem:
```bash
# Atualizar imagem do backend
kubectl set image deployment/backend-deployment backend=k8slab-backend:v2 -n k8slab
# 📝 O que faz: Muda imagem Docker de "latest" para "v2"
# 🎯 Processo: Cria Pod novo (v2) → Testa se funciona → Mata Pod antigo → Repete
# 💡 Use para: Deploy de nova versão da aplicação
# ⏱️ Tempo: ~2-3 minutos para completar (depende do readinessProbe)

# Ver status do rollout
kubectl rollout status deployment/backend-deployment -n k8slab
# 📝 O que faz: Acompanha progresso da atualização em tempo real
# 🎯 Mostra: "Waiting for rollout to finish: 1 of 2 updated replicas are available"
# 💡 Use para: Saber quando deploy terminou
# ✅ Sucesso: "deployment successfully rolled out"

# Ver histórico de rollouts
kubectl rollout history deployment/backend-deployment -n k8slab
# 📝 O que faz: Lista todas as versões já deployadas
# 🎯 Mostra: REVISION 1 (latest), REVISION 2 (v2), etc.
# 💡 Use para: Auditoria de deploys, escolher versão para rollback
```

### Rollback (voltar versão):
```bash
# Voltar para versão anterior
kubectl rollout undo deployment/backend-deployment -n k8slab
# 📝 O que faz: Volta para a versão imediatamente anterior
# 🎯 Processo: Mesmo rolling update, mas voltando para imagem antiga
# 💡 Use para: Quando nova versão tem bug crítico
# ⚡ Rápido: Imagem antiga já está em cache

# Voltar para versão específica
kubectl rollout undo deployment/backend-deployment --to-revision=1 -n k8slab
# 📝 O que faz: Volta para uma revisão específica do histórico
# 🎯 Resultado: Volta exatamente para REVISION 1
# 💡 Use para: Voltar várias versões atrás
```

---

## 🗑️ **6. DELETAR RECURSOS**

### Por que deletar?
- 🧹 **Limpeza**: Remover recursos não usados
- 💰 **Economia**: Liberar CPU/memória
- 🔄 **Restart**: Forçar recriação de Pod com problema
- 🚀 **Deploy**: Remover versão antiga

### Deletar recursos específicos:
```bash
# Deletar um pod (será recriado pelo deployment)
kubectl delete pod <nome-do-pod> -n k8slab
# 📝 O que faz: Remove Pod específico, mas Deployment cria outro no lugar
# 🎯 Resultado: Pod é terminado → Novo Pod é criado automaticamente
# 💡 Use para: "Restart" de Pod com problema (memory leak, travado)
# ⚠️ Importante: Deployment sempre mantém número de réplicas desejado

# Deletar deployment (deleta todos os pods)
kubectl delete deployment backend-deployment -n k8slab
# 📝 O que faz: Remove Deployment E todos os Pods que ele gerencia
# 🎯 Resultado: Todos os Pods do backend são terminados e NÃO recriados
# 💡 Use para: Remover aplicação completamente
# ⚠️ Cuidado: Aplicação fica indisponível!

# Deletar service
kubectl delete service backend-service -n k8slab
# 📝 O que faz: Remove "portaria" que distribui tráfego
# 🎯 Resultado: Pods continuam rodando, mas não são acessíveis internamente
# 💡 Use para: Reconfigurar rede, isolar aplicação
```

### Deletar tudo:
```bash
# Deletar todos os recursos dos manifests
kubectl delete -f k8s/
# 📝 O que faz: Lê todos os .yaml e deleta os recursos descritos neles
# 🎯 Resultado: Aplicação inteira é removida (Pods, Services, Ingress)
# 💡 Use para: "Uninstall" completo da aplicação
# 🔄 Reversível: kubectl apply -f k8s/ recria tudo

# Deletar namespace inteiro (cuidado!)
kubectl delete namespace k8slab
# 📝 O que faz: Remove namespace E TUDO que está dentro dele
# 🎯 Resultado: Todos os recursos do k8slab são deletados permanentemente
# 💡 Use para: Limpeza completa, reset total
# ⚠️ CUIDADO: Não tem volta! Perde tudo!
```

---

## 🌐 **7. NETWORKING E CONECTIVIDADE**

### Por que Port Forward?
**Port Forward** = "Túnel" do seu PC para dentro do cluster:
- 🔧 **Debug**: Acessar Pod diretamente sem Ingress
- 🧪 **Teste**: Testar aplicação antes de expor publicamente
- 🔍 **Troubleshooting**: Verificar se problema é no Pod ou na rede
- 💻 **Desenvolvimento**: Acessar banco de dados interno

### Port Forward (acessar pod localmente):
```bash
# Acessar backend diretamente
kubectl port-forward pod/<nome-do-pod-backend> 8080:8080 -n k8slab
# 📝 O que faz: Cria "túnel" do seu localhost:8080 para Pod:8080
# 🎯 Resultado: http://localhost:8080 acessa diretamente o Pod
# 💡 Use para: Testar Pod específico, bypass do Service
# ⚠️ Importante: Acessa apenas 1 Pod, não o load balancer
# Acesse: http://localhost:8080/api/tasks

# Acessar via service
kubectl port-forward service/backend-service 8080:8080 -n k8slab
# 📝 O que faz: Túnel para Service (load balancer), não Pod específico
# 🎯 Resultado: Requisições são distribuídas entre todos os Pods
# 💡 Use para: Testar comportamento real da aplicação
# ✅ Recomendado: Simula acesso real via Service
```

### Testar conectividade:
```bash
# Entrar em um pod e testar rede
kubectl exec -it <nome-do-pod> -n k8slab -- bash
# 📝 O que faz: "SSH" para dentro do Pod para testar rede interna
# 🎯 Objetivo: Ver se Pods conseguem se comunicar
# 💡 Use para: Debug de problemas de conectividade

# Dentro do pod, testar:
curl backend-service:8080/api/tasks
# 📝 O que faz: Testa se consegue acessar backend via Service DNS
# 🎯 Resultado: JSON das tasks OU erro de conexão
# 💡 Sucesso: Service e Pods estão funcionando
# ❌ Erro: Problema no Service ou Pods

curl frontend-service:80
# 📝 O que faz: Testa acesso ao frontend via Service
# 🎯 Resultado: HTML da página OU erro
# 💡 Use para: Verificar se frontend está acessível internamente

nslookup backend-service
# 📝 O que faz: Testa se DNS interno do Kubernetes está funcionando
# 🎯 Resultado: IP do Service (ex: 10.96.1.100)
# 💡 Sucesso: DNS resolve nome para IP
# ❌ Erro: Problema no DNS do cluster
```

---

## 📈 **8. MONITORAMENTO**

### Por que monitorar?
**Monitoramento** = "Painel do carro" do cluster:
- 📊 **Performance**: CPU/memória alta?
- 🚨 **Alertas**: Problemas acontecendo?
- 📈 **Capacidade**: Precisa escalar?
- 🔍 **Troubleshooting**: O que aconteceu?

### Ver uso de recursos:
```bash
# CPU e memória dos pods
kubectl top pods -n k8slab
# 📝 O que faz: Mostra consumo atual de CPU e RAM de cada Pod
# 🎯 Mostra: backend-pod (CPU: 250m, Memory: 512Mi)
# 💡 Use para: Identificar Pods que estão consumindo muito recurso
# 🚨 Alerta: Se próximo do limite (definido em resources.limits)
# 📊 Exemplo: 250m = 25% de 1 CPU, 512Mi = 512 megabytes RAM

# CPU e memória dos nodes
kubectl top nodes
# 📝 O que faz: Mostra consumo de recursos dos servidores do cluster
# 🎯 Mostra: docker-desktop (CPU: 15%, Memory: 60%)
# 💡 Use para: Ver se cluster tem capacidade para mais Pods
# ⚠️ Cuidado: >80% = cluster próximo do limite

# Eventos do cluster
kubectl get events -n k8slab --sort-by='.lastTimestamp'
# 📝 O que faz: Lista "log de eventos" do que aconteceu no namespace
# 🎯 Mostra: Pod criado, imagem baixada, erro de inicialização, etc.
# 💡 Use para: Timeline do que aconteceu, debug de problemas
# 🔍 Eventos comuns: "Pulled image", "Started container", "Failed to pull"
# ⏰ Ordenado: Mais recentes primeiro
```

---

## 🎯 **COMANDOS MAIS USADOS EM ENTREVISTAS**

### 1. Deploy e verificação:
```bash
kubectl apply -f k8s/
kubectl get pods -n k8slab
kubectl get svc -n k8slab
```

### 2. Debug de problemas:
```bash
kubectl describe pod <pod-name> -n k8slab
kubectl logs <pod-name> -n k8slab
kubectl get events -n k8slab
```

### 3. Escalar aplicação:
```bash
kubectl scale deployment backend-deployment --replicas=3 -n k8slab
kubectl get pods -n k8slab
```

### 4. Atualizar aplicação:
```bash
kubectl set image deployment/backend-deployment backend=nova-versao:v2 -n k8slab
kubectl rollout status deployment/backend-deployment -n k8slab
```

### 5. Acessar aplicação:
```bash
kubectl port-forward service/backend-service 8080:8080 -n k8slab
```

---

## 💡 **DICAS PARA ENTREVISTAS**

1. **Sempre use namespace**: `-n k8slab`
2. **Use labels para filtrar**: `kubectl get pods -l app=backend -n k8slab`
3. **Conheça os shortcuts**: `po` (pods), `svc` (services), `deploy` (deployments)
4. **Saiba explicar**: O que cada comando faz e quando usar
5. **Troubleshooting**: describe → logs → events (nessa ordem)

---

## 🚀 **PRÓXIMOS PASSOS**

Para testar esses comandos, você precisa:
1. **Instalar kubectl**
2. **Ter cluster Kubernetes** (Docker Desktop, Minikube, etc.)
3. **Aplicar os manifests** que criamos
4. **Praticar os comandos** acima