package info.gianlucacosta.eighthbridge.graphs

package object directed {
  /**
   * Function passed to fold(). Its signature must include:
   * <ul>
   * <li><b>cumulatedValue</b> - the value cumulated until now</li>
   * <li><b>vertex</b> - the current vertex</li>
   * <li><b>vertexTopology</b> - describes the vertex/arc connections for the current vertex</li>
   * </ul>
   *
   * The function must return the next iteration value for <b>fold()</b>.
   */

  type VertexFoldProcessor[T, V <: Vertex, A <: Link] = (T, V, VertexTopology[V, A]) => T


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
