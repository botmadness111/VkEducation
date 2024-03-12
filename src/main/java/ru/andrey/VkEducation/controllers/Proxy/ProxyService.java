package ru.andrey.VkEducation.controllers.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import ru.andrey.VkEducation.inMemoryCash.CacheTimer;

import java.io.IOException;

@Service
public class ProxyService {
    private final OkHttpClient client = new OkHttpClient();
    private final Jedis jedis;
    private final CacheTimer cacheTimer;

    private final String domain = "https://jsonplaceholder.typicode.com";


    @Autowired
    public ProxyService(Jedis jedis, CacheTimer cacheTimer) {
        this.jedis = jedis;
        this.cacheTimer = cacheTimer;
    }

    public String forwardRequestGet(String url, String entity) throws IOException {
        String jedisKey = entity;

        if (jedis.exists(jedisKey)) {
            cacheTimer.keyCreatedOrAccessed(jedisKey);
            return jedis.get(jedisKey);
        }

        Request request = new Request.Builder()
                .url(domain + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = response.body().string();

            if (response.isSuccessful()) {
                jedis.set(jedisKey, responseString);
                cacheTimer.keyCreatedOrAccessed(jedisKey);
            }

            return responseString;
        }
    }


    public String forwardRequestWithIdGet(String url, String entity, String id) throws IOException {
        String jedisKey = entity + id;

        if (jedis.exists(jedisKey)) {
            cacheTimer.keyCreatedOrAccessed(jedisKey);
            return jedis.get(jedisKey);
        }


        Request request = new Request.Builder()
                .url(domain + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = response.body().string();

            if (response.isSuccessful()) {
                jedis.set(jedisKey, responseString);
                cacheTimer.keyCreatedOrAccessed(jedisKey);
            }

            return responseString;
        }
    }


    public String forwardRequestWithBodyPost(String url, String entity, String requestBody) throws IOException {

        Request request = new Request.Builder()
                .url(domain + url)
                .post(okhttp3.RequestBody.create(requestBody, null))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (response.code() == 201) {
                JSONObject jsonResponse = new JSONObject(responseBody);
                String id = String.valueOf(jsonResponse.get("id"));

                updateAll(entity + "s", id);
            }

            return responseBody;
        }
    }

    public String forwardRequestWithBodyPut(String url, String entity, String id, String requestBody) throws IOException {
        String jedisKey = entity + id;

        Request request = new Request.Builder()
                .url(domain + url)
                .put(okhttp3.RequestBody.create(requestBody, null))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String resposeString = response.body().string();

            if (response.isSuccessful()) {

                if (jedis.exists(jedisKey)){
                    jedis.set(jedisKey, requestBody);
                    updateAll(entity + "s", id);
                }
            }

            return resposeString;
        }
    }

    public String forwardRequestWithBodyDelete(String url, String entity, String id, String requestBody) throws IOException {
        String jedisKey = entity + id;

        Request request = new Request.Builder()
                .url(domain + url)
                .delete(okhttp3.RequestBody.create(requestBody, null))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String resposeString = response.body().string();

            if (response.isSuccessful()) {
                if (jedis.exists(jedisKey) && !jedis.get(jedisKey).isEmpty()) {
                    jedis.set(jedisKey, "");
                    updateAll(entity + "s", id);
                }
            }

            return resposeString;
        }
    }

    private void updateAll(String entity, String id) {

        String usersData = jedis.get(entity);

        if (usersData == null) return;

        JSONArray usersArray = new JSONArray(usersData);


        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObject = usersArray.getJSONObject(i);

            if (!userObject.has("id")) continue;
            String userId = String.valueOf(userObject.get("id"));

            if (userId.equals(id)) {
                String jedisKey = entity.substring(0, entity.length() - 1) + id;

                String jsonUpdated = jedis.get(jedisKey);

                if (jsonUpdated.isEmpty()) {
                    userObject.clear();
                } else {
                    JSONObject updatedObject = new JSONObject(jsonUpdated);

                    for (String key : updatedObject.keySet()) {
                        userObject.put(key, updatedObject.get(key));
                    }
                }

                break;
            }
        }

        String updatedUsersData = usersArray.toString();

        jedis.set(entity, updatedUsersData);
    }

}
