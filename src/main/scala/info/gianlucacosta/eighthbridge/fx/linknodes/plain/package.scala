package info.gianlucacosta.eighthbridge.fx.linknodes

import info.gianlucacosta.eighthbridge.fx.{Delta2D, LinkHandle}
import scalafx.geometry.Point2D

package object plain {
  type SegmentIndex =
    Int

  type SegmentRightClickHandler =
    (SegmentIndex, Point2D) => Unit

  type HandleRightClickHandler =
    (LinkHandle) => Unit

  type HandleDragHandler =
    (LinkHandle, Delta2D) => Unit
}
