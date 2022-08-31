package ua.com.foxminded.newsfeed.mvi.states

abstract class AbstractState<T, S> : ScreenState<T, S> {

    override fun visit(screen: T) {
    }

    @Suppress("UNCHECKED_CAST")
    override fun merge(prevState: S): S {
        return this as S
    }
}
