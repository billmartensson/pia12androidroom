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

enum class ShopFilterType {
    ALL, BOUGHT, NOTBOUGHT
}

class ShopViewmodel : ViewModel() {

    private val _shoppinglist = MutableStateFlow<List<Shopitem>?>(null)
    val shoppinglist : StateFlow<List<Shopitem>?> get() = _shoppinglist


    val shoplistobs = AppDatabase.getDatabase(null).shopDao().getAllflow()

    private val _showfilter = MutableStateFlow<ShopFilterType>(ShopFilterType.ALL)
    val showfilter : StateFlow<ShopFilterType> get() = _showfilter


    fun switchfilter(newfilter : ShopFilterType) {
        _showfilter.value = newfilter
        getallshop()
    }

    fun addshop(shopname : String, shopamount : String) {

        val shopdao = AppDatabase.getDatabase(null).shopDao()

        if(shopname == "") {
            return
        }
        if(shopamount.toIntOrNull() == null) {
            return
        }

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
            getallshop()
        }
    }

    fun switchbought(shop : Shopitem) {
        val shopdao = AppDatabase.getDatabase(null).shopDao()

        if(shop.bought == 1) {
            shop.bought = 0
        } else {
            shop.bought = 1
        }

        CoroutineScope(Dispatchers.IO).launch {
            shopdao.updateShop(shop)
            getallshop()
        }
    }

    fun getallshop() {
        _shoppinglist.value = mutableListOf()
        val shopdao = AppDatabase.getDatabase(null).shopDao()
        CoroutineScope(Dispatchers.IO).launch {
            var allshop = mutableListOf<Shopitem>()

            if(_showfilter.value == ShopFilterType.ALL) {
                allshop = shopdao.getAll().toMutableList()
            }
            if(_showfilter.value == ShopFilterType.BOUGHT) {
                allshop = shopdao.getBought().toMutableList()
            }
            if(_showfilter.value == ShopFilterType.NOTBOUGHT) {
                allshop = shopdao.getNotBought().toMutableList()
            }

            for(shop in allshop) {
                Log.i("pia12debug", shop.title!! + " " + shop.bought!!.toString())
            }

            CoroutineScope(Dispatchers.Main).launch {
                _shoppinglist.value = allshop
            }
        }
    }
    
}