#############################################################################################
## Copyright 2014-2015, Egatuts & Esaú García                                              ##
## Open-source project shared under MIT License (http://opensource.org/licenses/MIT)       ##
## View, fork, help, and report issues at https://github.com/egatuts/nxt-remote-controller ##
#############################################################################################

## Go to the CA directory.
cd ../certificate_authority

## Sign the localhost.csr (Certificate Request) with
## EgaTrust.crt (Certificate Authority) using
## EgaTrust.key (Certificate Authority private encrypted key) and
## EgaTrust.cnf (Certificate Authority config file)
mkdir newcerts
echo $null >> index.txt
echo '01' >> serial.txt
openssl ca -config EgaTrust.cnf -policy signing_policy -extensions signing_req -keyfile private/EgaTrust.key -cert EgaTrust.crt -out ../certificates/localhost.crt -infiles ../certificates/localhost.csr

## Go back to the issued certificates folder.
cd ../certificates

## Exports the signed certificate to binary form (DER) to make it more compatible with Android, etc.
openssl x509 -in localhost.crt -outform DER -out localhost-DER.crt