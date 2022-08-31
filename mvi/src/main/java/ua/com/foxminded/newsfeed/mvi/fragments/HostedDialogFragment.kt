package ua.com.foxminded.newsfeed.mvi.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ua.com.foxminded.newsfeed.mvi.states.ScreenEffect
import ua.com.foxminded.newsfeed.mvi.states.ScreenState
import java.lang.reflect.ParameterizedType

abstract class HostedDialogFragment<
        VIEW : FragmentContract.View,
        STATE : ScreenState<VIEW, STATE>,
        ACTION : ScreenEffect<VIEW>,
        VIEW_MODEL : FragmentContract.ViewModel<STATE, ACTION>,
        HOST : FragmentContract.Host>
    : AppCompatDialogFragment(), FragmentContract.View, Observer<STATE>, LifecycleEventObserver {

    protected var model: VIEW_MODEL? = null
        private set

    protected var fragmentHost: HOST? = null
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentHost = context as HOST
        } catch (e: Throwable) {
            val hostClassName = ((javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[1] as Class<*>).canonicalName
            throw RuntimeException(
                "Activity must implement " + hostClassName
                        + " to attach " + javaClass.simpleName, e
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentHost = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setModel(createModel())
        lifecycle.addObserver(this)
        model?.getStateObservable()?.observe(this, this)
        model?.getEffectObservable()?.observe(this) { action -> action.visit(this as VIEW) }
    }

    override fun onStateChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        model?.onStateChanged(event)

        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            lifecycle.removeObserver(this)
            model?.getEffectObservable()?.removeObservers(this)
            model?.getStateObservable()?.removeObservers(this)
        }
    }

    override fun onChanged(state: STATE) {
        state.visit(this as VIEW)
    }

    protected abstract fun createModel(): VIEW_MODEL?

    protected fun setModel(model: VIEW_MODEL?) {
        this.model = model
    }
}
