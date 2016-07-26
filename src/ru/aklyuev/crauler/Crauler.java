package ru.aklyuev.crauler;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Elishtar on 25/07/16.
 */
public class Crauler {

    private Set<String> bookSet = new LinkedHashSet<>();
    private Set<String> newBookSet = new LinkedHashSet<>();
    private ExecutorService service;
    private File oldBooks =  new File("audioBooks.txt");
    private File newBooks =  new File("newAudioBooks.txt");
    private Properties properties = new Properties();
    private AtomicInteger count = new AtomicInteger(0);
    private int daily = 30;

    public Crauler() {
    }

    public Crauler(int daily) {
        this.daily = daily;
    }

    public void loadBooks() throws IOException, InterruptedException {

        while (true) {
            bookSet = new LinkedHashSet<>();
            newBookSet = new LinkedHashSet<>();
            service = Executors.newFixedThreadPool(4);
            readOldBooks();
            submitTask();
            PrintWriter writer = new PrintWriter(oldBooks);

            service.shutdown();
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            bookSet.forEach(bookName -> {
                writer.write(bookName);
            });

            writer.flush();
            writer.close();

            PrintWriter newWriter = new PrintWriter(newBooks);

            newBookSet.forEach(bookName -> {
                newWriter.write(bookName);
            });

            newWriter.flush();
            newWriter.close();

            Thread.sleep(daily*1000);
        }

    }

    private void submitTask() throws IOException {

        for (WebSite webSite : getWebSites()) {
            Future<?> asbookFantasticFuture = service.submit(() -> {
                try {
                    new CommonGraber(webSite, bookSet, newBookSet).grab();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    private List<WebSite> getWebSites() throws IOException {
        List<WebSite> webSites = new ArrayList<>();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("ru/aklyuev/crauler/websites.properties");
        properties.load(stream);

        for (String propName : properties.stringPropertyNames()) {

            String[] split = properties.getProperty(propName).split(",");

            WebSite webSite = new WebSite();
            webSite.setName(propName);
            webSite.setUrl(split[0]);
            webSite.setStringMarker(split[1]);
            webSite.setFirstConstraint(split[2]);
            webSite.setLastConstraint(split[3]);
            if (split.length == 5) {
                webSite.setCustomConstraint(split[4]);
            }

            webSites.add(webSite);

        }

        return webSites;
    }

    private void readOldBooks() throws IOException {

        if (oldBooks.exists()){
            String oldBook;
            BufferedReader reader = new BufferedReader(new FileReader(oldBooks));
            while ((oldBook = reader.readLine()) != null) {
                bookSet.add(oldBook + "\n");
            }
            reader.close();

        }
    }

}
