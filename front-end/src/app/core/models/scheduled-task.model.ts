// Enums
export enum TaskType {
  REST_CALL = 'REST_CALL',
  SOAP_CALL = 'SOAP_CALL',
  LOW_PLATFORM_BATCH = 'LOW_PLATFORM_BATCH',
  HIGH_PLATFORM_BATCH = 'HIGH_PLATFORM_BATCH',
  MESSAGING = 'MESSAGING'
}

export enum TaskStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  EXECUTING = 'EXECUTING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED'
}

export enum HttpMethod {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
  PATCH = 'PATCH',
  HEAD = 'HEAD',
  OPTIONS = 'OPTIONS'
}

export enum RestAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC',
  BEARER = 'BEARER',
  API_KEY = 'API_KEY'
}

export enum SoapAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC',
  WS_SECURITY = 'WS_SECURITY'
}

export enum BrokerType {
  KAFKA = 'KAFKA',
  RABBITMQ = 'RABBITMQ',
  ACTIVEMQ = 'ACTIVEMQ',
  IBM_MQ = 'IBM_MQ',
  AWS_SQS = 'AWS_SQS'
}

export enum MessagingAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC'
}

export enum DeliveryMode {
  PERSISTENT = 'PERSISTENT',
  NON_PERSISTENT = 'NON_PERSISTENT'
}

// Novo enum para HighPlatformBatchConfig
export enum EndpointType {
  JES = 'JES',
  CICS = 'CICS',
  MQ = 'MQ',
  API = 'API',
  OTHER = 'OTHER'
}

// Interfaces atualizadas
export interface RestTaskConfig {
  id?: number;
  url: string;
  method: HttpMethod;
  headers: string; // Agora obrigatório
  body?: string;
  authType: RestAuthType;
  timeout: number; // Novo campo obrigatório
  retryPolicy?: string; // Novo campo
  tokenEndpoint?: string; // Novo campo
  clientId?: string; // Novo campo
  clientSecret?: string; // Novo campo
  certificatePath?: string; // Novo campo
}

export interface SoapTaskConfig {
  id?: number;
  wsdlUrl: string; // Renomeado de endpointUrl
  operation: string; // Novo campo obrigatório
  namespace: string; // Novo campo obrigatório
  soapAction?: string;
  requestXml: string;
  authType: SoapAuthType;
  username: string; // Agora obrigatório
  password: string; // Agora obrigatório
  timeout: number; // Novo campo obrigatório
  customHeaders?: string; // Novo campo
}

export interface LowPlatformBatchConfig {
  id?: number;
  jobName: string; // Novo campo obrigatório
  command: string;
  parameters?: string; // Novo campo
  workingDirectory: string; // Agora obrigatório
  timeout: number; // Renomeado de timeoutSeconds
  runAsUser?: string; // Novo campo
  onSuccess?: string; // Renomeado de onSuccessCommand
  onFailure?: string; // Renomeado de onFailureCommand
}

export interface HighPlatformBatchConfig {
  id?: number;
  endpointType: EndpointType; // Novo campo obrigatório
  transactionId: string; // Novo campo obrigatório
  payload: string; // Novo campo obrigatório
  credentials: string; // Já existia
  timeout: number; // Renomeado de timeoutSeconds
  channel?: string; // Novo campo
  queue?: string; // Novo campo
  host?: string; // Novo campo
  port?: string; // Novo campo
  sslCertPath?: string; // Novo campo
}

export interface MessagingTaskConfig {
  id?: number;
  brokerType: BrokerType;
  destinationName: string; // Renomeado de destination
  messagePayload: string; // Renomeado de message
  connectionUrl: string; // Renomeado de connectionProperties
  authType: MessagingAuthType;
  headers: string; // Novo campo obrigatório
  deliveryMode: DeliveryMode;
  retryPolicy?: string; // Novo campo
}

export interface ScheduledTask {
  id?: number;
  name: string;
  description: string;
  taskType: TaskType;
  status: TaskStatus;
  cronExpression: string;
  maxRetries?: number;
  retryDelaySeconds?: number;
  restTaskConfig?: RestTaskConfig;
  soapTaskConfig?: SoapTaskConfig;
  lowPlatformBatchConfig?: LowPlatformBatchConfig;
  highPlatformBatchConfig?: HighPlatformBatchConfig;
  messagingTaskConfig?: MessagingTaskConfig;
}