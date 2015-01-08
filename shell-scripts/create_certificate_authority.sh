#!/bin/bash
#############################################################################################
## Copyright 2014-2015, Egatuts & Esaú García                                              ##
## Open-source project shared under MIT License (http://opensource.org/licenses/MIT)       ##
## View, fork, help, and report issues at https://github.com/egatuts/nxt-remote-controller ##
#############################################################################################

## Go to the Certificate Authority private directory (where are store our key-pairs).
cd ../certificate_authority/private

## Genereates an AES256 pass-phrase encrypted private key file.
## NEVER DELETE THIS FILE OR YOU WILL LOSE YOUR CERTIFICATE AUTHORITY FOREVER.
## AND IN CONSECUENCE ALL THE SIGNED CERTIFICATES.
openssl genrsa -aes256 –out EgaTrust.key 4096

## Decrypted version of the private key in order to use in automated
## environments where human intervention is impossible.
openssl rsa -in EgaTrust.key -outform PEM -pubout -out EgaTrust.pem

## We ensure the integrity of the both private keys (encrypted and decrypted)
## by setting the file permissions to read-only to all users and groups.
chmod 0400 ./*

## Generates the certificate request of our Certificate Authority (EgaTrust.csr)
## using our private encrypted key (EgaTrust.key) and
## our custom openssl config file (EgaTrust.cnf).
openssl req -new -key EgaTrust.key -out ../EgaTrust.csr -config ../EgaTrust.cnf

## Generates our Certificate Authority with a date of expiry of ~10 years
## using our private encrypted key (EgaTrust.key) and
## our own certificate request (EgaTrust.csr).
openssl x509 -trustout -days 3650 -extensions v3_ca -signkey EgaTrust.key -req -in ../EgaTrust.csr -out ../EgaTrust.crt

## Go back to our Certificate Authority directory.
cd ..

## Exports the signed certificate to binary form (DER) to make it more compatible with Android, etc.
openssl x509 -in EgaTrust.crt -outform DER -out EgaTrust-DER.crt