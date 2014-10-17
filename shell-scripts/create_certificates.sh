cd ..
cd certificates
# Genereates a private key. It will ask you for a password. DON'T REMOVE THAT NEVER.
openssl genrsa -des3 -aes256 –out localhost.key 4096

# Then we will decrypt it to extract the public key. Maybe this is useless for you.
openssl rsa -in localhost.key -outform PEM -pubout -out localhost.pem

# Then we generate a certificate request. That is what usually you send to the certificate authority (CA) to sign it for you, to prove your identity.
openssl req -new -key localhost.key -out localhost.csr -config localhost.cnf

# Then we generate the certificate which you will use in your server.
openssl x509 -req -days 365 -in localhost.csr -signkey localhost.key -out localhost.crt