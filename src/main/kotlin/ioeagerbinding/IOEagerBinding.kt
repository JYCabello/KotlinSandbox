package ioeagerbinding

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.monad.flatten

// The goal of this class is to effectively check when running the IO actually runs the lambdas.
class IOEagerBinding {
    val log: MutableList<String> = mutableListOf()

    fun insideAnFx(): IO<Unit> = IO.fx { !somethingThatLogs() }

    fun insideAnFxFromGetter(): IO<Unit> = IO.fx { !lambdaGettingAnIO(Unit) }

    fun mapping(): IO<Unit> = IO.just(Unit).flatMap { somethingThatLogs() }

    fun mappingTwice(): IO<Unit> = somethingThatLogs().flatMap { somethingThatLogs() }

    fun effectThenMap(): IO<Unit> = IO.effect { somethingThatLogs() }.flatten().map { somethingThatLogs() }.flatten()

    fun gettingLambdaFromGetter(): IO<Unit> =
        IO.just(Unit).flatMap(lambdaGettingAnIO).flatMap { somethingThatLogs() }

    fun executingLambdaFromGetter(): IO<Unit> =
        IO.just(Unit).flatMap { lambdaGettingAnIO(it) }.flatMap { somethingThatLogs() }

    fun consumingFunctionInFlatMap(): IO<Unit> =
        IO.just(Unit).flatMap(::somethingThatLogsTakingUnit).flatMap { somethingThatLogs() }

    private fun somethingThatLogs(): IO<Unit> = log.add("something").let { IO.effect { Unit } }

    private val lambdaGettingAnIO: (Unit) -> IO<Unit>
        get() = log.add("something").let { { IO.effect { Unit } } }

    @Suppress("UNUSED_PARAMETER")
    private fun somethingThatLogsTakingUnit(unit: Unit): IO<Unit> = log.add("something").let { IO.effect { Unit } }
}
