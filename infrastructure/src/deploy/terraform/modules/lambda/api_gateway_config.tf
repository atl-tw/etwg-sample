data aws_api_gateway_rest_api "api" {
  name = "${var.app_name}-${var.environment}"
}

data aws_api_gateway_vpc_link "link" {
  name        = "${var.app_name}-${var.environment}-link"
}

resource  "aws_api_gateway_resource" "lambda_resource" {
  name = "${var.app_name}-${var.environment}-${var.lambda_name}"
  rest_api_id = data.aws_api_gateway_rest_api.api.id
  parent_id = data.aws_api_gateway_rest_api.api.root_resource_id
  path_part = var.lambda_path_prefix
}

resource  "aws_api_gateway_resource" "proxy_resource" {
  name = "${var.app_name}-${var.environment}-${var.lambda_name}"
  rest_api_id = data.aws_api_gateway_rest_api.api.id
  parent_id = aws_api_gateway_resource.lambda_resource.id
  path_part = "{proxy+}"
}

output "api_gateway_id" {
  value = data.aws_api_gateway_rest_api.api.id
}

output "proxy_resource_id" {
  value = aws_api_gateway_resource.proxy_resource.id
}

