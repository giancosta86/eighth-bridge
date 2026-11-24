package info.gianlucacosta.eighthbridge

package object core {
  type AnchorsCreator[V <: Vertex] =
    (V, V) => Anchors[V]
}
