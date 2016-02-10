package domain.models

import io.forward.yahoo.YahooStockResponse
import play.api.libs.json.Json

case class IndexQuote(
  name: String, 
  symbol: String, 
  price: Double, 
  change: Double, 
  dayHigh: Double, 
  dayLow: Double)

object IndexQuote {
  implicit val format = Json.format[IndexQuote]

  def fromYahoo(quote: YahooStockResponse): IndexQuote =
    IndexQuote(
      quote.name, 
      quote.symbol, 
      quote.price.toDouble, 
      quote.change.toDouble, 
      quote.dayHigh.toDouble, 
      quote.dayLow.toDouble)
}
