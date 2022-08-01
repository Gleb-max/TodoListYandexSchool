package school.yandex.todolist.presentation.todoitem

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import school.yandex.todolist.R
import school.yandex.todolist.databinding.FragmentTodoItemBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.text.SimpleDateFormat
import java.util.*

class TodoItemFragment : Fragment() {

    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding == null")

    private val viewModel: TodoItemViewModel by lazy {
        ViewModelProvider(this)[TodoItemViewModel::class.java]
    }

    private var screenMode: String = MODE_UNKNOWN
    private var todoItemId: String = TodoItem.UNDEFINED_ID

    private lateinit var onTodoItemEditingFinishedListener: OnTodoItemEditingFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnTodoItemEditingFinishedListener) {
            onTodoItemEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnTodoItemEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchRightMode()
        observeViewModel()

        binding.ibClose.setOnClickListener {
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
        binding.btnSave.setOnClickListener {
            //todo не успел доделать(
            if (screenMode == MODE_ADD) {
                viewModel.addTodoItem(
                    binding.etContent.text.toString(),
                    TodoItemImportance.IMPORTANT,
                    Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 10) }.time,
                )
            } else {
                viewModel.editTodoItem(
                    binding.etContent.text.toString(),
                    TodoItemImportance.IMPORTANT,
                    Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) }.time,
                )
            }
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
        binding.dropdownImportance.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(
                    R.array.importance_levels
                )
            )
        )
        binding.btnDelete.isEnabled = screenMode == MODE_EDIT
        binding.btnDelete.setOnClickListener {
            viewModel.deleteTodoItem()
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
        binding.tlDate.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            val picker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Выберите дату")
                .build()

            picker.addOnPositiveButtonClickListener {
                val timeZoneUTC = TimeZone.getDefault()
                val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1

                val simpleFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val date = Date(it + offsetFromUTC)

                binding.tlDate.setText(simpleFormat.format(date))

                //todo save date to view model
            }

            picker.show(parentFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.todoItem.observe(viewLifecycleOwner) {
            binding.etContent.setText(it.content)
            //todo
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getTodoItem(todoItemId)
        //todo
    }

    private fun launchAddMode() {
        //todo
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(TODO_ITEM_ID)) {
                throw RuntimeException("Param todo item id is absent")
            }
            todoItemId = args.getString(TODO_ITEM_ID, TodoItem.UNDEFINED_ID)
        }
    }

    interface OnTodoItemEditingFinishedListener {

        fun onTodoItemEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val TODO_ITEM_ID = "extra_todo_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): Fragment {
            return TodoItemFragment().apply {
                arguments = bundleOf(SCREEN_MODE to MODE_ADD)
            }
        }

        fun newInstanceEditItem(todoItemId: String): Fragment {
            return TodoItemFragment().apply {
                arguments = bundleOf(
                    SCREEN_MODE to MODE_EDIT,
                    TODO_ITEM_ID to todoItemId,
                )
            }
        }
    }
}