#!/bin/bash
#############################################################################################
## Copyright 2014-2015, Egatuts & Esaú García                                              ##
## Open-source project shared under MIT License (http://opensource.org/licenses/MIT)       ##
## View, fork, help, and report issues at https://github.com/egatuts/nxt-remote-controller ##
#############################################################################################

## Go to the certificates directory.
cd ../certificates

## Genereates an AES256 pass-phrase encrypted private key file.
## NEVER DELETE THIS FILE OR YOU WILL LOSE YOUR CERTIFICATE FOREVER.
openssl genrsa -aes256 –out localhost.key 4096

## Decrypted version od the private key in order to use in automated
## environments where human intervention is impossible.
openssl rsa -in localhost.key -outform PEM -pubout -out localhost.pem

## Certificate Request which needs to be signed by a Root/Intermediate Certificate Authority.
## All certificates signed by a trusted CA become trusted too. We will use our own self-signed CA.
openssl req -sha256 -new -key localhost.key -out localhost.csr -config localhost.cnf

## Self-signed certificate with a duration of ~1 year.
## It is not sigend by our CA so if you use this, it won't be trusted unless you add it manually.
openssl x509 -req -days 365 -in localhost.csr -signkey localhost.key -out localhost.crt

## Exports the signed certificate to binary form (DER) to make it more compatible with Android, etc.
openssl x509 -in localhost.crt -outform DER -out localhost-DER.crt