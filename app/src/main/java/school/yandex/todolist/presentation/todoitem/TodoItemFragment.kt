package school.yandex.todolist.presentation.todoitem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
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

    private val args by navArgs<TodoItemFragmentArgs>()

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
                .setTitleText(getString(R.string.choose_date_label))
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
        viewModel.todoItemDraft.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.etContent.setText(it.content)
                //todo
            }
        }
    }

    private fun launchRightMode() {
        todoItemId = args.itemId
        screenMode = if (todoItemId.isEmpty()) MODE_ADD else MODE_EDIT

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getTodoItem(todoItemId)

        binding.btnSave.setOnClickListener {
            viewModel.editTodoItem(
                binding.etContent.text.toString(),
                TodoItemImportance.IMPORTANT,
                Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) }.time,
            )
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
    }

    private fun launchAddMode() {
        binding.btnSave.setOnClickListener {
            viewModel.addTodoItem(
                binding.etContent.text.toString(),
                TodoItemImportance.IMPORTANT,
                Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 10) }.time,
            )
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
    }

    interface OnTodoItemEditingFinishedListener {

        fun onTodoItemEditingFinished()
    }

    companion object {

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
    }
}