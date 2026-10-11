package info.gianlucacosta.eighthbridge.theory

import info.gianlucacosta.eighthbridge.theory.Entity.idGenerator

object Entity {
  private val idGenerator =
    new IdGenerator()
}

trait Entity {
  protected final val id: Int =
    idGenerator.generate()

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
