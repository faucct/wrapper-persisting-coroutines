import PersistingWrapper.wrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import java.util.UUID
import kotlin.coroutines.coroutineContext

var resumed = true

private suspend fun persist() {
  resumed = false
  coroutineContext[Persistor]!!.persist()
  if (!resumed) {
    throw RuntimeException("persisted")
  }
  resumed = true
}

suspend fun foo(a: String, b: String) {
  val idempotencyKey = UUID.randomUUID().toString()
  println("hi $idempotencyKey")
  persist()
  println("then $idempotencyKey")
  bar(b)
  println(a)
  delay(1000)
  yield()
  println("later")
}

suspend fun bar(b: String) {
  println("bar")
  persist()
  println("then")
  println(b)
  yield()
  delay(1000)
  yield()
  println("later")
}

fun main() {
  val message = runBlocking {
    wrapper {
      foo("a", "b")
      "foo"
    }
  }
  println(message)
}
