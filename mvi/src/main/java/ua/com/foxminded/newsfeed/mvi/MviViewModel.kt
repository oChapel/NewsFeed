package ua.com.foxminded.newsfeed.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.mvi.states.ScreenState

abstract class MviViewModel<V, S : ScreenState<V, S>, A> : ViewModel(), FragmentContract.ViewModel<S, A> {
    private val stateHolder = MutableLiveData<S>()
    private val effectHolder = MutableLiveData<A>()

    override fun getStateObservable() = stateHolder

    override fun getEffectObservable() = effectHolder

    protected fun setState(state: S) {
        stateHolder.value = stateHolder.value?.let {
            state.merge(it)
        } ?: state
    }

    protected fun getState() = stateHolder.value

    protected fun setEffect(action: A) {
        effectHolder.value = action
    }

    @CallSuper
    override fun onStateChanged(event: Lifecycle.Event) {
    }
}
