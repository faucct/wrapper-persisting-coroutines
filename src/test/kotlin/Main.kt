import PersistingWrapper.wrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.coroutines.coroutineContext

suspend fun foo() {
  println("hi")
  coroutineContext[Persistor.Key]!!.persist()
  bar()
  println("then")
  delay(1000)
  yield()
  println("later")
}

suspend fun bar() {
  println("bar")
  coroutineContext[Persistor.Key]!!.persist()
  println("then")
  delay(1000)
  yield()
  println("later")
}

fun main() {
  val message = runBlocking {
    wrapper {
      foo()
      "foo"
    }
  }
  println(message)
}
