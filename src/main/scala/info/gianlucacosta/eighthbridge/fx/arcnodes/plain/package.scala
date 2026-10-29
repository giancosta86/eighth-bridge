package info.gianlucacosta.eighthbridge.fx.arcnodes

import info.gianlucacosta.eighthbridge.fx.graph.{LinkHandle, Delta}
import scalafx.geometry.Point2D

package object plain {
  type SegmentRightClickHandler =
    (Int, Point2D) => Unit

  type HandleNodeRightClickHandler =
    (LinkHandle) => Unit

  type HandleNodeDragHandler =
    (LinkHandle, Delta) => Unit
}
