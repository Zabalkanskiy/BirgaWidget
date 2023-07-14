package com.example.newfinamwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newfinamwidget.Recycler.RecyclerViewSecurite
import com.example.newfinamwidget.databinding.StockWidgetConfigureBinding
import com.example.newfinamwidget.helper.Securite
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

/**
 * The configuration screen for the [StockWidget] AppWidget.
 */
class StockWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private lateinit var appWidgetToken: EditText
    private lateinit var appWidgetRecyclerView: RecyclerView
    private var onClickListener = View.OnClickListener {
        val context = this@StockWidgetConfigureActivity

        // When the button is clicked, store the string locally
        val widgetText = appWidgetText.text.toString()
        saveTitlePref(context,  widgetText)
        val tokenText = appWidgetToken.text.toString()
        saveTokenPref(context, tokenText)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
    private lateinit var binding: StockWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        val jsonFileString = getJsonFromAsset(applicationContext, "stock.json")

        val gson = Gson()

        val listPersonType = object : TypeToken<List<Securite>>() {} .type
        val persons: List<Securite> = gson.fromJson(jsonFileString, listPersonType)
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.

        setResult(RESULT_CANCELED)

        binding = StockWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appWidgetText = binding.appwidgetText as EditText
        appWidgetToken = binding.appwidgetToken
        appWidgetRecyclerView = binding.appwidgetRecyclerView
        appWidgetRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val recyclerView = RecyclerViewSecurite(persons)
        appWidgetRecyclerView.adapter = recyclerView
        binding.addButton.setOnClickListener(onClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        appWidgetText.setText(loadTitlePref(this@StockWidgetConfigureActivity))
    }

}

const val PREFS_NAME = "com.example.newfinamwidget.StockWidget"
const val PREF_PREFIX_KEY = "appwidget_"
const val TOKEN ="TOKEN"
const val PAPER = "PAPER"
const val MAPPRICE = "MAPPRICE"


// Write the prefix to the SharedPreferences object for this widget
fun saveTitlePref(context: Context, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY, text)
    prefs.apply()
}
fun saveTokenPref(context: Context, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + TOKEN, text)
    prefs.apply()
}

fun saveListPaper(context: Context, text: String){
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val setString = prefs.getStringSet(PAPER, mutableSetOf())?.let { HashSet<String?>(it) }
    setString?.add(text)
    context.getSharedPreferences(PREFS_NAME, 0).edit().putStringSet(PAPER, setString).apply()
}

fun deletListPaper(context: Context, text: String){
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val setString = prefs.getStringSet(PAPER, mutableSetOf())?.let { HashSet<String?>(it) }
    setString?.remove(text)
    context.getSharedPreferences(PREFS_NAME, 0).edit().putStringSet(PAPER, setString).apply()
}

fun loadListPaper(context: Context):Set<String>?{
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val set = prefs.getStringSet(PAPER, null)
    return set
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
fun loadTitlePref(context: Context): String? {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY, null)
    return titleValue
}

fun loadTokenPref(context: Context): String?{
    val pref = context.getSharedPreferences(PREFS_NAME, 0)
    val tokenValue = pref.getString(PREF_PREFIX_KEY + TOKEN, null)
    return tokenValue
}

internal fun deleteTitlePref(context: Context,) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY)
    prefs.apply()
}

fun savePriceMap(context: Context, text: String){
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val mapString = prefs.edit().putString(MAPPRICE, text).apply()
}

fun loadPriceMap(context: Context): HashMap<String, Double>?{
    val gson = Gson()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val mapKey = mutableMapOf<String, Double>()
    mapKey.put("No stock", "0".toDouble())
    //val gson = Gson()
    val hashMapString = gson.toJson(mapKey)
    val map = prefs.getString(MAPPRICE, hashMapString)
    val type: Type = object : TypeToken<HashMap<String, Double>>(){}.type
    val hashMap: HashMap<String, Double> =  gson.fromJson(map, type)
    return hashMap
}

fun getJsonFromAsset(context: Context, fileName: String): String?{
    val jsonString :String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException){
        ioException.printStackTrace()
        return null
    }
    return jsonString
}