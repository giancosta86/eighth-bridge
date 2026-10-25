package info.gianlucacosta.eighthbridge.graphs

package object directed {
  implicit class ArcsByVertexPairMapExtensions[V <: Vertex, A <: Link](
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
