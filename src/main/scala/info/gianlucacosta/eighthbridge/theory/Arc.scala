package info.gianlucacosta.eighthbridge.theory

trait Arc extends Entity

object Arc {
  implicit class ArcExtensions[A <: Arc](arc: A) {
    def update(updatedArc: A): A =
      if (arc == updatedArc)
        updatedArc
      else
        arc
  }

  implicit class ArcSetExtensions[A <: Arc](arcSet: Set[A]) {
    def updateArc(updatedArc: A): Set[A] =
      arcSet.map(_.update(updatedArc))
  }

  implicit class ArcsByVertexPairMapExtensions[V <: Vertex, A <: Arc](
                                                                         thisMap: Map[(V, V), Set[A]]
                                                                       ) {
    def flattenToSingleVertexKey(
                                  vertexSelector: ((V, V)) => V
                                ): Map[V, Set[A]] =
      thisMap
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

