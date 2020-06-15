
module "database" {
  source = "../../modules/database"
  app_name = var.app_name
  environment = var.environment
  instance_class = var.instance_class
  cluster_size = var.cluster_size
}