package se.magictechnology.pia12androidroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import se.magictechnology.pia12androidroom.ui.theme.Pia12androidroomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppDatabase.getDatabase(applicationContext)

        setContent {
            Pia12androidroomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Shopping()
                }
            }
        }




    }

    fun dodbstuff() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        val shopdao = db.shopDao()

        val tempshop = Shopitem(0,"Banan", 2, 0)

        CoroutineScope(Dispatchers.IO).launch {
            shopdao.insertAll(tempshop)

            val allshop = shopdao.getAll()

            for(shop in allshop) {
                Log.i("pia12debug", shop.title!!)
            }


        }

    }

}

@Composable
fun Shopping(shopvm : ShopViewmodel = viewModel(), modifier: Modifier = Modifier) {

    val shoppinglist by shopvm.shoppinglist.collectAsState()

    val shoppinglistobs by shopvm.shoplistobs.collectAsState(initial = mutableListOf())

    var addName by remember { mutableStateOf("") }
    var addAmount by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        Log.i("PIA12DEBUG", "LAUNCHED EFFECT")
        shopvm.getallshop()
    }

    Column {
        TextField(value = addName, onValueChange = { addName = it })
        TextField(value = addAmount, onValueChange = { addAmount = it })
        Button(onClick = {
            shopvm.addshop(addName, addAmount)
        }) {
            Text("Add")
        }



        Button(onClick = {
            shopvm.getallshop()
        }) {
            Text("GET")
        }

        if(shoppinglistobs != null) {
            LazyColumn {
                items(shoppinglistobs) {
                    Row {
                        Text(it.title!!)
                        Text(it.amount!!.toString())

                        Text("DELETE", modifier = Modifier.clickable {
                            shopvm.deleteshop(it)
                        })
                    }
                }
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pia12androidroomTheme {
        Shopping()
    }
}