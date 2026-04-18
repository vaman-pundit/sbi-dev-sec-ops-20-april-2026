"""
CKV_SBI_001 — Custom Checkov Policy
Ensure RDS instance uses Multi-AZ (RBI BCM requirement)

RBI IT Framework — Business Continuity Management (Annex 7):
  Financial institutions must ensure database high availability
  through multi-AZ deployment for production workloads.

Usage:
  checkov -d terraform/lms --external-checks-dir custom_policies
"""
from checkov.common.models.enums import CheckResult, CheckCategories
from checkov.terraform.checks.resource.base_resource_check import BaseResourceCheck


class DBMultiAZCheck(BaseResourceCheck):

    def __init__(self):
        name = "Ensure RDS instance uses Multi-AZ (RBI BCM requirement)"
        id   = "CKV_SBI_001"
        categories    = [CheckCategories.BACKUP_AND_RECOVERY]
        supported_resources = ["aws_db_instance"]
        super().__init__(
            name=name,
            id=id,
            categories=categories,
            supported_resources=supported_resources
        )

    def scan_resource_conf(self, conf):
        multi_az = conf.get("multi_az", [False])
        # Terraform parser wraps values in lists
        value = multi_az[0] if isinstance(multi_az, list) else multi_az
        if value is True:
            return CheckResult.PASSED
        return CheckResult.FAILED


check = DBMultiAZCheck()
