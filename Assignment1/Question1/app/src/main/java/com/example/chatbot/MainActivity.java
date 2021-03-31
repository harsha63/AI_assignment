package com.example.chatbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    boolean firstMessage=false;
    String userId="First";
    String Uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        ChatMessage chatQuery= new ChatMessage("If you are new to this app then please write Register",false,false);
        mAdapter.add(chatQuery);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String Add = "";

//code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api= Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                try {
                    sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }
    private void sendMessage(String message) throws IOException {
        // yahi pe sab edits karne hai post and all that stuff and chatMessage.getContent() use karna hai sab cheej k liye
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA "+message);
        String GET_URL = "http://c7aebfd73735.ngrok.io";
        System.out.println("REACHED UNTIL GETURL");
        String temp=message.toLowerCase();
        if(temp.equals("register"))
        {
            ChatMessage chatMessage = new ChatMessage(message, true, false);
            mAdapter.add(chatMessage);
            mimicOtherMessage("Please enter your details separated by commas \n1). User id \n2). Name \n3). Phone No \n4). Email\n5). House Address\n6). Emergency Contact Name\n7). Phone Number of Emergency Contact");
            firstMessage=true;

            return ;
        }

        if(message.equals("yes") || message.equals("Yes"))
        {

            if(userId.length()>0)
            {

                System.out.println("BBBBBBBBBBBBBBBBBB"+userId);
                URL obj = new URL(GET_URL+"/Address?username="+userId);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                String place="";
                int responseCode = con.getResponseCode();
                System.out.println("AAAAAAAAAAAAAAA ET Response Code :: " + responseCode);
                String resp="Did not get response";
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    //BufferedReader in = new BufferedReader(new InputStreamReader(
                    //con.getInputStream()));
                    InputStream a = con.getInputStream();
                    String inputLine;
                    byte[] array = new byte[1000];
                    a.read(array);
                    inputLine = new String(array);
                    resp = inputLine;
                    place = resp;
                    System.out.println("AAAAAAAAAAAAAAABBBBBBBBB "+resp);

                    // print result
                    System.out.println(inputLine);
                } else {
                    System.out.println("GET request not worked");
                }
                String actual = "https://www.google.com/maps/search/?api=1&query="+place;
                gotoUrl(actual);
                return;
            }
            ChatMessage chatMessage = new ChatMessage(message, true, false);
            mAdapter.add(chatMessage);
            Post post=new Post(userId+"yes");

            /*Call<Post> call=jsonPlaceHolderApi.createPost(post);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(!response.isSuccessful()) {
                        ChatMessage chatMessage = new ChatMessage("Code:" + response.code(), true, false);
                        //mAdapter.add(chatMessage);
                        mimicOtherMessage(chatMessage.getContent());
                        return;
                    }
                    Post posts=response.body();
                    String content="";
                    //content+="Code "+response.code()+"\n";
                    //content+=posts.getName();
                    ChatMessage chatMessage = new ChatMessage("Opening google maps for you", true, false);
                    mimicOtherMessage(chatMessage.getContent());
                    gotoUrl(posts.getName());

                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    ChatMessage chatMessage = new ChatMessage("error", true, false);
                    //mAdapter.add(chatMessage);
                    mimicOtherMessage(chatMessage.getContent());
                }
            });
            return ;*/
            //return;
        }
        if(firstMessage==true)
        {
            ChatMessage chatMessage = new ChatMessage(message, true, false);
            mAdapter.add(chatMessage);
            //createPosts(jsonPlaceHolderApi,"newuser,"+message);
            for(int i=0;i<message.length();i++)
            {

                if(message.charAt(i)==',')
                {
                    break;
                }
                userId+=message.charAt(i);
                //mimicOtherMessage("inside");

            }
            URL obj = new URL(GET_URL+"/new?data="+message);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("AAAAAAAAAAAAAAA ET Response Code :: " + responseCode);
            String resp="Did not get response";
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                //BufferedReader in = new BufferedReader(new InputStreamReader(
                //con.getInputStream()));
                InputStream a = con.getInputStream();
                String inputLine;
                byte[] array = new byte[1000];
                a.read(array);
                inputLine = new String(array);
                resp = inputLine;
                System.out.println("AAAAAAAAAAAAAAA "+resp);

                // print result
                System.out.println(inputLine);
            } else {
                System.out.println("GET request not worked");
            }
            //int status=create_file(userId+".txt");
            //writeInFile(userId+".txt",userId);
            //int status1=create_file(userId+"database.txt");
            //save("user_id",userId,false);
            //load(userId);

            mimicOtherMessage(resp);

            userId+='\n';
            SharedPreferences shrd=getSharedPreferences("demo",MODE_PRIVATE);
            SharedPreferences.Editor editor=shrd.edit();
            editor.putString("user",userId);
            editor.apply();
            firstMessage=false;
            return;
        }


        URL obj = new URL(GET_URL+"/main?message="+message);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("AAAAAAAAAAAAAAA ET Response Code :: " + responseCode);
        String resp="Did not get response";
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            //BufferedReader in = new BufferedReader(new InputStreamReader(
            //con.getInputStream()));
            InputStream a = con.getInputStream();
            String inputLine;
            byte[] array = new byte[1000];
            a.read(array);
            inputLine = new String(array);
            resp = inputLine;
            System.out.println("AAAAAAAAAAAAAAA "+resp);

            // print result
            System.out.println(inputLine);
        } else {
            System.out.println("GET request not worked");
        }
        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //mimicOtherMessage(chatMessage.getContent());
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:5000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi= retrofit.create(JsonPlaceHolderApi.class);*/


        /*
        if(temp.equals("login"))
        {
            //mimicOtherMessage("i am here");
            ChatMessage chatMessage = new ChatMessage(message, true, false);
            mAdapter.add(chatMessage);
            SharedPreferences getShared=getSharedPreferences("demo",MODE_PRIVATE);
            userId=getShared.getString("user","The user is not created yet");
            mimicOtherMessage("Loading all the previous chats with "+userId);
            //createPosts(jsonPlaceHolderApi,userId+"old user");

            Post post=new Post(userId+temp);

            Call<Post> call=jsonPlaceHolderApi.createPost(post);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(!response.isSuccessful()) {
                        ChatMessage chatMessage = new ChatMessage("Code:" + response.code(), true, false);
                        //mAdapter.add(chatMessage);
                        mimicOtherMessage(chatMessage.getContent());
                        return;
                    }
                    Post posts=response.body();
                    String content="";
                    //content+="Code "+response.code()+"\n";
                    content+=posts.getName();
                    String [] arrayOfMessages=content.split("\n");
                    int i=0;
                    for(String s:arrayOfMessages)
                    {
                        if(i%2==0)
                        {
                            ChatMessage chatMessage = new ChatMessage(s, true, false);
                            mAdapter.add(chatMessage);
                        }
                        else
                        {
                            mimicOtherMessage(s);
                        }
                        i++;
                    }
                    //ChatMessage chatMessage = new ChatMessage(content, true, false);
                    //mimicOtherMessage(chatMessage.getContent());

                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    ChatMessage chatMessage = new ChatMessage("error", true, false);
                    //mAdapter.add(chatMessage);
                    mimicOtherMessage(chatMessage.getContent());
                }
            });

            return ;
        }
*/






        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        mimicOtherMessage(resp);
        firstMessage = false;
        //createPosts(jsonPlaceHolderApi,userId+message);
        //getPosts(jsonPlaceHolderApi);

    }
    private void gotoUrl(String s)
    {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    private void save(String filename,String message,boolean bool)
    {
        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(new File(filename),bool);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos=openFileOutput(filename,MODE_PRIVATE);
            fos.write(message.getBytes());

            Toast.makeText(this,"saveTo "+getFilesDir()+"/"+filename,Toast.LENGTH_LONG).show();
            //mimicOtherMessage();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos!=null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void load(String filename)
    {
        FileInputStream fis=null;
        try {
            fis=openFileInput(filename);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text = null;
            while((text=br.readLine()) != null)
            {
                sb.append(text).append("\n");

            }
            mimicOtherMessage(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis!=null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                 }
            }
        }
    }
    private void writeInFile(String name,String message)
    {
        try
        {
            FileOutputStream fout=new FileOutputStream("test.txt");
            String s="Welcome to java tut";
            byte b[]=s.getBytes();
            fout.write(b);
            fout.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
    private int create_file(String name)
    {
        try
        {
            File myObj=new File(name);
            if(myObj.createNewFile())
            {
                System.out.println("File is created sucessfully and stored here ");

                return 1;
            }
            else
            {
                mimicOtherMessage("file is created");
                return 0;
            }
        }
        catch (IOException e)
        {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        return -1;
    }
    private void createPosts(JsonPlaceHolderApi jsonPlaceHolderApi,String chatMessage)
    {
        //Post post=new Post(1,"hi","hi");
        Post post=new Post(chatMessage);

        Call<Post> call=jsonPlaceHolderApi.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()) {
                    ChatMessage chatMessage = new ChatMessage("The server is busy please try after some time", true, false);
                    //mAdapter.add(chatMessage);
                    mimicOtherMessage(chatMessage.getContent());
                    return;
                }
                Post posts=response.body();
                String content="";
                //content+="Code "+response.code()+"\n";
                content+=posts.getName();
                ChatMessage chatMessage = new ChatMessage(content, true, false);
                mimicOtherMessage(chatMessage.getContent());

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                ChatMessage chatMessage = new ChatMessage("The server is busy please try after some time", true, false);
                //mAdapter.add(chatMessage);
                mimicOtherMessage(chatMessage.getContent());
            }
        });


    }
    private void getPosts(JsonPlaceHolderApi jsonPlaceHolderApi)
    {
        Call<List<Post>> call=jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                if(!response.isSuccessful())
                {
                    ChatMessage chatMessage = new ChatMessage("Code:"+response.code(), true, false);
                    //mAdapter.add(chatMessage);
                    mimicOtherMessage(chatMessage.getContent());
                    return ;
                }
                List<Post> posts=response.body();
                for (Post post:posts) {
                    String content = "";
                    content += post.getName();
                    ChatMessage chatMessage = new ChatMessage(content, true, false);
                    //mAdapter.add(chatMessage);
                    mimicOtherMessage(chatMessage.getContent());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                ChatMessage chatMessage = new ChatMessage("error", true, false);
                //mAdapter.add(chatMessage);
                mimicOtherMessage(chatMessage.getContent());
            }
        });

    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }
}