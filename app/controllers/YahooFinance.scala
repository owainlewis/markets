package controllers

import io.forward.yahoo.Finance
import play.api.mvc._
import play.api.libs.json.Json
import scalaz.{ -\/, \/- }

class YahooFinance extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  def quote(symbol: String) = Action.async {
    Finance.getQuote(symbol) map {
        // TODO map to correct response type
        case -\/(e) => Ok(e.toString)
        case \/-(q) => Ok(Json.toJson(q))
      }
    }
}
