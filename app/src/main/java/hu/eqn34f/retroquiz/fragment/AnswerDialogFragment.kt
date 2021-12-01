package hu.eqn34f.retroquiz.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.eqn34f.retroquiz.R
import hu.eqn34f.retroquiz.databinding.DialogAnswerBinding

class AnswerDialogFragment(private val state: DialogState, private val correct: String = "") :
    DialogFragment() {
    interface AnswerDialogFragmentListener {
        fun onNextQuestion()
        fun onFinishGame()
    }

    enum class DialogState {
        RightAnswer,
        WrongAnswer,
        TimeUp
    }

    companion object {
        const val TAG = "AnswerDialogFragment"
    }

    private lateinit var listener: AnswerDialogFragmentListener

    private lateinit var binding: DialogAnswerBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? AnswerDialogFragmentListener
            ?: throw RuntimeException("Activity must implement the AnswerDialogFragmentListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAnswerBinding.inflate(LayoutInflater.from(context))
        binding.txtMain.text = getMainText()
        binding.txtCorrect.text = correct
        binding.txtCorrect.visibility =
            if (state == DialogState.WrongAnswer) View.VISIBLE else View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getTitle())
            .setView(binding.root)
            .setNegativeButton(resources.getString(R.string.dialog_exit)) { _, _ ->
                listener.onFinishGame()
            }

        // if the state is not TimeUp then the user can go to the next question
        if (state != DialogState.TimeUp)
            dialog.setPositiveButton(resources.getString(R.string.dialog_next)) { _, _ ->
                listener.onNextQuestion()
            }


        return dialog.create().apply { setCanceledOnTouchOutside(false) }
    }


    private fun getMainText(): String {
        return when (state) {
            DialogState.RightAnswer -> getString(R.string.dialog_text_correct)
            DialogState.WrongAnswer -> getString(R.string.dialog_text_wrong)
            DialogState.TimeUp -> getString(R.string.dialog_text_timeup)
        }
    }

    private fun getTitle(): String {
        return when (state) {
            DialogState.RightAnswer -> getString(R.string.dialog_title_correct)
            DialogState.TimeUp -> getString(R.string.dialog_title_wrong)
            DialogState.WrongAnswer -> getString(R.string.dialog_title_timeup)
        }
    }

}