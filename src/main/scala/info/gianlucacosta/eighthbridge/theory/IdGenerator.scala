package info.gianlucacosta.eighthbridge.theory

private class IdGenerator {
  private var idToEmit: Int =
    0

  def generate(): Int = {
    val result =
      idToEmit

    idToEmit += 1

    result
  }
}
