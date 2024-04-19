package com.yusmle.restaurants.foundation

interface BaseUseCase<INPUT, OUTPUT> {

    suspend fun execute(input: INPUT): OUTPUT
}
