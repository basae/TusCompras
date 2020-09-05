package tucodigo.tuscompras.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import tucodigo.tuscompras.Models.User;

public class ServiceApi {
    public JSONObject Post(String Url, String requestObject) throws IOException
    {
        JSONObject result=new JSONObject();
        InputStream is = null;
        String ContentResponse="";

        try {


            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.connect();
            OutputStream sendString = connection.getOutputStream();
            BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(sendString, "UTF-8"));
            Gson parse=new Gson();
            String Test=new Gson().toJson(requestObject);
            sender.write(requestObject.toString());
            sender.flush();
            sender.close();
            sendString.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {


                String line = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    ContentResponse += line;
                }
                try {
                    result = new JSONObject(ContentResponse);
                }
                catch (JSONException ex)
                {
                    throw  new IOException(ex.getMessage());
                }
            }
            else {
                String line, error = "";
                is = connection.getInputStream();
                throw new IOException(connection.getResponseMessage());
            }
        }
        catch (Exception ex)
        {
            throw  new IOException(ex.getMessage());
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
        return result;
    }
    public JSONObject Post(String Url, JSONObject requestObject) throws IOException
    {
        JSONObject result=new JSONObject();
        InputStream is = null;
        String ContentResponse="";

        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.setRequestProperty("Accept-Language", "en-US, en; q=0.9, es; q=0.8");

            OutputStream sendString = connection.getOutputStream();
            BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(sendString, "UTF-8"));
            Gson parse=new Gson();
            String Test=requestObject.toString();
            sender.write(Test);
            sender.flush();
            sender.close();
            sendString.close();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {


                String line = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    ContentResponse += line;
                }
                try {
                    result = new JSONObject(ContentResponse);
                }
                catch (JSONException ex)
                {
                    throw  new IOException(ex.getMessage());
                }
            }
            else {
                String line, error = "";
                is = connection.getInputStream();
                throw new IOException(connection.getResponseMessage());
            }
        }
        catch (Exception ex)
        {
            throw  new IOException(ex.getMessage());
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
        return result;
    }
    public String Get(String Url) throws IOException
    {
        String result="";
        InputStream is = null;
        String ContentResponse="";

        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");
            //connection.setChunkedStreamingMode(0);

            if(User.UserInfo!=null && User.UserInfo.getToken()!=null && User.UserInfo.getToken()!="")
            {
                connection.setRequestProperty("Authorization",User.UserInfo.getToken());
            }
            //connection.setDoInput(true);
            //connection.setDoOutput(true);
            /*
            OutputStream sendString = connection.getOutputStream();
            BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(sendString, "UTF-8"));
            sender.flush();
            sender.close();
            sendString.close();
            connection.connect();
            */
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {


                String line = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    ContentResponse += line;
                }
                try {
                    result = ContentResponse;
                }
                catch (Exception ex)
                {
                    throw  new IOException(ex.getMessage());
                }
            }
            else {
                String line, error = "";
                is = connection.getInputStream();
                throw new IOException(connection.getResponseMessage());
            }
        }
        catch (Exception ex)
        {
            throw  new IOException(ex.getMessage());
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
        return result;
    }
}