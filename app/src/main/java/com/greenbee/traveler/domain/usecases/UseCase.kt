package com.greenbee.traveler.domain.usecases

import arrow.core.Either
import com.greenbee.traveler.domain.exceptions.Failure
import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    fun execute(onResult: (Either<Failure, Type>) -> Unit, params: Params) {
        val job = CoroutineScope(Dispatchers.Default).async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult.invoke(job.await()) }
    }
}