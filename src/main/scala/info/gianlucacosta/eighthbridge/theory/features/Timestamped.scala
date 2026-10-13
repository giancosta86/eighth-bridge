package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{Arc, DirectedGraph, Vertex}

trait Timestamped {
  protected def timestamp: Long

  protected def setTimestamp(timestamp: Long): this.type

  def updateTimestamp(): this.type =
    setTimestamp(System.nanoTime())

  def isNewerThan(other: Timestamped): Boolean =
    timestamp > other.timestamp
}

object Timestamped {
  implicit class TimestampedGraphExtensions[V <: Vertex with Timestamped, A <: Arc with Timestamped](graph: DirectedGraph[V, A]) {
    def getDiffSince(referenceGraph: DirectedGraph[V, A]): TimestampedGraphDiff[V, A] = {
      val addedVertexes =
        graph.vertexes -- referenceGraph.vertexes

      val addedArcs =
        graph.arcs -- referenceGraph.arcs

      val removedVertexes =
        referenceGraph.vertexes -- graph.vertexes

      val removedArcs =
        referenceGraph.arcs -- graph.arcs

      val keptVertexes =
        graph.vertexes -- addedVertexes

      val updatedVertexes =
        keptVertexes
          .filter(vertex => {
            val referenceVertex =
              referenceGraph.getVertexById(vertex.id).get

            vertex.isNewerThan(referenceVertex)
          })

      val keptArcs =
        graph.arcs -- addedArcs

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