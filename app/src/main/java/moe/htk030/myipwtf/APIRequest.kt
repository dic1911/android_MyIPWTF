package moe.htk030.myipwtf

import android.app.Activity
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class APIRequestThread(activity: Activity, cb: (HashMap<String,String>) -> Unit) : Thread() {
    var callback: (HashMap<String,String>) -> Unit = cb
    var activity: MainActivity = activity as MainActivity

    override fun run() {
        var data: HashMap<String, String> = HashMap()
        val wtf = URL("https://wtfismyip.com/json")
        try {
            (wtf.openConnection() as? HttpsURLConnection)?.run {
                val reader = JsonReader(InputStreamReader(inputStream))
                reader.beginObject()
                while (reader.hasNext()) {
                    val field = reader.nextName()
                    try {
                        when (field) {
                            "YourFuckingTorExit" -> data[field] = reader.nextBoolean().toString()
                            else -> data[field] = reader.nextString()
                        }
                    } catch (e: Exception) {
                        Log.e("030-wtf", "error reading field $field")
                    }
                }
                callback(data)
            }
        } catch (e: Exception) {
            activity.errorOccurred.postValue(e.message)
        }
    }

}