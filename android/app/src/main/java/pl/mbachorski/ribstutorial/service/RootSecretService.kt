package pl.mbachorski.ribstutorial.service

import android.content.Context
import android.widget.Toast

class RootSecretService constructor(private val context: Context) : IRootSecretService {

    override fun call007(message: String) {
        Toast.makeText(context, "call007[$message]", Toast.LENGTH_LONG).show()
    }
}