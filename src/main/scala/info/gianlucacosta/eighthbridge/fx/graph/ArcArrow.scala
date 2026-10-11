package info.gianlucacosta.eighthbridge.fx.graph

//TODO! In the end, rename this to ArcArrowEnd?
object ArcArrow {
  val Default = ArcArrow(
    angle = math.Pi / 6,
    length = 15
  )
}

case class ArcArrow(
                      angle: Double,
                      length: Double
                    )
