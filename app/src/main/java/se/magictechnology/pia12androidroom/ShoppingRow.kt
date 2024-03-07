package se.magictechnology.pia12androidroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShoppingRow(rowitem : Shopitem, dodelete : () -> Unit, dobought : () -> Unit) {
    Row {
        Text(rowitem.title!!)
        Text(rowitem.amount!!.toString())

        if(rowitem.bought == 1) {
            Text("KÖPT", modifier = Modifier.clickable {
                dobought()
            })
        } else {
            Text("INTE KÖPT", modifier = Modifier.clickable {
                dobought()
            })
        }

        Text("DELETE", modifier = Modifier.clickable {
            //shopvm.deleteshop(it)
            dodelete()
        })
    }
}