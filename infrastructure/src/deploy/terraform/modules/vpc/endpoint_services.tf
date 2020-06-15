
resource "aws_vpc_endpoint" "secretsmanager" {
  vpc_id       = aws_vpc.main.id
  service_name = "com.amazonaws.${data.aws_region.current.name}.secretsmanager"
}

resource "aws_vpc_endpoint" "cloudwatch" {
  vpc_id       = aws_vpc.main.id
  service_name = "com.amazonaws.${data.aws_region.current.name}.monitoring"
}
