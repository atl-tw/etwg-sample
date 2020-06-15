
module "vpc" {
  source = "../../modules/vpc"
  environment = var.environment
  cidr_block = var.cidr_block
  cidr_block_a = var.cidr_block_a
  cidr_block_b = var.cidr_block_b
  app_name = var.app_name
}

module "lock_table" {
  source = "../../modules/lock_table"
  environment = var.environment
  app_name = var.app_name
}