package school.yandex.todolist.presentation.todoitem

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import school.yandex.todolist.R
import school.yandex.todolist.core.ext.getReadableDate
import school.yandex.todolist.databinding.FragmentTodoItemBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import java.util.*

class TodoItemFragment : Fragment() {

    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding == null")

    private val viewModel: TodoItemViewModel by inject()

    private val args by navArgs<TodoItemFragmentArgs>()

    private var screenMode: String = MODE_UNKNOWN
    private var todoItemId: String = TodoItem.UNDEFINED_ID

    private lateinit var onTodoItemEditingFinishedListener: OnTodoItemEditingFinishedListener
    private var onSnackBarShowListener: OnSnackBarShowListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnTodoItemEditingFinishedListener) {
            onTodoItemEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnTodoItemEditingFinishedListener")
        }

        if (context is OnSnackBarShowListener) onSnackBarShowListener = context
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

        binding.btnDelete.setOnClickListener {
            viewModel.deleteTodoItem(todoItemId)
            onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
        }
        setupImportanceSelector()
        setupDatePicker()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupImportanceSelector() {
        binding.dropdownImportance.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(
                    R.array.importance_levels
                )
            )
        )
    }

    private fun setupDatePicker() {
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

                val date = Date(it + offsetFromUTC)
                viewModel.updateDraft(deadline = date)
            }

            picker.show(parentFragmentManager, null)
        }
    }

    private fun observeViewModel() {
        viewModel.todoItemDraft.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.etContent.setText(it.content)
                binding.tlDate.setText(it.deadline?.getReadableDate())
//                binding.dropdownImportance.setText("")
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
        fetchTodoItem()

        binding.btnSave.setOnClickListener {
            saveTodoItem()
        }
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun launchAddMode() {
        with(binding.btnDelete) {
            isEnabled = false
            setTextColor(ColorStateList.valueOf(requireContext().getColor(R.color.gray)))
            setIconTintResource(R.color.gray)
            compoundDrawableTintList = ColorStateList.valueOf(
                requireContext().getColor(R.color.gray)
            )
        }
        binding.btnSave.setOnClickListener {
            saveTodoItem()
        }
    }

    private fun fetchTodoItem() {
        //todo придумать как сделать все эти методы красивыми и переиспользуемыма,
        // а не каждый в отдельности через try/catch
        try {
            viewModel.getTodoItem(todoItemId)
        } catch (exc: Exception) {
            exc.printStackTrace()
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_load_data),
                getString(R.string.retry),
                this::fetchTodoItem
            )
        }
    }

    private fun saveTodoItem() {
        //todo: переделать это решение
        // сейчас вижу 2 минуса: дополнительная проверка на screenMode,
        // хотя уже проверяю в начале и вызываваю launchRightMode;
        // try/catch тут выглядит не к месту - нужно придумать куда его перенести
        val success = try {
            when (screenMode) {
                MODE_EDIT -> viewModel.editTodoItem()
                MODE_ADD -> viewModel.addTodoItem()
            }
            true
        } catch (exc: Exception) {
            exc.printStackTrace()
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_save_data),
                getString(R.string.retry),
                this::saveTodoItem
            )
            false
        }
        if (success) onTodoItemEditingFinishedListener.onTodoItemEditingFinished()
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