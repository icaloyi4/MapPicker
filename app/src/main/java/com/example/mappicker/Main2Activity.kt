package com.example.mappicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*


class Main2Activity : AppCompatActivity() {

    companion object{
        private var REQUEST_GET_MAP_LOCATION = 0;
    }
    // Add more, if you call different activities from Activity A


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn_map.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@Main2Activity, MainActivity::class.java)
                startActivityForResult(intent, REQUEST_GET_MAP_LOCATION);
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GET_MAP_LOCATION && resultCode == RESULT_OK) {
            if (data!=null) {
                val latitude = data.getDoubleExtra("latitude", 0.0)
                val longitude = data.getDoubleExtra("longitude", 0.0)
                txt_result.setText("latitude : ${latitude} \n longitude ${longitude}")
            }
            // do something with B's return values
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

}
