package se.desinata.androidapivecka2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import se.desinata.androidapivecka2.ui.theme.AndroidAPIVecka2Theme
import java.io.IOException
import androidx.compose.material3.Text as Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAPIVecka2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    ChuckJoke()
                }
            }
        }
    }
}

@Composable
fun ChuckJoke(modifier: Modifier=Modifier) {
        val client = OkHttpClient()
        var theJoke by remember { mutableStateOf(value = "joke") }
        var started by remember { mutableStateOf(value = false) }
    val ChuckURL = "https://api.chucknorris.io/jokes/random"

  fun LoadJoke(){
      val req = Request.Builder().url(ChuckURL).build()

      client.newCall(req).enqueue(object : Callback {
          override fun onFailure(call: Call, e: IOException){
          Log.i("APIDEBUG", "Could not fetch from API")
      }

          override fun onResponse(call: Call, response: Response) {
              response.use {
                  if (!response.isSuccessful) throw IOException("Unexpected code $response")

                  var responseString = response.body!!.string()

                  Log.i("APIDEBUG", responseString)

                  val jokeData = Json { ignoreUnknownKeys=true}.decodeFromString<ChuckJoke>(responseString)

                  theJoke = jokeData.value
              }
          }
  })
}
    LaunchedEffect(started){
                    LoadJoke()
                }

Column(
    verticalArrangement = Arrangement.Center,
    modifier = modifier.padding(30.dp)
){


    Text(
        fontSize = 40.sp,

        text = "The Chuckinator",
        modifier = modifier
            .align(alignment = Alignment.CenterHorizontally)


    )

    Text(
        modifier = modifier
            .align(alignment = Alignment.CenterHorizontally)
            .padding(20.dp),
        fontSize = 20.sp,
        text = theJoke,

    )
    Button(onClick = {LoadJoke()}, modifier = Modifier
        .align(alignment = Alignment.CenterHorizontally)

    )
    {
        Text(text="Load joke")
    }

        }
}

@Preview(showBackground=true)
@Composable
fun GreetingPreview() {
    AndroidAPIVecka2Theme {
        ChuckJoke()
    }
}
@Serializable
data class ChuckJoke(val categories: List<String>, val created_at : String, val value: String, val icon_url: String)