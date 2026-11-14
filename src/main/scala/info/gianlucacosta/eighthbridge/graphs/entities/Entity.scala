package info.gianlucacosta.eighthbridge.graphs.entities

trait Entity {
  type Id

  val id: Id

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

object Entity {
  implicit class EntityExtensions[E <: Entity](self: E) {
    def update(updated: E): E =
      if (self == updated)
        updated
      else
        self
  }

  implicit class EntityListExtensions[E <: Entity](self: List[E]) {
    def update(updated: E): List[E] =
      self.map(_.update(updated))
  }

  implicit class EntitySetExtensions[E <: Entity](self: Set[E]) {
    //TODO! Can this be optimized?
    def update(updated: E): Set[E] =
      self.map(_.update(updated))
  }

  implicit class EntityPairExtensions[E <: Entity](self: (E, E)) {
    def update(updated: E): (E, E) =
      (
        self._1.update(updated),
        self._2.update(updated)
      )
  }
}