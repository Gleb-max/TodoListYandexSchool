package school.yandex.todolist.presentation.i

interface OnSnackBarShowListener {

    fun showSnackBarMessage(title: String, action: String?, actionCallBack: () -> Unit)
}