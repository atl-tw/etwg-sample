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

data "aws_security_group" "private" {
  vpc_id = data.aws_vpc.vpc.id
  name = "${var.app_name}-${var.environment}-private"
}


data "aws_security_group" "tls" {
  vpc_id = data.aws_vpc.vpc.id
  name = "${var.app_name}-${var.environment}-tls"
}

data "aws_api_gateway_vpc_link" "link" {
  name = "${var.app_name}-${var.environment}-link"
}
