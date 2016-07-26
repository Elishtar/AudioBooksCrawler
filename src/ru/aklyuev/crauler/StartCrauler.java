package ru.aklyuev.crauler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Elishtar on 19/07/16.
 */
public class StartCrauler {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Crauler crauler = new Crauler(10);
        crauler.loadBooks();
    }
}
