package ru.andrey.VkEducation.controllers.Proxy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

            if (response.code() == 200) {
                JSONObject jsonObject = new JSONObject(responseString);
                jsonObject.put("id", Integer.parseInt(id));
                responseString = jsonObject.toString();

                jedis.set(jedisKey, responseString);
                cacheTimer.keyCreatedOrAccessed(jedisKey);
            }

            return responseString;
        }
    }


    public String forwardRequestWithBodyPost(String url, String entity, String id, String requestBody) throws IOException {

//        String id = "11"; //"11" - возвращается из json

        Request request = new Request.Builder()
                .url(domain + url)
                .post(okhttp3.RequestBody.create(requestBody, null))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == 201) {
                updateAll(entity + "s", id);

                JSONObject jsonObject = new JSONObject(requestBody);
                jsonObject.put("id", Integer.parseInt(id));
                requestBody = jsonObject.toString();

                jedis.set(entity + id, requestBody);


            }
            return requestBody;
        }
    }

    public String forwardRequestWithBodyPut(String url, String entity, String id, String requestBody) throws IOException {
        String jedisKey = entity + id;

        Request request = new Request.Builder()
                .url(domain + url)
                .put(okhttp3.RequestBody.create(requestBody, null))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == 200) {

                if (!jedis.exists(jedisKey)) forwardRequestWithIdGet(url, entity, id);
                if (jedis.get(jedisKey).isEmpty()) throw new RuntimeException("The object has been deleted");
                JSONObject userObject = new JSONObject(jedis.get(jedisKey));
                JSONObject updatedObject = new JSONObject(requestBody);

                for (String key : updatedObject.keySet()) {
                    if (updatedObject.get(key) != null) userObject.put(key, updatedObject.get(key));
                }

                jedis.set(jedisKey, userObject.toString());
                updateAll(entity + "s", id);

                return userObject.toString();
            }

            return null;
        }
    }

    public String forwardRequestWithBodyDelete(String url, String entity, String id) throws IOException {
        String jedisKey = entity + id;

        Request request = new Request.Builder()
                .url(domain + url)
                .delete(okhttp3.RequestBody.create("", null))
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

        Boolean flag = true;
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObject = usersArray.getJSONObject(i);

            if (!userObject.has("id")) continue;
            String userId = String.valueOf(userObject.get("id"));

            if (userId.equals(id)) {
                flag = false;
                String jedisKey = entity.substring(0, entity.length() - 1) + id;

                String jsonUpdated = jedis.get(jedisKey);

                if (jsonUpdated.isEmpty()) {
                    userObject.clear();
                } else {
                    JSONObject updatedObject = new JSONObject(jsonUpdated);

                    for (String key : updatedObject.keySet()) {
                        if (updatedObject.get(key) != null) userObject.put(key, updatedObject.get(key));
                    }
                }

                break;
            }
        }

        if (flag) {

            String jedisKey = entity.substring(0, entity.length() - 1) + id;

            String jsonUpdated = jedis.get(jedisKey);

            JSONObject updatedObject = new JSONObject(jsonUpdated);
            updatedObject.put("id", Integer.parseInt(id));

            usersArray.put(updatedObject);
        }

        String updatedUsersData = usersArray.toString();

        jedis.set(entity, updatedUsersData);
    }

}
