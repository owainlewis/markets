package controllers

import io.forward.reuters.{ReutersStockQuote, Reuters}
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

sealed case class StockIndex(symbol: String, name: String, region: String)

abstract sealed trait Region
case object US extends Region
case object Europe extends Region

class Index extends Controller {

  implicit val stockFormat  = Json.format[ReutersStockQuote]

  implicit val indexFormat  = Json.format[StockIndex]

  private val stockIndexes = Vector(
    StockIndex("DJI", "Dow Jones Industrial Average", US.toString),
    StockIndex("IXIC", "Nasdaq Composite Index", US.toString),
    StockIndex("SPX", "S&P 500", US.toString),
    StockIndex("FTSE", "FTSE 100 Index", Europe.toString)
  )

  /**
   * Get a list of available stock indexes
   *
   */
  def index() = Action {
    Ok(Json.toJson(stockIndexes))
  }

  /**
   * Show latest data about a single stock index
   *
   * @param symbol A stock symbol i.e DJI
   */
  def show(symbol: String) = Action.async {
    for {
      response <- Reuters.getQuote(symbol)
    } yield Ok(Json.toJson(response))
  }
}
