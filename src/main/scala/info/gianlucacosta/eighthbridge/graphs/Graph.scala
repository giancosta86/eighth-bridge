package info.gianlucacosta.eighthbridge.graphs

trait Graph[V <: Vertex, L <: Link] {
  val vertexes: Set[V]

  val vertexesById: Map[Vertex#Id, V]

  def getVertexById(id: Vertex#Id): Option[V]

  val isEmpty: Boolean

  val nonEmpty: Boolean

  def addVertexes(vertexesToAdd: Set[V]): this.type

  def addVertexes(vertexesToAdd: V*): this.type

  def addVertex(vertex: V): this.type

  def updateVertex(updatedVertex: V): this.type

  def updateVertexes(updatedVertexes: Set[V]): this.type

  def updateVertexes(updatedVertexes: V*): this.type

  def removeVertexes(vertexesToRemove: Set[V]): this.type

  def removeVertexes(vertexes: V*): this.type

  def removeVertex(vertex: V): this.type

  val links: Set[L]

  val linksById: Map[Link#Id, L]

  def getLinkById(id: Link#Id): Option[L]

  def getLinksBetween(first: V, second: V): Set[L]

  def addLink(first: V, second: V, link: L): this.type

  def addLink(vertexPair: (V, V), link: L): this.type

  def updateLink(updatedLink: L): this.type

  def updateLinks(updatedLinks: Set[L]): this.type

  def updateLinks(updatedLinks: L*): this.type

  def removeLinks(linksToRemove: Set[L]): this.type

  def removeLinks(links: L*): this.type

  def removeLink(link: L): this.type

  val neighborsByVertex: Map[V, Set[V]]

  def getNeighbors(vertex: V): Set[V]

  val connectedVertexes: Set[V]

  val isolatedVertexes: Set[V]

  val connectedLinksByVertex: Map[V, Set[L]]

  def getConnectedLinks(vertex: V): Set[L]

  val isCyclic: Boolean
}
