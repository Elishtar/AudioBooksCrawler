package ru.aklyuev.crauler;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Set;

/**
 * Created by Elishtar on 19/07/16.
 */
public class CommonGraber implements Graber {

    private WebSite site;
    private Set<String> bookSet;
    private Set<String> newBookSet;

    public CommonGraber(WebSite site, Set<String> bookSet,
                        Set<String> newBookSet) throws MalformedURLException {
        this.site = site;
        this.bookSet = bookSet;
        this.newBookSet = newBookSet;
    }

    @Override
    public void grab() throws IOException, KeyManagementException, NoSuchAlgorithmException {

        URL url = new URL(site.getUrl());
        BufferedReader reader;
        if (url.getProtocol().equals("http")) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URLConnection connection = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }

        String bookName;
        while ((bookName = reader.readLine()) != null) {
            if (!bookName.contains("topic-title")) {
                if (bookName.contains(site.getStringMarker()) && bookSet.add(getBookName(bookName) + "\n")) {
                    newBookSet.add(getBookName(bookName) + "\n");
                    System.out.println(Thread.currentThread().getName() + "-1");
                }
            } else {
                bookName = reader.readLine();
                bookName = bookName.substring(bookName.indexOf(">") + 1);
                if (bookSet.add(getBookName(bookName) + "\n")) {
                    newBookSet.add(getBookName(bookName) + "\n");
                    System.out.println(Thread.currentThread().getName() + "-1");
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + "-2");
    }

    @Override
    public String getBookName(String bookName) {
        bookName = bookName.substring(bookName.indexOf(site.getFirstConstraint())
                + site.getFirstConstraint().length(), bookName.indexOf(site.getLastConstraint()));
        if (site.getCustomConstraint() != null) {
            bookName = bookName.substring(0, bookName.indexOf(site.getCustomConstraint()));
        }
        return bookName;
    }

    private void httpsCall(URL url) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URLConnection con = url.openConnection();
        Reader reader = new InputStreamReader(con.getInputStream());
    }

}
