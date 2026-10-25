package info.gianlucacosta.eighthbridge.theory.entities

import info.gianlucacosta.eighthbridge.theory.{Arc, Vertex}

object Entity {
  private val idGenerator =
    new IdGenerator()

  implicit class EntityExtensions[E <: Entity](self: E) {
    def update(updated: E): E =
      if (self == updated)
        updated
      else
        self
  }

  implicit class EntitySetExtensions[E <: Entity](self: Set[E]) {
    def updateEntity(updated: E): Set[E] =
      self.map(_.update(updated))
  }

  implicit class EntityPairExtensions[E <: Entity](self: (E, E)) {
    def updateEntity(updated: E): (E, E) =
      (
        self._1.update(updated),
        self._2.update(updated)
      )
  }
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
