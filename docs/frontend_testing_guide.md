# Guia de Teste do Front-end com Dados de Exemplo

Este guia fornece instruções sobre como usar os dados de teste para validar as funcionalidades do front-end do Task Scheduler.

## Pré-requisitos

1. Certifique-se de que o banco de dados está populado com os dados de teste (execute `import_test_data.sh` ou `import_test_data.bat`)
2. Certifique-se de que o back-end e o front-end estão em execução

## Cenários de Teste

### 1. Dashboard

No dashboard, você deve ver:

- Total de 7 tarefas agendadas
- 5 tarefas ativas e 2 inativas
- 4 tarefas REST e 3 tarefas SOAP
- Gráficos mostrando a distribuição de tarefas por tipo e status
- Histórico de execuções recentes (18 execuções no total)

**Validação**: Verifique se os números e gráficos correspondem aos dados importados.

### 2. Lista de Tarefas

Na lista de tarefas, você deve ver:

- 7 tarefas no total
- Filtros funcionando corretamente (por tipo, status)
- Ordenação funcionando corretamente
- Paginação funcionando corretamente (se aplicável)

**Validação**: Aplique diferentes filtros e verifique se os resultados correspondem aos dados importados.

### 3. Detalhes da Tarefa

Ao clicar em uma tarefa, você deve ver:

- Informações básicas da tarefa (nome, descrição, tipo, status)
- Configuração específica do tipo de tarefa (REST ou SOAP)
- Histórico de execuções da tarefa
- Próxima execução agendada

**Validação**: Verifique os detalhes de cada tipo de tarefa:

#### Tarefa REST "Consulta CEP ViaCEP" (ID 1)
- Verifique se a URL do endpoint está correta: `https://viacep.com.br/ws/01001000/json/`
- Verifique se o método HTTP está correto: `GET`
- Verifique se os headers estão corretos
- Verifique se não há corpo da requisição

#### Tarefa SOAP "Calculadora SOAP" (ID 5)
- Verifique se a URL WSDL está correta: `http://www.dneonline.com/calculator.asmx?WSDL`
- Verifique se a operação está correta: `Add`
- Verifique se o namespace está correto: `http://tempuri.org/`
- Verifique se o XML de requisição está correto

### 4. Histórico de Execuções

Na página de histórico de execuções, você deve ver:

- 18 execuções no total
- Filtros funcionando corretamente (por tarefa, status)
- Ordenação funcionando corretamente
- Paginação funcionando corretamente (se aplicável)

**Validação**: Aplique diferentes filtros e verifique se os resultados correspondem aos dados importados.

### 5. Criação de Nova Tarefa

Ao criar uma nova tarefa:

- Selecione o tipo REST
- Preencha os campos obrigatórios
- Use um dos endpoints públicos fornecidos
- Salve a tarefa

**Validação**: Verifique se a tarefa foi criada corretamente e aparece na lista de tarefas.

### 6. Edição de Tarefa

Ao editar uma tarefa existente:

- Modifique alguns campos
- Salve as alterações

**Validação**: Verifique se as alterações foram salvas corretamente.

### 7. Execução Manual de Tarefa

Para executar uma tarefa manualmente:

- Encontre uma tarefa ativa na lista
- Clique no botão de execução manual

**Validação**: Verifique se a tarefa foi executada e se uma nova entrada foi adicionada ao histórico de execuções.

### 8. Ativação/Desativação de Tarefa

Para ativar/desativar uma tarefa:

- Encontre uma tarefa na lista
- Altere seu status (ativo/inativo)

**Validação**: Verifique se o status foi alterado corretamente.

## Testes de Responsividade

Teste o front-end em diferentes tamanhos de tela:

1. Desktop (1920x1080)
2. Tablet (768x1024)
3. Mobile (375x667)

**Validação**: Verifique se a interface se adapta corretamente a cada tamanho de tela.

## Testes de Navegação

Teste a navegação entre as diferentes páginas:

1. Dashboard → Lista de Tarefas
2. Lista de Tarefas → Detalhes da Tarefa
3. Detalhes da Tarefa → Edição de Tarefa
4. Lista de Tarefas → Criação de Nova Tarefa
5. Dashboard → Histórico de Execuções

**Validação**: Verifique se a navegação funciona corretamente e se os dados são mantidos durante a navegação.

## Problemas Conhecidos

- A tarefa "Consulta de Clima" (ID 4) está configurada com uma API key inválida, o que resultará em erro ao executá-la
- A tarefa "Número por Extenso" (ID 7) está configurada como inativa devido a falhas anteriores