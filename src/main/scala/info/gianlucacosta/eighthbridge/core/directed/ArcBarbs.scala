package info.gianlucacosta.eighthbridge.core.directed

case class ArcBarbs(
                     angleInDegrees: Double,
                     length: Double
                   ) {
  require(0 < angleInDegrees && angleInDegrees < 90)

  require(length > 0)
}

object ArcBarbs {
  val Default = ArcBarbs(
    angleInDegrees = 30,
    length = 15
  )
}
