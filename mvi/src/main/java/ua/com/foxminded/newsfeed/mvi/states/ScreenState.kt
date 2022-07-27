package ua.com.foxminded.newsfeed.mvi.states

interface ScreenState<T, S> {
    fun visit(screen: T)

    fun merge(prevState: S): S
}
