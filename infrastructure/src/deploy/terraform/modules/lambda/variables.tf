variable "environment" {
  description = "The environment the VPC is for."
}

variable "app_name" {
}

variable "lambda_name" {
}

variable "lambda_version" {
}

variable "lambda_handler" {

}

variable "lambda_file" {

}

variable "lambda_file_hash" {

}

variable "lambda_runtime" {
  default = ""
}

variable "lambda_policy_statement" {
}

variable "lambda_environment_variables" {}

variable "lambda_timeout" {
  type = number
}

variable "lambda_memory" {
  type = number
}

variable "lambda_path_prefix" {
  type = number
}

variable "lambda_security_groups" {
  type = set(string)
}

variable "lambda_subnets" {
  type = set(string)
}