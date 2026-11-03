package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.Includes._

private object ShaftPolyline {
  private def buildSegmentsFromExtremes(
                                 segmentExtremes: List[Point2D],
                                 onSegmentRightClick: SegmentRightClickHandler
                               ): List[ShaftSegment] =
    segmentExtremes
      .sliding(2)
      .zipWithIndex
      .map {
        case (startPoint::endPoint::Nil, index) =>
          new ShaftSegment(
            onRightClick = (point) => {
              onSegmentRightClick(index, point)
            }
          ) {
            startX() =
              startPoint.x

            startY() =
              startPoint.y

            endX() =
              endPoint.x

            endY() =
              endPoint.y
          }

        case _ =>
          throw new AssertionError("This must not occur!")
      }
      .toList

  private def updateSegmentsFromExtremes(
                                         segments: List[ShaftSegment],
                                         segmentExtremes: List[Point2D]
                                       ): Unit =
    segments
      .zipWithIndex
      .foreach {
        case (segment, index) =>
          segment.startX() =
            segmentExtremes(index).x

          segment.startY() =
            segmentExtremes(index).y

          segment.endX() =
            segmentExtremes(index + 1).x

          segment.endY() =
            segmentExtremes(index + 1).y
      }
}

private class ShaftPolyline(
                             initialSegmentExtremes: List[Point2D],
                             onSegmentRightClick: SegmentRightClickHandler
                           ) extends Group {
  private val segments =
    new SimpleObjectProperty[List[ShaftSegment]](Nil)

  segments.onChange {
    children.clear()

    segments().foreach(children.add(_))
  }

  val segmentExtremes: ObjectProperty[List[Point2D]] =
    new SimpleObjectProperty[List[Point2D]](initialSegmentExtremes)

  segmentExtremes.onChange {
    setupSegments()
  }

  setupSegments()

  private def setupSegments(): Unit = {
    val shouldRebuildSegments = {
      (segmentExtremes().size != segments().size + 1) &&
        !(segmentExtremes().isEmpty && segments().isEmpty)
    }

    if (shouldRebuildSegments) {
      segments() =
        ShaftPolyline.buildSegmentsFromExtremes(
          segmentExtremes =
            segmentExtremes(),
          onSegmentRightClick =
            onSegmentRightClick
        )
    } else {
      ShaftPolyline.updateSegmentsFromExtremes(
        segments =
          segments(),
        segmentExtremes =
          segmentExtremes()
      )
    }
  }
}
