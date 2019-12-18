package ioeagerbinding

import arrow.fx.IO
import io.kotlintest.matchers.numerics.shouldBeExactly
import org.junit.Test

class IOEagerBindingTests {
    @Test
    fun `only logs once you execute inside the fx`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.insideAnFx()
        // Inside an fx, it doesn't log until you actually run.
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 1
    }

    @Test
    fun `only logs once you execute inside the fx even from getter`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.insideAnFxFromGetter()
        // Inside an fx you already are inside the IO execution chain, so the getter doesn't log until you run.
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 1
    }

    @Test
    fun `doesn't log if you map neither`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.mapping()
        // The lambda inside a map does not resolve non-IO code neither.
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 1
    }

    @Test
    fun `mapping twice`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.mappingTwice()
        // The first logging happens before the IO execution chain starts building.
        binding.log.size shouldBeExactly 1
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 2
    }

    @Test
    fun `mapping from inside an effect then flatten, map and flatten`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.effectThenMap()
        // The IO chain already started, so no eager logging.
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 2
    }

    @Test
    fun `getting one of the lambdas from a getter`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.gettingLambdaFromGetter()
        // The getter runs because we are consuming the lambda straight away.
        binding.log.size shouldBeExactly 1
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 2
    }

    @Test
    fun `executing one of the lambdas from a getter`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.executingLambdaFromGetter()
        // The getter doesn't get executed because it is INSIDE the lambda passed to flatMap
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 2
    }

    @Test
    fun `consuming a function straight away in the flatMap`() {
        val binding = IOEagerBinding()
        val io: IO<Unit> = binding.consumingFunctionInFlatMap()
        // Consuming the function acts exactly the same, the lambda doesn't get run until you actually execute it.
        binding.log.size shouldBeExactly 0
        io.unsafeRunSync()
        binding.log.size shouldBeExactly 2
    }
}
