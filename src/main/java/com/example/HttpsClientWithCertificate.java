package com.example;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

public class HttpsClientWithCertificate {
    public static void main(String[] args) {
        try {
            // Impostazioni del keystore
            String keystorePath = "mykeystore.jks"; // Percorso al file del keystore
            char[] keystorePassword = "password".toCharArray(); // Password del keystore

            // Carica il keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keystorePath), keystorePassword);

            // Inizializza il KeyManagerFactory con il keystore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword);

            // Crea e inizializza SSLContext per TLS
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            // Crea la SSLSocketFactory dal SSLContext
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Crea l'URL e apri la connessione HTTPS
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslSocketFactory);

            // Ottieni e stampa il contenuto dalla connessione
            InputStream inputStream = conn.getInputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                System.out.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
