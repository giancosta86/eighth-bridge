package info.gianlucacosta.eighthbridge.core

import info.gianlucacosta.eighthbridge.core.facets.HasBottomRight
import scalafx.geometry.Point2D

private class BottomRightFringe(onEmpty: () => Unit) {
  private var _value: Double =
    0

  //TODO! Simplify the notation for empty collections?
  private var items: Set[HasBottomRight] =
    Set[HasBottomRight]()

  def value: Double =
    _value

  def notify(item: HasBottomRight, itemValue: Double): Unit = {
    Math.signum(itemValue - _value) match {
      case 1 =>
        items =
          Set(item)

        _value =
          itemValue

      case 0 =>
        items +=
          item

      case _ =>
        this -=
          item
    }
  }

  def -=(item: HasBottomRight): Unit = {
    items -=
      item

    if (items.isEmpty) {
      _value =
        0

      onEmpty
    }
  }
}

//TODO! To helios-fx?
class BottomRightTracker extends HasBottomRight {
  private var bottomRightByItem: Map[HasBottomRight, Point2D] =
    Map[HasBottomRight, Point2D]()

  private val rightFringe =
    new BottomRightFringe(recomputeRight)

  private val bottomFringe =
    new BottomRightFringe(recomputeBottom)

  def this(items: HasBottomRight*) = {
    this()
    items.foreach(set)
  }

  final def set(item: HasBottomRight): Unit = {
    val bottomRight =
      item.bottomRight

    bottomRightByItem +=
      (item -> bottomRight)

    rightFringe.notify(item, bottomRight.x)

    bottomFringe.notify(item, bottomRight.y)
  }

  final def remove(item: HasBottomRight): Unit = {
    rightFringe -=
      item

    bottomFringe -=
      item
  }

  private def recomputeRight(): Unit = {
    bottomRightByItem.foreach { case (item, bottomRight) =>
      rightFringe.notify(item, bottomRight.x)
    }
  }

  private def recomputeBottom(): Unit = {
    bottomRightByItem.foreach { case (item, bottomRight) =>
      bottomFringe.notify(item, bottomRight.y)
    }
  }

  override def bottomRight: Point2D =
    new Point2D(
      rightFringe.value,
      bottomFringe.value
    )
}
