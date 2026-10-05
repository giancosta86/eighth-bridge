package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import info.gianlucacosta.eighthbridge.graphs.point2point.ArcBinding

/**
  * Default VisualGraph implementation
  *
  * @param vertexes
  * @param links
  * @param bindings
  */
case class DefaultVisualGraph[V <: VisualVertex, L <: VisualLink](
                                                                         vertexes: Set[V] = Set[V](),
                                                                         links: Set[L] = Set[L](),
                                                                         bindings: Set[ArcBinding] = Set[ArcBinding]()) extends VisualGraph[V, L] {
  override def graphCopy(vertexes: Set[V], links: Set[L], bindings: Set[ArcBinding]): this.type =
    copy(
      vertexes = vertexes,
      links = links,
      bindings = bindings
    ).asInstanceOf[this.type]
}
