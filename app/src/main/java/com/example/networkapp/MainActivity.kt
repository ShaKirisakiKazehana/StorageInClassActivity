package com.example.networkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

        loadComic()

    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
        saveComic(
            comicObject.getString("num"),
            comicObject.getString("title"),
            comicObject.getString("alt"),
            comicObject.getString("img")
        )
    }
    private val sharedPreferencesKey = "comic_info"

    private fun saveComic(comicId: String, title: String, description: String, imageUrl: String) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("comicId", comicId)
        editor.putString("title", title)
        editor.putString("description", description)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    private fun loadComic() {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, MODE_PRIVATE)
        val comicId = sharedPreferences.getString("comicId", "")
        val title = sharedPreferences.getString("title", "")
        val description = sharedPreferences.getString("description", "")
        val imageUrl = sharedPreferences.getString("imageUrl", "")

        if (comicId!!.isNotEmpty()) {
            // Display the saved comic information
            numberEditText.setText(comicId)
            titleTextView.text = title
            descriptionTextView.text = description
            Picasso.get().load(imageUrl).into(comicImageView)
        }
    }



}