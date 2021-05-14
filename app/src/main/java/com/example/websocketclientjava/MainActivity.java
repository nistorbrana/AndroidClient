package com.example.websocketclientjava;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {


    private WebSocketClient webSocketClient;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_sounds);
        createWebSocketClient();

    }

    public void playSoundFile(Integer fileName){
        if(mediaPlayer!=null) {

            if (mediaPlayer.isPlaying()) {

                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        mediaPlayer = MediaPlayer.create(this, fileName);
        mediaPlayer.setVolume(50,50);
        mediaPlayer.start();
    }

    private void createWebSocketClient(){
        URI uri;
        try{
            uri=new URI("ws://10.0.2.2:8080/websocket");
        }
        catch(URISyntaxException e){
            e.printStackTrace();
            return;
        }
        webSocketClient=new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket","Session is starting...");
                webSocketClient.send("Hello world!");
            }

            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket","Message received");
                final String s=message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TextView textView=findViewById(R.id.animalSound);
                            textView.setText(message);
                            switch (message) {
                                case "Wroar wroar":
                                    playSoundFile(R.raw.lionroar);
                                    break;
                                case "Woof woof":
                                    playSoundFile(R.raw.dogbarking);
                                    break;
                                case "Hee Haw":
                                    playSoundFile(R.raw.zebrabark);
                                    break;
                                case "Meow Meow":
                                    playSoundFile(R.raw.catmeow);
                                    break;
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket","Closed");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(15000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(View view){
        Log.i("WebSocket","Button was clicked");
        switch (view.getId()) {
            case (R.id.dogButton):
                webSocketClient.send("dog");
                break;
            case (R.id.catButton):
                webSocketClient.send("cat");
                break;
            case (R.id.lionButton):
                webSocketClient.send("lion");
                break;
            case (R.id.zebraButton):
                webSocketClient.send("zebra");
                break;
        }
    }

}