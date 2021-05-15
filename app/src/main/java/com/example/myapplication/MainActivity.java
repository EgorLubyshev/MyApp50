package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView =findViewById(R.id.textview);

        UserLoader userLoader = new UserLoader();
        userLoader.execute("http://10.67.172.157/EgorLubyshev/");

    }

    class UserLoader extends AsyncTask<String, Integer, Answer>{

        @Override
        protected Answer doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(strings[0])
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UserServers userServers = retrofit.create(UserServers.class);
            Call<Answer> call = userServers.getUsers(4);

            try {
                Response<Answer> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Answer answer) {
            if(answer!=null){
                textView.setText(answer.getUsers());
            }else {
                textView.setText("Ошибка");
            }


        }
    }

    class UserInsert extends AsyncTask<String, Integer, Answer>{

        @Override
        protected Answer doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(strings[0])
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UserServers userServers = retrofit.create(UserServers.class);
            Call<Answer> call = userServers.setUsers("new", "1212", "sdas.mail");

            try {
                Response<Answer> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Answer answer) {
            if(answer!=null){
                textView.setText(answer.getUsers());
            }else {
                textView.setText("Ошибка");
            }


        }
    }

    interface UserServers{
        @GET("get_user.php")
        Call<Answer> getUsers(
                @Query("id") int id
        );

        @GET("set_user.php")
        Call<Answer> setUsers(
                @Query("login") String login,
                @Query("password") String pass,
                @Query("mail") String mail
        );
    }

    class Answer{
        boolean status;
        ArrayList<User> data;
        String getUsers(){
            String res="";
            for (User user : data){
                res+= user.toString()+"\n";

            }
            return  res;
        }
    }
    class User{
        int id;
        String login, password,mail;

        @NonNull
        @Override
        public String toString() {
            return id+ ", "+login+", "+password+", "+mail;
        }
    }
}