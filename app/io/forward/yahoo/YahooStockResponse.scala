package io.forward.yahoo

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class YahooStockResponse(
  name: String,
  symbol: String,
  price: String, // todo type mapping
  change: String,
  dayLow: String,
  dayHigh: String,
  yearLow: String,
  yearHigh: String)

object YahooStockResponse {

  implicit val yahooStockResponseReads: Reads[YahooStockResponse] = (
    (JsPath \ "query" \ "results" \ "quote" \ "Name").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "Symbol").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "LastTradePriceOnly").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "Change").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "DaysLow").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "DaysHigh").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "YearLow").read[String] and
    (JsPath \ "query" \ "results" \ "quote" \ "YearHigh").read[String]
  )(YahooStockResponse.apply _)

  implicit val yahooStockResponseWrites = Json.writes[YahooStockResponse]
}
