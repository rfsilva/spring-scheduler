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

// Interfaces
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