interface Wrapper {
  suspend operator fun <T> invoke(block: suspend () -> T): T
}
