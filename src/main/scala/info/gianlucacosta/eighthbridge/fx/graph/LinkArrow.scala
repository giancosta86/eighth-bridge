package info.gianlucacosta.eighthbridge.fx.graph

//TODO! In the end, rename this to LinkArrowEnd?
object LinkArrow {
  val Default = LinkArrow(
    angle = math.Pi / 6,
    length = 15
  )
}

case class LinkArrow(
                      angle: Double,
                      length: Double
                    )
