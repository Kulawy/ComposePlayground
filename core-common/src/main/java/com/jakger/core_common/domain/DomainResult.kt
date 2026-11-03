package com.jakger.core_common.domain

sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Failure(val exception: DomainError) : DomainResult<Nothing>
}

inline fun <T, R> DomainResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (DomainError) -> R,
): R = when (this) {
    is DomainResult.Success -> onSuccess(data)
    is DomainResult.Failure -> onFailure(exception)
}