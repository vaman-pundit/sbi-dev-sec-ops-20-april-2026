#!/bin/sh
# setup-hooks.sh — Run once after cloning the repo to install the pre-commit hook
#
# DevSecOps: This hook blocks any commit that contains a potential secret.
# It catches passwords, API keys, JWT tokens, and private keys BEFORE they
# reach the repository — where they would be impossible to fully remove.
#
# Usage:
#   chmod +x setup-hooks.sh
#   ./setup-hooks.sh

set -e

# Install detect-secrets if not present
if ! command -v detect-secrets >/dev/null 2>&1; then
  echo "[*] Installing detect-secrets..."
  pip install detect-secrets
fi

# Write the pre-commit hook
cat > .git/hooks/pre-commit << 'HOOK'
#!/bin/sh
# detect-secrets pre-commit hook
# Blocks commits containing secrets not in the approved .secrets.baseline

detect-secrets-hook --baseline .secrets.baseline
if [ $? -ne 0 ]; then
  echo ""
  echo "╔══════════════════════════════════════════════════════╗"
  echo "║  COMMIT BLOCKED — potential secret detected          ║"
  echo "║                                                        ║"
  echo "║  If this is a false positive, run:                   ║"
  echo "║    detect-secrets audit .secrets.baseline            ║"
  echo "║  and mark the finding as not a secret.               ║"
  echo "║                                                        ║"
  echo "║  Never commit real passwords, keys, or tokens.       ║"
  echo "╚══════════════════════════════════════════════════════╝"
  exit 1
fi
HOOK

chmod +x .git/hooks/pre-commit

echo "[✓] Pre-commit hook installed."
echo ""
echo "Test it:"
echo "  echo 'password=SuperSecret123' > test-secret.txt"
echo "  git add test-secret.txt && git commit -m 'test'"
echo "  # Expected: COMMIT BLOCKED"
echo "  rm test-secret.txt"
