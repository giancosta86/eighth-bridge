package info.gianlucacosta.eighthbridge.fx

import scalafx.scene.layout.Region

//TODO! To helios-fx!
object RegionUtils {
 implicit class RegionExtensions(self: Region) {
   def basicBorderRadius: Double =
     Option(self.border.value)
       .filter(!_.getStrokes.isEmpty)
       .map(
         _.getStrokes
           .get(0)
           .getRadii
           .getTopLeftHorizontalRadius
       )
       .getOrElse(0)
 }
}
