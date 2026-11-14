package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

trait HasTimestamp {
  def timestamp: Timestamp

  def isNewerThan(other: HasTimestamp): Boolean =
    timestamp > other.timestamp
}

object HasTimestamp {
  implicit class TimestampedGraphExtensions[
    V <: Vertex with HasTimestamp,
    L <: Link with HasTimestamp](
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
              referenceGraph.vertexesById(vertex.id)

            vertex.isNewerThan(referenceVertex)
          })

      val keptLinks =
        self.links -- addedLinks

      val updatedLinks =
        keptLinks
          .filter(link => {
            val referenceLink =
              referenceGraph.linksById(link.id)

            link.isNewerThan(referenceLink)
          })

      TimestampedGraphDiff(
        updatedVertexes =
          updatedVertexes,

        updatedLinks =
          updatedLinks,

        addedVertexes =
          addedVertexes,

        addedLinks =
          addedLinks,

        removedVertexes =
          removedVertexes,

        removedLinks =
          removedLinks
      )
    }
  }
}