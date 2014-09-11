cd ..
cd certificates
openssl genrsa -des3 -aes256 –out localhost-private.pem 4096
openssl rsa -in localhost-private.pem -outform PEM -pubout -out localhost-public.pem
openssl req -new -key localhost-private.pem -out localhost.csr -config localhost.cnf
openssl x509 -req -days 365 -in localhost.csr -signkey localhost-private.pem -out localhost.crt