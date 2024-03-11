package ru.andrey.VkEducation.controllers.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProxyService {
    private final OkHttpClient client = new OkHttpClient();
    private final String domen = "https://jsonplaceholder.typicode.com";

    // Метод для перенаправления GET запросов
    public String forwardRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(domen + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // Метод для перенаправления POST, PUT и DELETE запросов с телом
    public String forwardRequestWithBody(String url, String method, String requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(domen + url)
                .method(method, okhttp3.RequestBody.create(null, requestBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
