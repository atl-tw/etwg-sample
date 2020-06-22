
data "aws_iam_policy_document" "policy_statement" {
  statement {
    sid = "default_lambda_requirements"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents",
      "ec2:CreateNetworkInterface",
      "ec2:DescribeNetworkInterfaces",
      "ec2:DeleteNetworkInterface"
    ],
    effect = "Allow",
    resources = ["*"]
  }
}
module "lambda_service" {
  source = "../../modules/lambda"
  environment = var.environment
  app_name = var.app_name
  lambda_name = "lambda_service"
  lambda_environment_variables = {
    ENVIRONMENT: var.environment
    APP_NAME: var.app_name
  }
  lambda_path_prefix = "/lambda-service"
  lambda_memory = 300
  lambda_timeout = 120
  lambda_version = var.lambda_service
  lambda_handler = "com.thoughtworks.sample.Handler"
  lambda_runtime = "java11"
  lambda_file = "${local.files_root}/lambda-service-${var.lambda_service}.jar"
  lambda_file_hash = "${local.files_root}/lambda-service-${var.lambda_service}.sha256"
  //Because this lambda talks to the api gateway and the database, we have to dual home it on both
  //the private and public sets of subnets
  lambda_security_groups = [data.aws_security_group.private.id, data.aws_security_group.tls.id]
  lambda_subnets = [data.aws_subnet.public_subnet_b.id, data.aws_subnet.public_subnet_a.id,
    data.aws_subnet.private_subnet_a.id, data.aws_subnet.private_subnet_b]
  lambda_policy_statement = data.aws_iam_policy_document.policy_statement.json
}

// We configure our api_gateway_methods here so they don't pollute the lambda module
resource "aws_api_gateway_method" "any" {
  rest_api_id   = module.lambda_service.api_gateway_id
  resource_id   = module.lambda_service.proxy_resource_id
  http_method   = "ANY"
  authorization = "NONE"
  request_parameters = {
    "method.request.path.proxy" = true
  }
}

resource "aws_api_gateway_integration" "lambda_root" {
  rest_api_id = module.lambda_service.api_gateway_id
  resource_id = module.lambda_service.proxy_resource_id
  http_method = aws_api_gateway_method.any.http_method
  connection_id = data.aws_api_gateway_vpc_link.link.id
  integration_http_method = "ANY"
  type = "VPC_LINK"
  uri = module.lambda_service.lambda_invoke_arn
}


resource "aws_api_gateway_deployment" "deployment" {
  depends_on = [
    aws_api_gateway_integration.lambda_root
  ]
  rest_api_id = module.lambda_service.api_gateway_id
  stage_name = var.master_version
}
