#!/usr/bin/env sh
touch $KEY_PROPERTIES
echo keyAlias = $KEY_ALIAS > $KEY_PROPERTIES
echo keyPassword = $KEY_PASSWORD >> $KEY_PROPERTIES
echo storeFile = $STORE_FILE >> $KEY_PROPERTIES
echo storePassword = $STORE_PASSWORD >> $KEY_PROPERTIES

# convert jks to base64 command : $ openssl base64 -A -in .signing/release.jks 
# convert base64 to jks command : $ echo $KEYSTORE_BASE64 | base64 -d > .signing/release.jks
echo $ACCOUNTING_KEYSTORE_BASE64 | base64 -d > ../accounting.jks
