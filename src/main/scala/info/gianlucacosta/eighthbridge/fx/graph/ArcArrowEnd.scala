package info.gianlucacosta.eighthbridge.fx.graph

object ArcArrowEnd {
  val Default = ArcArrowEnd(
    angle = math.Pi / 6,
    length = 15
  )
}

case class ArcArrowEnd(
                      angle: Double,
                      length: Double
                    )
