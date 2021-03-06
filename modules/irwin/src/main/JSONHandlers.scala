package lila.irwin

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

object JSONHandlers {

  import IrwinReport._
  private implicit val moveReportReader = Json.reads[MoveReport]
  private implicit val gameReportReader = Json.reads[GameReport]

  private implicit val pvReader = Reads[Option[Int]] {
    case JsNumber(n) if n.isValidInt => JsSuccess(n.toInt.some)
    case _ => JsSuccess(None)
  }

  implicit val reportReader: Reads[IrwinReport] = (
    (__ \ "userId").read[String] and
    (__ \ "isLegit").readNullable[Boolean] and
    (__ \ "activation").read[Int] and
    (__ \ "games").read[List[GameReport]] and
    (__ \ "pv0ByAmbiguity").read[List[Option[Int]]] and
    Reads(_ => JsSuccess(DateTime.now))
  )(IrwinReport.apply _)
}
