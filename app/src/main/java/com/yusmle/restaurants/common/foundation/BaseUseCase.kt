package com.yusmle.restaurants.common.foundation

interface BaseUseCase<INPUT, OUTPUT> {

    suspend fun execute(input: INPUT): OUTPUT
}
