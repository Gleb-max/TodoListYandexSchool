package school.yandex.todolist.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import school.yandex.todolist.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthBinding == null")

    private lateinit var onAuthActionsListener: OnAuthActionsListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnAuthActionsListener) onAuthActionsListener = context
        else throw RuntimeException("Activity must implement OnAuthActionsListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardAuthYandex.setOnClickListener {
            onAuthActionsListener.onLogin()
        }
    }

    interface OnAuthActionsListener {

        fun onLogin()
    }
}