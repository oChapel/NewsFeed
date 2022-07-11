package ua.com.foxminded.newsfeed.mvi.states

interface ScreenEffect<T> {
    fun visit(screen: T)
}