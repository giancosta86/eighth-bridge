package info.gianlucacosta.eighthbridge.theory

import java.util.UUID

trait Entity {
  private final val id: UUID = UUID.randomUUID()

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
