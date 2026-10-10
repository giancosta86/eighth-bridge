package info.gianlucacosta.eighthbridge.fx.graph

case class DefaultVisualGraph[V <: VisualVertex, L <: VisualLink] private (
                                                                 vertexes: Set[V],
                                                                 arcsByVertexPair: Map[(V, V), Set[L]]
                                                                 ) extends VisualGraph[V, L] {

  def this() = this(vertexes = Set[V](), arcsByVertexPair = Map[(V, V), Set[L]]())

  override protected def graphCopy(vertexes: Set[V], arcsByVertexPair: Map[(V, V), Set[L]]): DefaultVisualGraph.this.type =
    copy(
      vertexes = vertexes,
      arcsByVertexPair = arcsByVertexPair
    ).asInstanceOf[this.type]
}
