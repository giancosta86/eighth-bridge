package info.gianlucacosta.eighthbridge.fx.graph

case class DefaultVisualGraph[V <: VisualVertex, A <: VisualArc] private(
                                                                 vertexes: Set[V],
                                                                 arcsByVertexPair: Map[(V, V), Set[A]]
                                                                 ) extends VisualGraph[V, A] {

  def this() = this(
    vertexes = Set[V](),
    arcsByVertexPair = Map[(V, V), Set[A]]()
  )

  override protected def graphCopy(
                                    vertexes: Set[V],
                                    arcsByVertexPair: Map[(V, V), Set[A]]
                                  ): this.type =
    copy(
      vertexes =
        vertexes,

      arcsByVertexPair =
        arcsByVertexPair
    ).asInstanceOf[this.type]
}
