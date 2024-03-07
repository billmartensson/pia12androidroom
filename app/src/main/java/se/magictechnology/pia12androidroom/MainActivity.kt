package se.magictechnology.pia12androidroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val shopfilter by shopvm.showfilter.collectAsState()

    //val shoppinglistobs by shopvm.shoplistobs.collectAsState(initial = mutableListOf())

    var addName by remember { mutableStateOf("") }
    var addAmount by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        Log.i("PIA12DEBUG", "LAUNCHED EFFECT")
        shopvm.getallshop()
    }

    Column {

        Row {

            TextField(value = addName,
                onValueChange = { addName = it },
                modifier = Modifier.weight(1f)
            )

            TextField(value = addAmount,
                onValueChange = { addAmount = it },
                modifier = Modifier.width(100.dp)
            )

            Button(modifier = Modifier.width(100.dp), onClick = {
                shopvm.addshop(addName, addAmount)
                addName = ""
                addAmount = ""
            }) {
                Text("Add")
            }

        }

        Row(modifier = Modifier.height(50.dp)) {
            ShopTab(tabtitle = "ALL", shopfilter == ShopFilterType.ALL) {
                shopvm.switchfilter(ShopFilterType.ALL)
            }
            ShopTab(tabtitle = "BOUGHT",shopfilter == ShopFilterType.BOUGHT) {
                shopvm.switchfilter(ShopFilterType.BOUGHT)
            }
            ShopTab(tabtitle = "NOT BOUGHT", shopfilter == ShopFilterType.NOTBOUGHT) {
                shopvm.switchfilter(ShopFilterType.NOTBOUGHT)
            }

        }

        if(shoppinglist != null) {
            LazyColumn {
                items(shoppinglist!!) {theitem ->

                    ShoppingRow(rowitem = theitem, dodelete = {
                        shopvm.deleteshop(theitem)
                    }, dobought = {
                        shopvm.switchbought(theitem)
                    })
                }
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingPreview() {
    Pia12androidroomTheme {
        Shopping()
    }
}