package info.gianlucacosta.eighthbridge.theory

import info.gianlucacosta.eighthbridge.theory.entities.Entity

trait Arc extends Entity

object Arc {
  implicit class ArcExtensions[A <: Arc](self: A) {
    def update(updatedArc: A): A =
      if (self == updatedArc)
        updatedArc
      else
        self
  }

  implicit class ArcSetExtensions[A <: Arc](self: Set[A]) {
    def updateArc(updatedArc: A): Set[A] =
      self.map(_.update(updatedArc))
  }

  implicit class ArcsByVertexPairMapExtensions[V <: Vertex, A <: Arc](
                                                                       self: Map[(V, V), Set[A]]
                                                                       ) {
    def flattenToSingleVertexKey(
                                  vertexSelector: ((V, V)) => V
                                ): Map[V, Set[A]] =
      self
        .view
        .groupBy { case (vertexPair, _) =>
          vertexSelector(vertexPair)
        }
        .mapValues(
          _.map( {
              case (_, arcs) => arcs
            })
            .flatten
            .toSet
        )
  }
}

