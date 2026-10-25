package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.{Arc, DirectedGraph, Vertex}

trait Timestamped {
  protected def timestamp: Timestamp

  protected def setTimestamp(timestamp: Timestamp): this.type

  def updateTimestamp(): this.type =
    setTimestamp(System.nanoTime())

  def isNewerThan(other: Timestamped): Boolean =
    timestamp > other.timestamp
}

object Timestamped {
  implicit class TimestampedGraphExtensions[V <: Vertex with Timestamped, A <: Arc with Timestamped](self: DirectedGraph[V, A]) {
    def getDiffSince(referenceGraph: DirectedGraph[V, A]): TimestampedGraphDiff[V, A] = {
      val addedVertexes =
        self.vertexes -- referenceGraph.vertexes

      val addedArcs =
        self.arcs -- referenceGraph.arcs

      val removedVertexes =
        referenceGraph.vertexes -- self.vertexes

      val removedArcs =
        referenceGraph.arcs -- self.arcs

      val keptVertexes =
        self.vertexes -- addedVertexes

      val updatedVertexes =
        keptVertexes
          .filter(vertex => {
            val referenceVertex =
              referenceGraph.getVertexById(vertex.id).get

            vertex.isNewerThan(referenceVertex)
          })

      val keptArcs =
        self.arcs -- addedArcs

      val updatedArcs =
        keptArcs
          .filter(arc => {
            val referenceArc =
              referenceGraph.getArcById(arc.id).get

            arc.isNewerThan(referenceArc)
          })

      TimestampedGraphDiff(
        addedVertexes =
          addedVertexes,

        addedArcs =
          addedArcs,

        updatedVertexes =
          updatedVertexes,

        updatedArcs =
          updatedArcs,

        removedVertexes =
          removedVertexes,

        removedArcs =
          removedArcs
      )
    }
  }
}