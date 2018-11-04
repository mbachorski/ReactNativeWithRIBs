package pl.mbachorski.ribstutorial.rib.logged_out

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.logged_out_rib.view.*

/**
 * Top level view for {@link LoggedOutBuilder.LoggedOutScope}.
 */
class LoggedOutView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), LoggedOutInteractor.LoggedOutPresenter {

    // implementation of LoggedOutInteractor.LoggedOutPresenter
    override fun loginName(): Observable<Pair<String, String>> {
        return RxView.clicks(login_button)
                .map { _ ->
                    Log.d("MOO", "click")
                    Pair(player_name_1.text.toString(), player_name_2.text.toString())
                }
    }

    /**
     * Alternatively
     * Inside onFinishInflate() bind the DetailsView’s subviews into private fields.
     * This should not be done in the constructor since the constructor is executed before
     * subviews are attached to DetailsView. Alternatively, just call findViewById on demand.
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}
