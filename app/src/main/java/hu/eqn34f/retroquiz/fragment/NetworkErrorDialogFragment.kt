package hu.eqn34f.retroquiz.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.eqn34f.retroquiz.R
import hu.eqn34f.retroquiz.databinding.DialogNetworkErrorBinding

class NetworkErrorDialogFragment(val errorMessage: Throwable?) : DialogFragment() {
    interface NetworkErrorDialogListener {
        fun onRetry()
        fun onExit()
    }


    companion object {
        const val TAG = "NetworkErrorDialogFragment"
    }

    private lateinit var listener: NetworkErrorDialogListener

    private lateinit var binding: DialogNetworkErrorBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NetworkErrorDialogListener
            ?: throw RuntimeException("Activity must implement the NetworkErrorDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNetworkErrorBinding.inflate(LayoutInflater.from(context))
        errorMessage?.let {
            binding.switchShow.visibility = View.VISIBLE
            binding.errorMessage.text = it.message
            binding.switchShow.setOnCheckedChangeListener { _, b ->
                binding.errorMessage.visibility = if (b) View.VISIBLE else View.INVISIBLE
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.network_error_title))
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_retry) { _, _ ->
                listener.onRetry()
            }
            .setNegativeButton("Main Menu") { _, _ ->
                listener.onExit()
            }.create()
    }


}