package com.jakger.core_common.domain

interface DomainError {
    data class Unknown(val message: String?) : DomainError
}