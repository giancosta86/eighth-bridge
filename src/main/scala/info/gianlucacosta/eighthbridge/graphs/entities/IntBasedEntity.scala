package info.gianlucacosta.eighthbridge.graphs.entities

object IntBasedEntity {
  private var _nextId: IntBasedEntity#EntityId =
    0

  private def nextId: IntBasedEntity#EntityId = {
    val result =
      _nextId

    _nextId +=
      1

    result
  }
}

trait IntBasedEntity extends Entity {
  override type EntityId =
    Int

  val id: EntityId =
    IntBasedEntity.nextId
}
