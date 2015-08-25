package dao

import domain.models.{IndexQuote, Europe, US, Region}
import io.forward.reuters.Reuters

import scala.concurrent.{ ExecutionContext, Future }

case class Index(symbol: String, name: String, region: String)

object IndexRepository {

  /**
   * Returns all available stock indexes
   */
  def all: Vector[Index] = Vector(
    Index("DJI", "Dow Jones Industrial Average", US.toString),
    Index("IXIC", "Nasdaq Composite Index", US.toString),
    Index("SPX", "S&P 500", US.toString),
    Index("FTSE", "FTSE 100 Index", Europe.toString)
  )

  /**
   * Fetch a quote for an index
   *
   * @param symbol A stock index symbol such as FTSE or DJI
   */
  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[Option[IndexQuote]] = {
    Reuters.getQuote(symbol) map { maybeQuote =>
      maybeQuote map (IndexQuote(symbol, "", _))
    }
  }
}
