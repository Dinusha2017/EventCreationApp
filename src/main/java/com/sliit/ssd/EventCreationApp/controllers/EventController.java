package com.sliit.ssd.EventCreationApp.controllers;

import com.sliit.ssd.EventCreationApp.models.Job;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class EventController {

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${access_token_endpoint}")
    private String accessTokenEndpoint;

    private String accessToken;

    private HttpClient httpclient;
    private HttpPost httppost;
    private HttpResponse resp;
    private BufferedReader reader;
    private StringBuffer buffer = new StringBuffer();
    private JSONObject json;


    @GetMapping("/callback")
    public void callbackListener(HttpServletRequest request,
                                     HttpServletResponse response){
        String code = request.getParameter("code");

        httpclient = HttpClients.createDefault();
        httppost = new HttpPost(accessTokenEndpoint);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8081/callback"));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("Host", "www.googleapis.com");
            resp = httpclient.execute(httppost);

            reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                buffer.append(line);
            }

            json = new JSONObject(buffer.toString());
            accessToken = json.getString("access_token");
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e);
        }

        try {
            response.sendRedirect("index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addEvent")
    public String addEventToGoogleCalendar(Job eventDetails){
        httpclient = HttpClients.createDefault();
        httppost = new HttpPost("https://www.googleapis.com/calendar/v3/calendars/" +
                                    eventDetails.getCalendarId() + "/events?sendNotifications=True");

        JSONObject requestBody = new JSONObject();

        JSONObject startDateTimeBody = new JSONObject();
        JSONObject endDateTimeBody = new JSONObject();

        startDateTimeBody.put("dateTime", eventDetails.getStartDateTime());
        endDateTimeBody.put("dateTime", eventDetails.getEndDateTime());

        requestBody.put("start", startDateTimeBody);
        requestBody.put("end", endDateTimeBody);

        requestBody.put("summary", eventDetails.getTitle());
        requestBody.put("location", eventDetails.getLocation());
        requestBody.put("description", eventDetails.getDescription());

        try {
            String message = requestBody.toString();

            System.out.println("message request body string: ");
            System.out.println(message);

            httppost.setEntity(new StringEntity(message));
            httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Authorization", "Bearer " + accessToken);
            httppost.setHeader("Host", "www.googleapis.com");
            resp = httpclient.execute(httppost);

            reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            buffer.setLength(0);
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                buffer.append(line);
            }

            json = new JSONObject(buffer.toString());

            System.out.println(json);
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e);
        }

//        System.out.printf("Event created: %s\n", event.getHtmlLink());

        return "success";
    }
}
