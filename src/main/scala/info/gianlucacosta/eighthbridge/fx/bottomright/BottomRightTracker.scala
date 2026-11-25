package info.gianlucacosta.eighthbridge.fx.bottomright

import scalafx.geometry.Point2D

class BottomRightTracker extends HasBottomRight {
  private var bottomRightByItem: Map[HasBottomRight, Point2D] =
    Map[HasBottomRight, Point2D]()

  private val rightFringe =
    new Fringe(recomputeRight)

  private val bottomFringe =
    new Fringe(recomputeBottom)

  def set(item: HasBottomRight): Unit = {
    val bottomRight =
      item.bottomRight

    bottomRightByItem +=
      (item -> bottomRight)

    rightFringe.notifyItemValue(item, bottomRight.x)

    bottomFringe.notifyItemValue(item, bottomRight.y)
  }

  def remove(item: HasBottomRight): Unit = {
    bottomRightByItem -=
      item

    rightFringe.remove(item)

    bottomFringe.remove(item)
  }

  private def recomputeRight(): Unit = {
    bottomRightByItem.foreach { case (item, bottomRight) =>
      rightFringe.notifyItemValue(item, bottomRight.x)
    }
  }

  private def recomputeBottom(): Unit = {
    bottomRightByItem.foreach { case (item, bottomRight) =>
      bottomFringe.notifyItemValue(item, bottomRight.y)
    }
  }

  override def bottomRight: Point2D =
    new Point2D(
      rightFringe.value,
      bottomFringe.value
    )
}
