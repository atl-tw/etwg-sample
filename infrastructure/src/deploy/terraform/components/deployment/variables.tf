variable "environment" {
  description = "The environment the VPC is for."
}
variable "app_name" {
}
variable "lambda_service" {
  type = string
  description = "Version of the lambda service being deployed."
}
variable "master_version" {
  type = string
  description = "The master version of the environment across all service versions"
}
