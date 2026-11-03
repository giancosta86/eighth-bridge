package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

/**
 * Topology information for a vertex in a directed graph.
 */
case class VertexTopology[V <: Vertex, A <: Link](
                           predecessors: Set[V],
                           enteringArcs: Set[A],
                           exitingArcs: Set[A],
                           successors: Set[V]
                         )