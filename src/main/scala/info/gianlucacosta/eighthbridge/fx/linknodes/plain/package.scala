package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.{Delta2D, LinkHandle}
import scalafx.geometry.Point2D

package object plain {
  type SegmentIndex =
    Int

  type SegmentRightClickHandler =
    (SegmentIndex, Point2D) => Unit

  type HandleNodeRightClickHandler =
    (LinkHandle) => Unit

  type HandleNodeDragHandler =
    (LinkHandle, Delta2D) => Unit
}
