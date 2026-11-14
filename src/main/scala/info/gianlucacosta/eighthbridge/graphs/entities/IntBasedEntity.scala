package info.gianlucacosta.eighthbridge.graphs.entities

trait IntBasedEntity extends Entity {
  override type Id =
    Int

  val id: Id =
    IntBasedEntity.nextId
}

object IntBasedEntity {
  private var _nextId: IntBasedEntity#Id =
    0

  private def nextId: IntBasedEntity#Id = {
    val result =
      _nextId

    _nextId +=
      1

    result
  }
}


