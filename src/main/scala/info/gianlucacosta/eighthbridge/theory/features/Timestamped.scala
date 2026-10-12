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
  implicit class TimestampedGraphExtension[V <: Vertex with Timestamped, A <: Arc with Timestamped](graph: DirectedGraph[V, A]) {
    def getDiffWith(reference: DirectedGraph[V, A]): TimestampedGraphDiff[V, A] = {
      TimestampedGraphDiff(
        addedVertexes = Set[V](),
        addedArcs = Set[A](),
        newerVertexes = Set[V](),
        newerArcs = Set[A](),
        removedVertexes = Set[V](),
        removedArcs = Set[A]()
      )
    }
  }
}