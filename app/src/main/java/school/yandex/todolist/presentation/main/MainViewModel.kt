package school.yandex.todolist.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.yandex.todolist.core.viewmodel.BaseViewModel
import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.domain.usecase.GetUserUseCase
import school.yandex.todolist.domain.usecase.SaveUserUseCase

class MainViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : BaseViewModel() {

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    init {

        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _user.value = getUserUseCase()
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            saveUserUseCase(user)
        }
    }
}