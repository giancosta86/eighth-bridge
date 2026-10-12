package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{Arc, Vertex}

case class TimestampedGraphDiff[V <: Vertex with Timestamped, A <: Arc with Timestamped](
                                                        addedVertexes: Set[V],
                                                        addedArcs: Set[A],
                                                        newerVertexes: Set[V],
                                                        newerArcs: Set[A],
                                                        removedVertexes: Set[V],
                                                        removedArcs: Set[A]
                               )