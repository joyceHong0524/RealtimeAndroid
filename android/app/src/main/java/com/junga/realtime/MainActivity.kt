package com.junga.realtime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    val Tag = "MainActivity"
    lateinit var mSocket : Socket
    lateinit var username :String
    var users : Array<String> = arrayOf()

    // Event when it is connected to socket server successfully.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        try {
            //IO.socket returns a socket for url with default options.
            mSocket = IO.socket("http://10.0.2.2:3001")
        }catch(e : URISyntaxException){
            Log.e("MainActivity",e.reason)
        }


        var intent = intent;
        username = intent.getStringExtra("username")

        //Neeo to
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("newUser",onNewUser)
        mSocket.on("myMsg",onMyMessage)
        mSocket.on("newMsg",onNewMessage)
        mSocket.on("logout",onLogout)

        send.setOnClickListener {
            var chat = editText.text.toString()
            mSocket.emit("say",chat)
        }

        logout.setOnClickListener {

            mSocket.emit("logout")
        }


    }


    val onMyMessage = Emitter.Listener {
        Log.d("on","Mymessage has been triggered.")
        Log.d("mymsg : ",it[0].toString())
    }

    val onNewMessage = Emitter.Listener {
        Log.d("on","New message has been triggered.")
        Log.d("new msg : ",it[0].toString())
    }

    val onLogout = Emitter.Listener {
        Log.d("on","Logout has been triggered.")

        try {
           val jsonObj: JSONObject = it[0] as JSONObject
           Log.d("logout ",jsonObj.getString("disconnected"))
           Log.d("WHO IS ON NOW",jsonObj.getString("whoIsOn"))


            //Disconnect socket!
            mSocket.close()
       } catch (e : JSONException){
           e.printStackTrace()
       }
    }






    val onConnect : Emitter.Listener = Emitter.Listener {

        mSocket.emit("login",username)
        Log.d(Tag,"Socket is connected with ${username}")
    }

    val onMessageRecieved : Emitter.Listener = Emitter.Listener {
        try{
            val receivedData : Any = it[0]
            Log.d(Tag, receivedData.toString())

        }catch (e : Exception){
            Log.e(Tag,"error",e)
        }
    }


    val onNewUser : Emitter.Listener = Emitter.Listener {

        Log.d("onNewUser","ON NEW USER!!!!!")
        Log.d("a",it[0].toString())

        var data = it[0] //String 임


        if (data is String)
        {
            users = data.split(",").toTypedArray()
           for(a : String in users){
            Log.d("users",a)
           }
        }else{
            Log.d("error","Something went wrong")
        }
//        var jsonArray : JSONArray = it[0] as JSONArray
//
//        var bol = jsonArray.get(0) is String
//
//        Log.d("a",bol.toString())

//        if(it[0] is Array<*>){
//           users = it[0] as Array<String>
//       }
//        for( a : String in users){
//            Log.d("User list",a)
//        }


    }




    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}

