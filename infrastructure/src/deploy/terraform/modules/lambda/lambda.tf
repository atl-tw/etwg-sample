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


resource "aws_iam_role" "lambda_exec" {
  name = "${var.app_name}-${var.environment}-${var.lambda_name}-${var.lambda_version}"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "test_policy" {
  name = "test_policy"
  role = aws_iam_role.lambda_exec.id
  policy = var.lambda_policy_statement
}

resource "aws_lambda_function" "api" {
  function_name = "${var.app_name}-${var.environment}-${var.lambda_name}-${var.lambda_version}"
  handler = var.lambda_handler
  runtime = var.lambda_runtime
  filename = var.lambda_file
  source_code_hash = var.lambda_file_hash
  role = aws_iam_role.lambda_exec.arn
  publish = true

//  layers = [
//    aws_lambda_layer_version.lambda_layer_dependencies.arn]

  timeout = 120
  memory_size = 3008

  environment {
    variables = var.lambda_environment_variables
  }

  vpc_config {
    subnet_ids = var.lambda_subnets
    security_group_ids = var.lambda_security_groups
  }
}

output "lambda_invoke_arn" {
  value = aws_lambda_function.api.invoke_arn
}