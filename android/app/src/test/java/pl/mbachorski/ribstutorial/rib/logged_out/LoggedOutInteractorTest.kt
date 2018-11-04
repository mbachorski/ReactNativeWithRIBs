package pl.mbachorski.ribstutorial.rib.logged_out

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Note: We recommend not to use TextUtils because TextUtils will cause a runtime crash.
 * AOSP classes can’t be used inside non-roboelectric tests.
 * And roboelectric tests are really slow, so we avoid them.
 */
class LoggedOutInteractorTest : RibTestBasePlaceholder() {

    @Mock
    internal lateinit var presenter: LoggedOutInteractor.LoggedOutPresenter
    @Mock
    internal lateinit var router: LoggedOutRouter
    @Mock
    internal lateinit var listener: LoggedOutInteractor.Listener

    private var interactor: LoggedOutInteractor? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = TestLoggedOutInteractor.create(presenter, listener)
    }

    @Test
    fun attach_whenViewEmitsName_shouldCallListener() {
        `when`(presenter.loginName()).thenReturn(Observable.just(Pair("fakename 1", "fakename 2")))

        InteractorHelper.attach(interactor, presenter, router, null)
        verify(listener).login(anyInKotlin(String::class.java))
    }

    @Test
    fun attach_whenViewEmitsEmptyName_shouldNotCallListener() {
        `when`(presenter.loginName()).thenReturn(Observable.just(""))

        InteractorHelper.attach(interactor, presenter, router, null)
        // This test will fail because the interactor doesn’t have any logic for handling empty strings.
        // You’ll need to fix this as discussed above the code snippet.
        verify(listener, never()).login(anyInKotlin(String::class.java))
    }

    // HACK FOR any() for kotlin
    private fun <T> anyInKotlin(arg: Class<T>): T {
        Mockito.any<T>(arg)
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}