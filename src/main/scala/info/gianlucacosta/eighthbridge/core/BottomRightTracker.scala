package info.gianlucacosta.eighthbridge.core

import info.gianlucacosta.eighthbridge.core.facets.HasBottomRight
import info.gianlucacosta.eighthbridge.fx.PointUtils.Point2DExtension
import scalafx.geometry.Point2D

//TODO! To helios-fx?
class BottomRightTracker extends HasBottomRight {
  //TODO! Simplify the notation for empty collections?
  private var bottomRightByItem: Map[HasBottomRight, Point2D] =
    Map[HasBottomRight, Point2D]()

  private var fringe: Set[HasBottomRight] =
    Set[HasBottomRight]()

  private var globalBottomRight: Point2D =
    new Point2D(0, 0)

  def this(items: HasBottomRight*) = {
    this()
    items.foreach(set)
  }

  override def bottomRight: Point2D =
    globalBottomRight

  def set(item: HasBottomRight): Unit = {
    val bottomRight =
      item.bottomRight

    bottomRightByItem +=
      (item -> bottomRight)

    if (
      bottomRight.x > globalBottomRight.x ||
        bottomRight.y > globalBottomRight.y
    ) {
      globalBottomRight =
        bottomRight.mergeToBottomRight(globalBottomRight)

      fringe =
        fringe.filter(fringeItem => {
          val currentBottomRight =
            bottomRightByItem(fringeItem)

          currentBottomRight.x == globalBottomRight.x ||
            currentBottomRight.y == globalBottomRight.y
        }) +
          item
    } else if (
      bottomRight.x == globalBottomRight.x ||
        bottomRight.y == globalBottomRight.y
    ) {
      fringe += item
    } else {
      fringe -= item

      if (fringe.isEmpty) {
        update()
      }
    }
  }

  private def update(): Unit = {
    globalBottomRight =
      new Point2D(0, 0)

    fringe =
      Set[HasBottomRight]()

    bottomRightByItem
      .foreach{ case (item, bottomRight) =>
        val rightPassed =
          bottomRight.x > globalBottomRight.x

        val bottomPassed =
          bottomRight.y > globalBottomRight.y

        if (rightPassed || bottomPassed) {
          if (rightPassed && bottomPassed) {
            globalBottomRight =
              bottomRight

            fringe =
              Set(item)
          } else if (rightPassed) {
            globalBottomRight =
              new Point2D(
                bottomRight.x,
                globalBottomRight.y
              )

            fringe =
              fringe
                .filter(currentItem =>
                  bottomRightByItem(currentItem).y == globalBottomRight.y
                ) + item
          } else if (bottomPassed) {
            globalBottomRight =
              new Point2D(
                globalBottomRight.x,
                bottomRight.y
              )

            fringe =
              fringe
                .filter(currentItem =>
                  bottomRightByItem(currentItem).x == globalBottomRight.x
                ) + item
          }
        }
      }
  }

  def remove(item: HasBottomRight): Unit = {
    bottomRightByItem -=
      item

    fringe -=
      item

    if (fringe.isEmpty) {
      update()
    }
  }
}
