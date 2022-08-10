package ua.com.foxminded.newsfeed.ui.error

import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.error.state.ErrorScreenEffect
import ua.com.foxminded.newsfeed.ui.error.state.ErrorScreenState

interface ErrorContract {
    interface ViewModel : FragmentContract.ViewModel<ErrorScreenState, ErrorScreenEffect>
    interface View : FragmentContract.View
    interface Host : FragmentContract.Host
}