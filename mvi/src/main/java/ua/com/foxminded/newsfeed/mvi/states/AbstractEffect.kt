package ua.com.foxminded.newsfeed.mvi.states

abstract class AbstractEffect<T> : ScreenEffect<T> {

    private var isHandled = false

    override fun visit(screen: T) {
        if (isHandled) {
            handle(screen)
            isHandled = true
        }
    }

    abstract fun handle(screen: T)
}