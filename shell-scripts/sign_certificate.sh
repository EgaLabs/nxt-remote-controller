###
# CA (meaning) = Certificate Authority. The business/organization which emits and signs others certificates.
# CA.key = 4096 bits long modulus key encrypted with AES 256.
# CA.pem = Decrypted version of CA.key.
# CA.crt = Normal certificate of your CA.
# CA-DER.crt = Exported version of the CA.crt with DER format which is compatible with Android. Normal CA doesn't work in some Android versions.
###

cd ..
cd certificate_authority

# We sign the certificate using the certificate request (replacing the old one) with the previous created CA.
openssl x509 -req -in ../certificates/localhost.csr -CA CA.crt -CAkey CA.key -CAcreateserial ../certificates/localhost.srl -out localhost.crt -days 365

# We export the signed certificate to make it compatible with Android.
openssl x509 -in localhost.crt -outform DER -out localhost-DER.crt