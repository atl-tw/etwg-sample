data "aws_region" "current" {}

data "aws_vpc" "vpc" {
  tags = {
    Name = "${var.app_name}-${var.environment}"
  }
}

data "aws_subnet" "private_subnet_a" {
  vpc_id = data.aws_vpc.vpc.id
  ags = {
    Name = "${var.app_name}-${var.environment}-priv-a"
  }
}

data "aws_subnet" "private_subnet_b" {
  vpc_id = data.aws_vpc.vpc.id
  ags = {
    Name = "${var.app_name}-${var.environment}-priv-b"
  }
}

data "aws_security_group" "private" {
  vpc_id = data.aws_vpc.vpc.id
  name = "${var.app_name}-${var.environment}-private"
}


resource "random_password" "username" {
  length = 8
  special = false
}

resource "random_password" "password" {
  length = 16
  special = true
  override_special = "_%@"
}

resource "aws_rds_cluster" "default" {
  cluster_identifier = "${var.app_name}-${var.environment}"
  availability_zones = [
    "${data.aws_region.current.name}a",
    "${data.aws_region.current.name}b"]
  publicly_accessible = false
  database_name = "${var.app_name}-${var.environment}"
  master_username = random_password.username.result
  master_password = random_password.password.result
  vpc_security_group_ids = [data.aws_security_group.private.id]
}

resource "aws_rds_cluster_instance" "cluster_instances" {
  count = var.cluster_size
  identifier = "${var.app_name}-${var.environment}-${count.index}"
  cluster_identifier = aws_rds_cluster.default.id
  instance_class = var.instance_class
}

resource "aws_secretsmanager_secret" "db_connect" {
  name = "${var.app_name}-${var.environment}-db"
}

resource "aws_secretsmanager_secret_version" "db_connect_version" {
  secret_id = aws_secretsmanager_secret.db_connect.id
  secret_string = <<EOF
{
  "db_host": "${aws_rds_cluster.default.endpoint}",
  "db_user": "${aws_rds_cluster.default.master_username}",
  "db_pass": "${aws_rds_cluster.default.master_password}",
  "db_name": "${aws_rds_cluster.default.database_name}"
}
EOF
}

