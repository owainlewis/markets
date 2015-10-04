package controllers

import dao.IndexRepository
import play.api.Play.current
import play.api.cache.Cached
import play.api.libs.json._
import play.api.mvc._
import scalaz._, Scalaz._

class IndexController extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  def index() = Cached("index") {
    Action {
      Ok( Json.toJson(IndexRepository.all) )
    }
  }

  def show(symbol: String) = Action.async {
    IndexRepository.getQuote(symbol) map {
      case \/-(quote) => Ok(Json.toJson(quote))
      case _          => NotFound("We could not find a quote for " ++ symbol)
    }
  }
}
