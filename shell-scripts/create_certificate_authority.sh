###
# CA (meaning) = Certificate Authority. The business/organization which emits and signs others certificates.
# CA.key = 4096 bits long modulus key encrypted with AES 256.
# CA.pem = Decrypted version of CA.key.
# CA.crt = Normal certificate of your CA.
# CA-DER.crt = Exported version of the CA.crt with DER format which is compatible with Android. Normal CA doesn't work in some Android versions.
###

cd ..
cd certificate_authority

# Creates your Certificate Authority private key.
openssl genrsa -des3 -aes256 –out CA.key 4096

# Decrypt private key to sign other certificates with your CA.
openssl rsa -in CA.key -outform PEM -pubout -out CA.pem

# Generates the CA with 3650 days of duration (~10 years).
openssl req -x509 -new -nodes -key CA.key -days 365 -out CA.crt

# Exports the CA to DER format which is compatible with Android.
openssl x509 -in CA.crt -outform DER -out CA-DER.crt