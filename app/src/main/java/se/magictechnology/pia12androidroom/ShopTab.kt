package se.magictechnology.pia12androidroom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShopTab(tabtitle : String, isActive : Boolean, clicktab : () -> Unit) {

    var colstyle = Modifier
        .border(1.dp, Color.Green)
        .fillMaxHeight()
        .clickable {
            clicktab()
        }

    var shoptextcolor = Color.Black
    if(isActive) {
        colstyle = colstyle.then(Modifier.background(Color.Red))
    } else {
        colstyle = colstyle.then(Modifier.background(Color.LightGray))
        shoptextcolor = Color.White
    }


    Column(modifier = colstyle) {

        Text(tabtitle,
            color = shoptextcolor,
            modifier = Modifier.weight(1f))
    }

}


@Preview
@Composable
fun ShopTabRowPreview() {
    Row(modifier = Modifier.height(50.dp)) {
        ShopTab(tabtitle = "ALL", true) {}
        ShopTab(tabtitle = "BOUGHT", false) {}
        ShopTab(tabtitle = "NOTBOUGHT", false) {}
    }
}
@Preview
@Composable
fun ShopTabPreview() {
    ShopTab(tabtitle = "ALL", true) {}
}

