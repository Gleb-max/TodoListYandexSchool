package school.yandex.todolist.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    protected fun CoroutineScope.execute(
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        function: suspend () -> Unit,
    ) {
        launch(Dispatchers.IO) {
            runCatching {
                function()
            }
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        try {
                            onSuccess?.invoke()
                        } catch (exc: Exception) {
                            exc.printStackTrace()
                        }
                    }
                }
                .onFailure { error ->
                    error.printStackTrace()
                    withContext(Dispatchers.Main) {
                        try {
                            onError?.invoke()
                        } catch (exc: Exception) {
                            exc.printStackTrace()
                        }
                    }
                }
        }
    }
}