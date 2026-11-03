package info.gianlucacosta.eighthbridge.graphs

trait Graph[V <: Vertex, L <: Link] {
  val vertexes: Set[V]

  @transient
  lazy val vertexesById: Map[Vertex#EntityId, V] =
    vertexes
      .map(vertex =>
        vertex.id -> vertex
      )
      .toMap

  @transient
  val isEmpty: Boolean =
    vertexes.isEmpty

  @transient
  val nonEmpty: Boolean =
    vertexes.nonEmpty

  def links: Set[L]

  def linksById: Map[Link#EntityId, L]

  def connectedVertexes: Set[V]

  def isolatedVertexes: Set[V]

  def getNeighbors(vertex: V): Set[V]

  def getConnectedLinks(vertex: V): Set[L]

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: AnyRef =>
        eq(other)

      case _ =>
        false
    }
  }

  override def hashCode(): Int =
    System.identityHashCode(this)
}
