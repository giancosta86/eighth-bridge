package info.gianlucacosta.eighthbridge.fx.arcnodes

import scalafx.geometry.Point2D

package object plain {
  type SegmentRightClickHandler =
    (Int, Point2D) => Unit
}
