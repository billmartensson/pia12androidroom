package se.magictechnology.pia12androidroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShopViewmodel : ViewModel() {

    private val _shoppinglist = MutableStateFlow<List<Shopitem>?>(null)
    val shoppinglist : StateFlow<List<Shopitem>?> get() = _shoppinglist

    val shoplistobs = AppDatabase.getDatabase(null).shopDao().getAllflow()


    fun addshop(shopname : String, shopamount : String) {

        val shopdao = AppDatabase.getDatabase(null).shopDao()

        val tempshop = Shopitem(0, shopname, shopamount.toInt(), 0)

        CoroutineScope(Dispatchers.IO).launch {
            shopdao.insertAll(tempshop)
            getallshop()
        }
    }

    fun deleteshop(delshop : Shopitem) {
        val shopdao = AppDatabase.getDatabase(null).shopDao()

        CoroutineScope(Dispatchers.IO).launch {
            shopdao.delete(delshop)
        }
    }

    fun getallshop() {
        /*
        val shopdao = AppDatabase.getDatabase(null).shopDao()

        CoroutineScope(Dispatchers.IO).launch {

            val allshop = shopdao.getAll()

            _shoppinglist.value = allshop
            for(shop in allshop) {
                Log.i("pia12debug", shop.title!!)
            }
        }

         */
    }
    
}