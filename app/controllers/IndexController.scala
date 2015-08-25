package controllers

import dao.{Index, IndexRepository}
import domain.models.{IndexQuote, StockQuote}
import play.api.cache.Cached
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

class IndexController extends Controller {

  implicit val stockFormat      = Json.format[StockQuote]
  implicit val indexFormat      = Json.format[Index]
  implicit val indexQuoteFormat = Json.format[IndexQuote]

  private val cacheDuration = 1000

  /**
   * Get a list of available stock indexes
   *
   */
  def index() = Cached("index") {
    Action {
      Ok( Json.toJson(IndexRepository.all) )
    }
  }

  /**
   * Show latest data about a single stock index
   *
   * @param symbol A stock symbol i.e DJI
   */

  def show(symbol: String) = Cached.status(_ => symbol, cacheDuration) {
    Action.async {
      IndexRepository.getQuote(symbol) map { response =>
        Ok(Json.toJson(response))
      }
    }
  }
}
