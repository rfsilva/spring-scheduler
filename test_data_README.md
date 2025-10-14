# Massa de Dados de Teste para Task Scheduler

Este arquivo contém uma massa de dados de teste para validação do front-end e agendamentos do Task Scheduler, incluindo exemplos de endpoints públicos para REST e SOAP.

## Conteúdo

O arquivo `test_data.sql` contém:

- 7 tarefas agendadas de exemplo (4 REST e 3 SOAP)
- 18 execuções de tarefas para histórico
- Diferentes estados de tarefas (ACTIVE, INACTIVE)
- Diferentes resultados de execução (SUCCESS, FAILED)

## Endpoints Públicos Incluídos

### REST APIs

1. **ViaCEP** - Consulta de CEP
   - URL: `https://viacep.com.br/ws/01001000/json/`
   - Método: GET
   - Descrição: API pública para consulta de CEPs brasileiros

2. **Brasil API - Feriados Nacionais**
   - URL: `https://brasilapi.com.br/api/feriados/v1/2023`
   - Método: GET
   - Descrição: API pública para consulta de feriados nacionais

3. **Awesome API - Cotação de Moedas**
   - URL: `https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL,GBP-BRL`
   - Método: GET
   - Descrição: API pública para consulta de cotações de moedas

4. **OpenWeatherMap - Previsão do Tempo**
   - URL: `https://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,br&appid=YOUR_API_KEY`
   - Método: GET
   - Descrição: API para consulta de previsão do tempo (requer API key)

### SOAP Services

1. **Calculator**
   - WSDL: `http://www.dneonline.com/calculator.asmx?WSDL`
   - Operação: Add
   - Descrição: Serviço SOAP público para operações matemáticas simples

2. **Temperature Converter**
   - WSDL: `https://www.w3schools.com/xml/tempconvert.asmx?WSDL`
   - Operação: CelsiusToFahrenheit
   - Descrição: Serviço SOAP público para conversão de temperatura

3. **Number Conversion**
   - WSDL: `https://www.dataaccess.com/webservicesserver/NumberConversion.wso?WSDL`
   - Operação: NumberToWords
   - Descrição: Serviço SOAP público para converter números em texto por extenso

## Como Usar

Para carregar os dados de teste no banco de dados:

```bash
psql -U seu_usuario -d sua_base_de_dados -f test_data.sql
```

Ou execute o script diretamente no seu cliente SQL preferido.

## Validação do Front-end

Com estes dados, você poderá validar:

1. **Listagem de Tarefas**: Visualização de tarefas com diferentes estados
2. **Detalhes de Tarefas**: Visualização de configurações específicas para REST e SOAP
3. **Histórico de Execuções**: Visualização de execuções bem-sucedidas e com falhas
4. **Dashboard**: Estatísticas baseadas nos dados de exemplo

## Validação de Agendamentos

Para validar a execução dos jobs, você pode:

1. Ativar as tarefas inativas
2. Modificar as expressões cron para execução imediata
3. Verificar os logs de execução após o disparo dos jobs

## Observações

- Os endpoints REST são públicos e podem ser acessados sem autenticação (exceto o OpenWeatherMap que requer API key)
- Os serviços SOAP são públicos e podem ser acessados sem autenticação
- Alguns serviços podem ter limitações de taxa de requisições