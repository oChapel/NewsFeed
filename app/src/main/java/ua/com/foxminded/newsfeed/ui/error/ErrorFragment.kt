package ua.com.foxminded.newsfeed.ui.error

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import ua.com.foxminded.newsfeed.mvi.fragments.HostedDialogFragment
import ua.com.foxminded.newsfeed.ui.error.state.ErrorScreenEffect
import ua.com.foxminded.newsfeed.ui.error.state.ErrorScreenState

class ErrorFragment : HostedDialogFragment<
        ErrorContract.View,
        ErrorScreenState,
        ErrorScreenEffect,
        ErrorContract.ViewModel,
        ErrorContract.Host>(), ErrorContract.View, DialogInterface.OnClickListener {

    private var error: Throwable? = null

    override fun createModel(): ErrorContract.ViewModel? {
        return null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getInt(DIALOG_TITLE_STRING_ID)
        val message = requireArguments().getInt(DIALOG_MESSAGE_STRING_ID)
        val positiveButton = requireArguments().getInt(POSITIVE_BUTTON_TEXT_STRING_ID)
        return AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(getString(message) + " " + error?.message)
            .setPositiveButton(positiveButton, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                dismissAllowingStateLoss()
            }
        }
    }

    fun setError(error: Throwable) {
        this.error = error
    }

    companion object {
        const val TAG = "error_dialog"
        private const val DIALOG_TITLE_STRING_ID = "dialog_title"
        private const val DIALOG_MESSAGE_STRING_ID = "dialog_message"
        private const val POSITIVE_BUTTON_TEXT_STRING_ID = "positive_button"

        fun newInstance(
            title: Int,
            message: Int,
            positiveButtonText: Int
        ): ErrorFragment {
            val b = Bundle()
            b.putInt(DIALOG_TITLE_STRING_ID, title)
            b.putInt(DIALOG_MESSAGE_STRING_ID, message)
            b.putInt(POSITIVE_BUTTON_TEXT_STRING_ID, positiveButtonText)
            return ErrorFragment().apply { this.arguments = b }
        }
    }
}
