package pl.mbachorski.ribstutorial.rib.logged_in

import com.google.common.collect.ImmutableMap
import io.reactivex.Observable

interface ScoreStream {
    fun scores(): Observable<ImmutableMap<String, Int>>
}