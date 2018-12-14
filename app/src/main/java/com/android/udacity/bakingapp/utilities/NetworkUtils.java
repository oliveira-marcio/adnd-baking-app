package com.android.udacity.bakingapp.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilitários para estabelecer a conexão com a rede.
 */
public class NetworkUtils {

    final static String RECIPES_BASE_URL =
            "http://go.udacity.com/android-baking-app-json";

    private NetworkUtils() {
    }

    /**
     * Contrói a URL para consultar as receitas
     */
    public static URL buildUrl() {
        URL url = null;
        try {
            url = new URL(RECIPES_BASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Retorna a string JSON com o resultado das receitas. É feita uma checagem antes na URL
     * fornecida pela rubrica do projeto para verificar se a mesma não está redirecionando para
     * outra URL. Caso esteja, passa a utilizar esta nova URL.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(false);

        boolean redirect = false;

        int status = urlConnection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }

        if (redirect) {
            try {
                URL newUrl = new URL(urlConnection.getHeaderField("Location"));
                urlConnection = (HttpURLConnection) newUrl.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                urlConnection.disconnect();
                return null;
            }
        }

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
