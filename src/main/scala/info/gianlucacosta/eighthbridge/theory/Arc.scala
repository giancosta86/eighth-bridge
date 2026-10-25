package info.gianlucacosta.eighthbridge.theory

import info.gianlucacosta.eighthbridge.theory.entities.{Entity, IntBasedEntity}

trait Arc extends Link

object Arc {
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

