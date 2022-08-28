package school.yandex.todolist.di

import javax.inject.Scope

/**
 * скоуп жизни приложения, по хорошему должны быть еще, например для фрагментов и юзкейсов
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope