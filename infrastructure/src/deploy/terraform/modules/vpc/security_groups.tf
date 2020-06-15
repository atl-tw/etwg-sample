resource "aws_security_group" "tls" {
  name        = "${var.app_name}-${var.environment}-tls"
  description = "Allow TLS inbound traffic"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "TLS to VPC"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.main.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name =  "${var.app_name}-${var.environment}-tls"
  }
}

resource "aws_security_group" "ssh" {
  name        = "${var.app_name}-${var.environment}-ssh"
  description = "Allow SSH inbound traffic"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "SSH to VPC"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.main.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name =  "${var.app_name}-${var.environment}-tls"
  }
}

resource "aws_security_group" "private" {
  name        = "${var.app_name}-${var.environment}-private"
  description = "Grant access on the private network"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = [aws_subnet.private-a.cidr_block, aws_subnet.private-b.cidr_block]
  }

  tags = {
    Name =  "${var.app_name}-${var.environment}-private"
  }
}

resource "aws_security_group" "public" {
  name        = "${var.app_name}-${var.environment}-public"
  description = "grand access on the public network, and egress"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = [aws_subnet.pub-a.cidr_block, aws_subnet.pub-b.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name =  "${var.app_name}-${var.environment}-private"
  }
}