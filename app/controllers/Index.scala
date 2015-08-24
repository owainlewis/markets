package controllers

import io.forward.reuters.{ReutersStockQuote, Reuters}
import play.api.mvc._

class Index extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  import play.api.libs.json._

  implicit val stockFormat = Json.format[ReutersStockQuote]

  def show(symbol: String) = Action.async {
    for {
      response <- Reuters.getQuote(symbol)
    } yield Ok(Json.toJson(response))
  }
}
