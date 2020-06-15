data "aws_region" "current" {}

resource "aws_vpc" "main" {
  cidr_block = var.cidr_block
  tags = {
    Name = "${var.app_name}-${var.environment}"
  }
}

resource "aws_subnet" "pub-a" {
  vpc_id = aws_vpc.main.id
  cidr_block = var.cidr_block_a
  availability_zone = "${data.aws_region.current.name}a"
  tags = {
    Name = "${var.app_name}-${var.environment}-pub-a"
  }
}

resource "aws_subnet" "pub-b" {
  vpc_id = aws_vpc.main.id
  cidr_block = var.cidr_block_b
  availability_zone = "${data.aws_region.current.name}a"
  tags = {
    Name = "${var.app_name}-${var.environment}-pub-b"
  }
}

resource "aws_vpc_ipv4_cidr_block_association" "private_cidr" {
  vpc_id = aws_vpc.main.id
  cidr_block = "172.2.0.0/16"
}

resource "aws_subnet" "private-a" {
  vpc_id = aws_vpc_ipv4_cidr_block_association.private_cidr.vpc_id
  cidr_block = "172.2.0.0/17"
  availability_zone = "${data.aws_region.current.name}a"
  tags = {
    Name = "${var.app_name}-${var.environment}-priv-a"
  }
}

resource "aws_subnet" "private-b" {
  vpc_id = aws_vpc_ipv4_cidr_block_association.private_cidr.vpc_id
  cidr_block = "172.128.0.0/17"
  availability_zone = "${data.aws_region.current.name}b"
  tags = {
    Name = "${var.app_name}-${var.environment}-priv-b"
  }
}
