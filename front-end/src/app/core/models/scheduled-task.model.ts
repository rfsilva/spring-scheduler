export enum TaskType {
  REST = 'REST',
  SOAP = 'SOAP',
  LOW_PLATFORM_BATCH = 'LOW_PLATFORM_BATCH',
  HIGH_PLATFORM_BATCH = 'HIGH_PLATFORM_BATCH',
  MESSAGING = 'MESSAGING'
}

export enum TaskStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  RUNNING = 'RUNNING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED'
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

export enum HttpMethod {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
  PATCH = 'PATCH'
}

export enum RestAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC',
  BEARER = 'BEARER',
  API_KEY = 'API_KEY',
  OAUTH2 = 'OAUTH2'
}

export interface RestTaskConfig {
  id?: number;
  url: string;
  method: HttpMethod;
  headers?: string;
  body?: string;
  authType: RestAuthType;
  username?: string;
  password?: string;
  token?: string;
  apiKeyName?: string;
  apiKeyValue?: string;
  connectTimeoutSeconds?: number;
  readTimeoutSeconds?: number;
}

export enum SoapAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC',
  WS_SECURITY = 'WS_SECURITY'
}

export interface SoapTaskConfig {
  id?: number;
  endpointUrl: string;
  soapAction?: string;
  requestXml: string;
  authType: SoapAuthType;
  username?: string;
  password?: string;
  connectTimeoutSeconds?: number;
  readTimeoutSeconds?: number;
}

export interface LowPlatformBatchConfig {
  id?: number;
  command: string;
  workingDirectory?: string;
  environmentVariables?: string;
  successExitCodes?: string;
  onSuccessCommand?: string;
  onFailureCommand?: string;
  timeoutSeconds?: number;
}

export interface HighPlatformBatchConfig {
  id?: number;
  systemType: string;
  jobName: string;
  parameters?: string;
  credentials?: string;
  connectionDetails?: string;
  timeoutSeconds?: number;
}

export enum BrokerType {
  ACTIVEMQ = 'ACTIVEMQ',
  RABBITMQ = 'RABBITMQ',
  KAFKA = 'KAFKA',
  IBM_MQ = 'IBM_MQ',
  AWS_SQS = 'AWS_SQS'
}

export enum MessagingAuthType {
  NONE = 'NONE',
  BASIC = 'BASIC',
  SSL = 'SSL',
  SASL = 'SASL'
}

export enum DeliveryMode {
  PERSISTENT = 'PERSISTENT',
  NON_PERSISTENT = 'NON_PERSISTENT'
}

export interface MessagingTaskConfig {
  id?: number;
  brokerType: BrokerType;
  destination: string;
  message: string;
  connectionProperties?: string;
  authType: MessagingAuthType;
  username?: string;
  password?: string;
  deliveryMode: DeliveryMode;
  timeoutSeconds?: number;
}