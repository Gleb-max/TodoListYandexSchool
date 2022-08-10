package school.yandex.todolist.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun CoroutineScope.execute(
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        function: suspend () -> Unit,
    ) {
        launch(Dispatchers.Main) {
            runCatching {
                function()
            }
                .onSuccess {
                    onSuccess?.invoke()
                }
                .onFailure { error ->
                    error.printStackTrace()
                    onError?.invoke()
                }
        }
    }
}