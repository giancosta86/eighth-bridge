package info.gianlucacosta.eighthbridge.theory.entities

object Entity {
  private val idGenerator =
    new IdGenerator()
}

trait Entity {
  final val id: EntityId =
    Entity.idGenerator.generate()

  final override def equals(obj: Any): Boolean =
    obj match {
      case other: Entity =>
        id == other.id

      case _ =>
        false
    }

  final override def hashCode(): Int =
    id.hashCode()
}
