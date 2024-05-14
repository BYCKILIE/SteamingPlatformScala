package models
// AUTO-GENERATED Slick data model for table ApplicationPost
trait ApplicationPostTable {

  self:TablesRoot  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table ApplicationPost
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(varchar), Length(100,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(1000,true), Default(None)
   *  @param views Database column views SqlType(int4), Default(Some(0))
   *  @param likes Database column likes SqlType(int4), Default(Some(0))
   *  @param dislikes Database column dislikes SqlType(int4), Default(Some(0))
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true), Default(None)
   *  @param path Database column path SqlType(varchar), Length(200,true) */
  case class ApplicationPostRow(id: Long, title: Option[String] = None, description: Option[String] = None, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), postingDate: Option[String] = None, path: String)
  /** GetResult implicit for fetching ApplicationPostRow objects using plain SQL queries */
  implicit def GetResultApplicationPostRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[String]): GR[ApplicationPostRow] = GR{
    prs => import prs._
    ApplicationPostRow.tupled((<<[Long], <<?[String], <<?[String], <<?[Int], <<?[Int], <<?[Int], <<?[String], <<[String]))
  }
  /** Table description of table application_post. Objects of this class serve as prototypes for rows in queries. */
  class ApplicationPost(_tableTag: Tag) extends profile.api.Table[ApplicationPostRow](_tableTag, "application_post") {
    def * = (id, title, description, views, likes, dislikes, postingDate, path).<>(ApplicationPostRow.tupled, ApplicationPostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), title, description, views, likes, dislikes, postingDate, Rep.Some(path))).shaped.<>({r=>import r._; _1.map(_=> ApplicationPostRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(varchar), Length(100,true), Default(None) */
    val title: Rep[Option[String]] = column[Option[String]]("title", O.Length(100,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(1000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1000,varying=true), O.Default(None))
    /** Database column views SqlType(int4), Default(Some(0)) */
    val views: Rep[Option[Int]] = column[Option[Int]]("views", O.Default(Some(0)))
    /** Database column likes SqlType(int4), Default(Some(0)) */
    val likes: Rep[Option[Int]] = column[Option[Int]]("likes", O.Default(Some(0)))
    /** Database column dislikes SqlType(int4), Default(Some(0)) */
    val dislikes: Rep[Option[Int]] = column[Option[Int]]("dislikes", O.Default(Some(0)))
    /** Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
    val postingDate: Rep[Option[String]] = column[Option[String]]("posting_date", O.Length(16,varying=true), O.Default(None))
    /** Database column path SqlType(varchar), Length(200,true) */
    val path: Rep[String] = column[String]("path", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table ApplicationPost */
  lazy val ApplicationPost = new TableQuery(tag => new ApplicationPost(tag))
}
