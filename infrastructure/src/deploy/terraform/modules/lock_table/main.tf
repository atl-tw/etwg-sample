
resource "aws_dynamodb_table" "terraform_state_lock" {
  name           = "${var.app_name}-${var.environment}-tf-lck"
  read_capacity  = 5
  write_capacity = 5
  hash_key       = "LockID"
  attribute {
    name = "LockID"
    type = "S"
  }
}