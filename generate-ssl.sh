#!/bin/bash
# ============================================================================
# KATMALL - SSL Certificate Generator (Self-Signed for Development)
# Author: tai.buivan@outlook.com
# Usage: ./generate-ssl.sh
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SSL_DIR="$SCRIPT_DIR/docker/nginx/ssl"

echo "🔐 Generating self-signed SSL certificate for development..."

mkdir -p "$SSL_DIR"

# Create OpenSSL config file for SAN support
cat > "$SSL_DIR/openssl.cnf" << 'EOF'
[req]
default_bits = 2048
prompt = no
default_md = sha256
distinguished_name = dn
x509_extensions = v3_req

[dn]
C = VN
ST = HoChiMinh
L = HoChiMinh
O = KatMall
OU = Development
CN = localhost

[v3_req]
subjectAltName = @alt_names
basicConstraints = CA:FALSE
keyUsage = nonRepudiation, digitalSignature, keyEncipherment

[alt_names]
DNS.1 = localhost
DNS.2 = *.localhost
DNS.3 = katmall.local
IP.1 = 127.0.0.1
EOF

# Generate private key and certificate using config file
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout "$SSL_DIR/server.key" \
    -out "$SSL_DIR/server.crt" \
    -config "$SSL_DIR/openssl.cnf"

# Clean up config file
rm -f "$SSL_DIR/openssl.cnf"

# Set permissions
chmod 600 "$SSL_DIR/server.key"
chmod 644 "$SSL_DIR/server.crt"

echo "SSL certificate generated successfully!"
echo "   - Location: $SSL_DIR"
echo "   - Certificate: server.crt"
echo "   - Private Key: server.key"
echo ""
echo "Note: This is a self-signed certificate for DEVELOPMENT only."
echo "For production, use Let's Encrypt or a trusted CA."
