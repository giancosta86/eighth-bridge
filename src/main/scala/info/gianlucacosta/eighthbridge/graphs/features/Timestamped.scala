package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Graph, GraphCore, Link, Vertex}

trait Timestamped {
  protected def timestamp: Timestamp

  protected def setTimestamp(timestamp: Timestamp): this.type

  def updateTimestamp(): this.type =
    setTimestamp(Timestamp.now)

  def isNewerThan(other: Timestamped): Boolean =
    timestamp > other.timestamp
}

object Timestamped {
  implicit class TimestampedDirectedGraphExtensions[
    V <: Vertex with Timestamped,
    L <: Link with Timestamped](
      self: Graph[V, L]
  ) {
    def getDiffSince(referenceGraph: Graph[V, L]): TimestampedGraphDiff[V, L] = {
      val addedVertexes =
        self.vertexes -- referenceGraph.vertexes

      val addedLinks =
        self.links -- referenceGraph.links

      val removedVertexes =
        referenceGraph.vertexes -- self.vertexes

      val removedLinks =
        referenceGraph.links -- self.links

      val keptVertexes =
        self.vertexes -- addedVertexes

      val updatedVertexes =
        keptVertexes
          .filter(vertex => {
            val referenceVertex =
              referenceGraph.getVertexById(vertex.id).get

            vertex.isNewerThan(referenceVertex)
          })

      val keptLinks =
        self.links -- addedLinks

      val updatedLinks =
        keptLinks
          .filter(link => {
            val referenceLink =
              referenceGraph.getLinkById(link.id).get

            link.isNewerThan(referenceLink)
          })

      TimestampedGraphDiff(
        addedVertexes =
          addedVertexes,

        addedLinks =
          addedLinks,

        updatedVertexes =
          updatedVertexes,

        updatedLinks =
          updatedLinks,

        removedVertexes =
          removedVertexes,

        removedLinks =
          removedLinks
      )
    }
  }
}