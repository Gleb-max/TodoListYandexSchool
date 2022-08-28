package school.yandex.todolist.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialSharedAxis
import school.yandex.todolist.R
import school.yandex.todolist.TodoApp
import school.yandex.todolist.core.ext.getReadableDate
import school.yandex.todolist.databinding.FragmentTodoItemBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import school.yandex.todolist.presentation.stateholder.TodoItemViewModel
import school.yandex.todolist.presentation.stateholder.ViewModelFactory
import java.util.*
import javax.inject.Inject

class TodoItemFragment : Fragment() {

    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: TodoItemViewModel

    private val component by lazy {
        (requireActivity().application as TodoApp).component
    }

    private val args by navArgs<TodoItemFragmentArgs>()

    private var screenMode: String = MODE_UNKNOWN
    private var todoItemId: String = TodoItem.UNDEFINED_ID

    private var onSnackBarShowListener: OnSnackBarShowListener? = null

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnSnackBarShowListener) onSnackBarShowListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        enterTransition = Slide()
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo передавать todo item id во вью модель через фабрику. Убрать логику работы с id из фрагмента
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoItemViewModel::class.java]
        observeViewModel()

        launchRightMode()

        binding.ibClose.setOnClickListener {
            onTodoItemEditingFinished()
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteTodoItem(todoItemId)
            onTodoItemEditingFinished()
        }
        binding.etContent.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.updateDraft(content = it.toString()) }
        }
        setupImportanceSelector()
        setupDatePicker()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onTodoItemEditingFinished() {
        findNavController().navigateUp()
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
        binding.dropdownImportance.setOnItemClickListener { adapterView, view, i, l ->
            val importance = when (i) {
                1 -> TodoItemImportance.BASIC
                2 -> TodoItemImportance.LOW
                3 -> TodoItemImportance.IMPORTANT
                else -> null
            }
            viewModel.updateDraft(importance = importance)
        }
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
                if (binding.etContent.text.isNullOrBlank() && it.content.isNotBlank()) {
                    binding.etContent.setText(it.content)
                }
                binding.tlDate.setText(it.deadline?.getReadableDate())
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
            val grayColor = MaterialColors.getColor(this, R.attr.disableLabelColor)
            setTextColor(grayColor)
//            iconTint = grayColor
//            setIconTintResource(R.color.gray)
            compoundDrawableTintList = ColorStateList.valueOf(grayColor)
        }
        binding.btnSave.setOnClickListener {
            saveTodoItem()
        }
    }

    private fun fetchTodoItem() {
        viewModel.getTodoItem(todoItemId, onError = {
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_load_data),
                getString(R.string.retry),
                this::fetchTodoItem
            )
        })
    }

    private fun saveTodoItem() {
        //todo: переделать это решение сейчас вижу минус: дополнительная проверка на screenMode,
        // хотя уже проверяю в начале и вызываваю launchRightMode;
        val onError: () -> Unit = {
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_save_data),
                getString(R.string.retry),
                this::saveTodoItem
            )
        }
        val onSuccess = { onTodoItemEditingFinished() }
        when (screenMode) {
            MODE_EDIT -> viewModel.editTodoItem(onError = onError, onSuccess = onSuccess)
            MODE_ADD -> viewModel.addTodoItem(onError = onError, onSuccess = onSuccess)
        }
    }

    companion object {

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
    }
}