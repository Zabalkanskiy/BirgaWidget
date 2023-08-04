package com.example.widgetbirga

import android.app.Person
import android.app.SearchManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widgetbirga.Recycler.RecyclerViewSecurite
import com.example.widgetbirga.databinding.StockWidgetConfigureBinding
import com.example.widgetbirga.helper.Securite
import com.example.widgetbirga.helper.WidgetJobService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

/**
 * The configuration screen for the [StockWidget] AppWidget.
 */
class StockWidgetConfigureActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
  //  private lateinit var appWidgetText: EditText
  //  private lateinit var appWidgetToken: EditText
    lateinit var appSecondUpdate: EditText
    private lateinit var appWidgetRecyclerView: RecyclerView
    lateinit var button: Button
    lateinit var clearButton: Button
   // lateinit var toolbar: android.widget.Toolbar
    lateinit var binding: StockWidgetConfigureBinding
    lateinit var  persons: List<Securite>
    //empty recyclerView
     var  recyclerView =  RecyclerViewSecurite(listOf())
    private var onClickListener = View.OnClickListener {
        val context = this@StockWidgetConfigureActivity

        // When the button is clicked, store the string locally
        //save title not work now
       // val widgetText = appWidgetText.text.toString()
       // saveTitlePref(context,  widgetText)
        //save Token now not work this is function create for finam site
       // val tokenText = appWidgetToken.text.toString()
     //   saveTokenPref(context, tokenText)


        //load data from internet and setting Job Service
        //unical number job ID
        //создаем jobScheduler для обновления виджета через каждую мнуту
        val sJobId = 1
        val componentName = ComponentName(context, WidgetJobService::class.java)
        val jobInfo = JobInfo.Builder(sJobId, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            // .setOverrideDeadline(TimeUnit.MINUTES.toMillis(1))
            //create and start now
           // .setMinimumLatency(60000)//60 second
            .setPersisted(true)
            //если нужно можно добавить бандл .setExtras(extraInf)
            .build()
        // val componentName = ComponentName(context,)
        //создаем новый jobScheduler для того чтобы он обновил данные через минуту
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        seveSecondUpdater(context = applicationContext, appSecondUpdate.text.toString().toLong())
        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
    private val clearListener = View.OnClickListener {
        clearPreferences(WidgetApplication.getAppContext())
        val jsonFileString = getJsonFromAsset(applicationContext, "stock.json")

        val gson = Gson()

        val listPersonType = object : TypeToken<List<Securite>>() {} .type
        persons = gson.fromJson(jsonFileString, listPersonType)
        recyclerView.filterList = persons.toMutableList()

        recyclerView.notifyDataSetChanged()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_search){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        val jsonFileString = getJsonFromAsset(applicationContext, "stock.json")

        val gson = Gson()
       // applicationContext.startActivities(Intent(MainActivity::c))

        val listPersonType = object : TypeToken<List<Securite>>() {} .type
         persons = gson.fromJson(jsonFileString, listPersonType)
         val loadPrice = loadPriceMap(WidgetApplication.getAppContext())
        if(loadPrice !=null){
          for (x in  persons){
              if ( loadPrice.keys.contains( x.secid)){
                 x.checked = true
              }
          }

        }
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.

        setResult(RESULT_CANCELED)


        binding = StockWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(binding.toolbar)

      //  appWidgetText = binding.appwidgetText
       // appWidgetToken = binding.appwidgetToken
        appSecondUpdate = binding.appwidgetNumber


        appWidgetRecyclerView = binding.appwidgetRecyclerView
        appWidgetRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView = RecyclerViewSecurite(persons)
        appWidgetRecyclerView.adapter = recyclerView
        button = binding.addButton
        button.setOnClickListener(onClickListener)

        clearButton = binding.clearButton
        clearButton.setOnClickListener(clearListener)



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

     //   appWidgetText.setText(loadTitlePref(this@StockWidgetConfigureActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val search: androidx.appcompat.widget.SearchView = menu?.findItem(R.id.action_search)?.actionView as androidx.appcompat.widget.SearchView
        val searchView = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView?

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.maxWidth = Int.MAX_VALUE
        //searchView?.imeOptions = ("en", "ru")
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
            //    recyclerView.getFilter().filter(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                recyclerView.filter.filter(p0)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }



}



const val PREFS_NAME = "com.example.newfinamwidget.StockWidget"
const val PREF_PREFIX_KEY = "appwidget_"
const val TOKEN ="TOKEN"
const val PAPER = "PAPER"
const val MAPPRICE = "MAPPRICE"
const val SECOND = "SECOND"


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

internal fun deleteTitlePref(context: Context) {
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

fun seveSecondUpdater(context: Context,  long: Long?){
    var sec: Long = 90
    if (long == null || long<90){ sec = 90} else { sec = long}
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putLong(SECOND, sec)
    prefs.apply()
}

fun loadSecondUpdater(context: Context): Long{
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val secondValue = prefs.getLong(SECOND, 90)
    return secondValue
}

fun clearPreferences(context: Context){
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val setString = prefs.getStringSet(PAPER, mutableSetOf())?.let { HashSet<String?>(it) }
    setString?.clear()
    context.getSharedPreferences(PREFS_NAME, 0).edit().putStringSet(PAPER, setString).apply()
}