package ua.com.foxminded.newsfeed.ui.error.state

import ua.com.foxminded.newsfeed.mvi.states.AbstractState
import ua.com.foxminded.newsfeed.ui.error.ErrorContract

sealed class ErrorScreenState : AbstractState<ErrorContract.View, ErrorScreenState>()
