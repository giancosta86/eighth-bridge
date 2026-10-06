package info.gianlucacosta.eighthbridge.fx.canvas

object LinkArrow {
  val Default = LinkArrow(
    angle = math.Pi / 6,
    length = 15
  )
}


/**
  * Link arrow
  */
case class LinkArrow(
                      angle: Double,
                      length: Double
                    )
