package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import info.gianlucacosta.eighthbridge.graphs.point2point.ArcBinding

/**
  * Default VisualGraph implementation
  *
  * @param vertexes
  * @param links
  * @param bindings
  */
case class DefaultVisualGraph[V <: VisualVertex[V], L <: VisualLink[L]](
                                                                         vertexes: Set[V] = Set[V](),
                                                                         links: Set[L] = Set[L](),
                                                                         bindings: Set[ArcBinding] = Set[ArcBinding]()) extends VisualGraph[V, L, DefaultVisualGraph[V, L]] {

  override def graphCopy(vertexes: Set[V], links: Set[L], bindings: Set[ArcBinding]): DefaultVisualGraph[V, L] =
    copy(
      vertexes = vertexes,
      links = links,
      bindings = bindings
    )
}
