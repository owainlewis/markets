package io.forward.yahoo

import play.api.libs.ws.{WSResponse, WS}
import scalaz._, Scalaz._
import play.api.libs.json.Json

import scala.concurrent.{Future, ExecutionContext}

object Finance {

  import play.api.Play.current

  private val yahooFinance = "https://query.yahooapis.com/v1/public/yql"

  private val table = "store://datatables.org/alltableswithkeys"

  def stockQuoteYQL(symbol: String, table: String = "yahoo.finance.quote"): String =
    s"""select * from $table where symbol=\"$symbol\""""

  def buildRequest(yql: String): Future[WSResponse] =
    WS.url(yahooFinance).withQueryString("q" -> yql, "format" -> "json", "env" -> table).get()

  /**
   * Fetch a YAHOO stock quote
   *
   * @param symbol A stock symbol (YAHOO Finance format) (e.g. Nasdaq => ^NDX)
   * @param ec An execution context
   */
  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[\/[Int, YahooStockResponse]] = {
    buildRequest(stockQuoteYQL(symbol)) map { response =>
      if (response.status == 200)
        Json.parse(response.body).as[YahooStockResponse].right
      else response.status.left
    }
  }
}
