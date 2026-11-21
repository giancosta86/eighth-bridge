package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.Group

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
    updateSegmentNodes()
  }

  updateSegmentNodes()

  private def updateSegmentNodes(): Unit = {
    val shouldCreateSegmentsFromScratch = {
      (segmentExtremes().size != segments().size + 1) &&
        !(segmentExtremes().isEmpty && segments().isEmpty)
    }

    if (shouldCreateSegmentsFromScratch) {
      segments() =
        ShaftPolyline.buildSegmentNodesFromExtremes(
          segmentExtremes =
            segmentExtremes(),
          onSegmentRightClick =
            onSegmentRightClick
        )
    } else {
      ShaftPolyline.updateSegmentNodesFromExtremes(
        segments =
          segments(),
        segmentExtremes =
          segmentExtremes()
      )
    }
  }
}

private object ShaftPolyline {
  private def buildSegmentNodesFromExtremes(
                                         segmentExtremes: List[Point2D],
                                         onSegmentRightClick: SegmentRightClickHandler
                                       ): List[ShaftSegment] =
    segmentExtremes
      .sliding(2)
      .zipWithIndex
      .map {
        case (startPoint :: endPoint :: Nil, index) =>
          new ShaftSegment(
            onRightClick =
              (point) => {
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

  private def updateSegmentNodesFromExtremes(
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