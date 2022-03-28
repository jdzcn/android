package com.example.netapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnProduct = (Button) findViewById(R.id.product);
        Button btnWeather = (Button) findViewById(R.id.weather);
        responseText = (TextView) findViewById(R.id.response_text);
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithHttpURLConnection();
                //sendRequestWithOkHttp();
            }
        });
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendRequestWithHttpURLConnection();
                sendRequestWithOkHttp();
            }
        });

    }


    private void sendRequestWithHttpURLConnection() {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://172.96.193.223/product.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                    parseJSONWithGSON(response.toString());
                    //parseXMLWithPull(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }


    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.yiketianqi.com/free/day?appid=41633843&appsecret=If8zSUqK&unescape=1")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                    //parseJSONWithGSON(responseData);
                    parseJSONWithJSONObject(responseData);
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
//                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<Product> appList = gson.fromJson(jsonData, new TypeToken<List<Product>>() {}.getType());
        for (Product app : appList) {
            Log.d("MainActivity", "id is " + app.id);
            Log.d("MainActivity", "name is " + app.name);
            //Log.d("MainActivity", "version is " + app.getVersion());
        }
    }


    private void parseJSONWithJSONObject(String jsonData) {
        try {

                JSONObject jsonObject = new JSONObject(jsonData);
                String cityid = jsonObject.getString("cityid");
                String city = jsonObject.getString("city");
                String update_time = jsonObject.getString("update_time");
                String wea = jsonObject.getString("wea");
                String wea_img = jsonObject.getString("wea_img");
                String tem = jsonObject.getString("tem");
                String win = jsonObject.getString("win");
                String air = jsonObject.getString("air");
                String win_meter = jsonObject.getString("win_meter");
                String win_speed = jsonObject.getString("win_speed");
                String tem_night = jsonObject.getString("tem_night");
                String tem_day = jsonObject.getString("tem_day");

                Log.d("MainActivity", "cityid is " + cityid);
                Log.d("MainActivity", "city is " + city);
                Log.d("MainActivity", "update_time is " + update_time);
                Log.d("MainActivity", "wea is " + wea);
                Log.d("MainActivity", "wea_img is " + wea_img);
                Log.d("MainActivity", "tem is " + tem);
                Log.d("MainActivity", "tem_day is " + tem_day);
                Log.d("MainActivity", "tem_night is " + tem_night);
                Log.d("MainActivity", "win is " + win);
                Log.d("MainActivity", "win_speed is " + win_speed);
                Log.d("MainActivity", "win_meter is " + win_meter);
                Log.d("MainActivity", "air is " + air);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("DATA_RECORD".equals(nodeName)) {
                            Log.d("MainActivity", "id is " + id);
                            Log.d("MainActivity", "name is " + name);

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}