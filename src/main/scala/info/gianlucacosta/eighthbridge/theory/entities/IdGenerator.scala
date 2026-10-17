package info.gianlucacosta.eighthbridge.theory.entities

private class IdGenerator {
  private var idToEmit: EntityId =
    0

  def generate(): EntityId = {
    val result =
      idToEmit

    idToEmit +=
      1

    result
  }
}
