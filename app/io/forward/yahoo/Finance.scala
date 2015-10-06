package io.forward.yahoo

import play.api.libs.ws.{WSResponse, WS}
import scalaz._, Scalaz._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, ExecutionContext}

object Finance {

  import play.api.Play.current

  private val yahooFinance = "https://query.yahooapis.com/v1/public/yql"

  private val quoteTable = "store://datatables.org/alltableswithkeys"
  
  private val historicalData = "yahoo.finance.historicaldata"

  def stockQuoteYQL(symbol: String, table: String = "yahoo.finance.quote"): String =
    s"""select * from $table where symbol=\"$symbol\""""

  def buildRequest(yql: String): Future[WSResponse] =
    WS.url(yahooFinance).withQueryString("q" -> yql, "format" -> "json", "env" -> quoteTable).get()

  /**
   *
   * Get historical stock data
   *
   * @param symbol Stock symbol
   * @param start Start date in format
   * @param end End date in format
   */
  def historicalData(symbol: String, start: String = "2013-01-01", end: String = "2015-01-01"): Future[String] = {
    val query = s"""select * from yahoo.finance.historicaldata where symbol="$symbol" and startDate="$start" and endDate="$end""""
    buildRequest(query) map { response => response.body }
  }

  /**
   * Fetch a YAHOO stock quote
   *
   * @param symbol A stock symbol (YAHOO Finance format) (e.g. Nasdaq => NDX)
   * @param ec An execution context
   */
  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[\/[Int, YahooStockResponse]] = {
    buildRequest(stockQuoteYQL(symbol)) map { response =>
      if (response.status == 200)
        Json.parse(response.body).asOpt[YahooStockResponse] map {
          _.right
        } getOrElse 500.left
      else response.status.left
    }
  }
}
