package ru.aklyuev.crauler;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * Created by Elishtar on 19/07/16.
 */
public interface Graber {

    void grab() throws IOException, KeyManagementException, NoSuchAlgorithmException;

    String getBookName(String bookName);

}
