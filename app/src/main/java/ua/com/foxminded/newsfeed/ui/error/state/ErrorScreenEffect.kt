package ua.com.foxminded.newsfeed.ui.error.state

import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.error.ErrorContract

sealed class ErrorScreenEffect : AbstractEffect<ErrorContract.View>()
