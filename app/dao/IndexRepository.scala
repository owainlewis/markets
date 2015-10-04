package dao

import domain.models.IndexQuote
import io.forward.yahoo.Finance
import scala.concurrent.{ ExecutionContext, Future }
import scalaz.\/

object IndexRepository {

  /**
   * Returns all available stock indexes
   */
  def all: Set[String] = Set(
    "DJI"
  )

  /**
   * Fetch a quote for an index
   *
   * @param symbol A stock index symbol such as DJI
   */
  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[\/[Int, IndexQuote]] = {
    Finance.getQuote("^" ++ symbol) map { _ map IndexQuote.fromYahoo }
  }
}
