package com.example;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

@SuppressWarnings("unused")
public class HttpsClientWithCert {

    public static void main(String[] args) throws Exception {
        // Percorso del keystore client
        String keyStorePath = "/percorso/del/keystore/clientkeystore.jks";
        String keyStorePassword = "passwordKeystore";

        // Percorso del truststore (opzionale, di solito cacerts)
        String trustStorePath = "/percorso/del/truststore/cacerts";
        String trustStorePassword = "passwordTruststore";

        // Carica il keystore client
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream keyStoreStream = new FileInputStream(keyStorePath)) {
            keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
        }

        // Carica il truststore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreStream = new FileInputStream(trustStorePath)) {
            trustStore.load(trustStoreStream, trustStorePassword.toCharArray());
        }

        // Inizializza il KeyManager per il certificato client
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        // Inizializza il TrustManager per il certificato server
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Inizializza il contesto SSL
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        // Configura l'HTTPS connection
        URL url = new URL("https://somehost.dk:3049");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());

        // Ottieni il risultato
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}
