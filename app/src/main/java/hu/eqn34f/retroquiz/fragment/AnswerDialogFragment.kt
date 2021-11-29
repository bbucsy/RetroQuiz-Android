package hu.eqn34f.retroquiz.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.eqn34f.retroquiz.databinding.DialogAnswerBinding

class AnswerDialogFragment(val state: DialogState,val correct: String = "") : DialogFragment() {
    interface AnswerDialogFragmentListener {
        fun onNextQuestion()
        fun onExit()
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
        binding.txtCorrect.visibility = if(state == DialogState.WrongAnswer)  View.VISIBLE else View.GONE

        val dialog =  AlertDialog.Builder(requireContext())
            .setTitle(getTitle())
            .setView(binding.root)
            .setNegativeButton("Main Menu") { _, _ ->
                listener.onExit()
            }

        if(state != DialogState.TimeUp)
            dialog.setPositiveButton("Next") { _, _ ->
                listener.onNextQuestion()
            }


        return dialog.create()
    }


    private fun getMainText(): String {
        return when (state) {
            DialogState.RightAnswer -> "Your answer was correct!"
            DialogState.WrongAnswer -> "Your answer was wrong!"
            DialogState.TimeUp -> "Your time is up"
        }
    }

    private fun getTitle(): String {
        return when (state) {
            DialogState.RightAnswer -> "Congratulations"
            DialogState.TimeUp -> "Not enough Time"
            DialogState.WrongAnswer -> "Oops"
        }
    }

}