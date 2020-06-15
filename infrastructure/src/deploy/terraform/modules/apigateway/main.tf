
data "aws_vpc" "vpc" {
  tags = {
    Name = "${var.app_name}-${var.environment}"
  }
}

data "aws_subnet" "public_subnet_a" {
  vpc_id = data.aws_vpc.vpc.id
  ags = {
    Name = "${var.app_name}-${var.environment}-pub-a"
  }
}

data "aws_subnet" "public_subnet_b" {
  vpc_id = data.aws_vpc.vpc.id
  ags = {
    Name = "${var.app_name}-${var.environment}-pub-b"
  }
}

data "aws_security_group" "public_tls" {
  vpc_id = data.aws_vpc.vpc.id
  name = "${var.app_name}-${var.environment}-tls"
}

resource "aws_api_gateway_rest_api" "api" {
  name        = "${var.app_name}-${var.environment}"
  description = "Endpoint for internal incentive"
  binary_media_types = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "multipart/form-data"]
}

resource "aws_lb" "public_lb" {
  name               = "${var.app_name}-${var.environment}-public"
  internal           = true
  load_balancer_type = "network"
  subnets = [data.aws_subnet.public_subnet_a.id, data.aws_subnet.public_subnet_b.id]
  security_groups = [data.aws_security_group.public_tls.id]
}

resource "aws_api_gateway_vpc_link" "example" {
  name        = "${var.app_name}-${var.environment}-link"
  description = ""
  target_arns = [
    aws_lb.public_lb.arn]
}

