package pl.mbachorski.ribstutorial.service

import android.content.Context
import android.widget.Toast

class AndroidSimpleMessageShower constructor(private val context: Context) : ISimpleMessageShower {

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}