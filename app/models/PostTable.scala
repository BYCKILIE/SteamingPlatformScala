package models
// AUTO-GENERATED Slick data model for table Post
trait PostTable {

  self:TablesRoot with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Post
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param views Database column views SqlType(int4), Default(Some(0))
   *  @param likes Database column likes SqlType(int4), Default(Some(0))
   *  @param dislikes Database column dislikes SqlType(int4), Default(Some(0))
   *  @param userId Database column user_id SqlType(int8)
   *  @param title Database column title SqlType(varchar), Length(100,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(1000,true), Default(None)
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true), Default(None)
   *  @param path Database column path SqlType(varchar), Length(200,true) */
  case class PostRow(id: Long, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long, title: Option[String] = None, description: Option[String] = None, postingDate: Option[String] = None, path: String)
  /** GetResult implicit for fetching PostRow objects using plain SQL queries */
  implicit def GetResultPostRow(implicit e0: GR[Long], e1: GR[Option[Int]], e2: GR[Option[String]], e3: GR[String]): GR[PostRow] = GR{
    prs => import prs._
    PostRow.tupled((<<[Long], <<?[Int], <<?[Int], <<?[Int], <<[Long], <<?[String], <<?[String], <<?[String], <<[String]))
  }
  /** Table description of table post. Objects of this class serve as prototypes for rows in queries. */
  class Post(_tableTag: Tag) extends profile.api.Table[PostRow](_tableTag, "post") {
    def * = (id, views, likes, dislikes, userId, title, description, postingDate, path).<>(PostRow.tupled, PostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), views, likes, dislikes, Rep.Some(userId), title, description, postingDate, Rep.Some(path))).shaped.<>({r=>import r._; _1.map(_=> PostRow.tupled((_1.get, _2, _3, _4, _5.get, _6, _7, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column views SqlType(int4), Default(Some(0)) */
    val views: Rep[Option[Int]] = column[Option[Int]]("views", O.Default(Some(0)))
    /** Database column likes SqlType(int4), Default(Some(0)) */
    val likes: Rep[Option[Int]] = column[Option[Int]]("likes", O.Default(Some(0)))
    /** Database column dislikes SqlType(int4), Default(Some(0)) */
    val dislikes: Rep[Option[Int]] = column[Option[Int]]("dislikes", O.Default(Some(0)))
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column title SqlType(varchar), Length(100,true), Default(None) */
    val title: Rep[Option[String]] = column[Option[String]]("title", O.Length(100,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(1000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1000,varying=true), O.Default(None))
    /** Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
    val postingDate: Rep[Option[String]] = column[Option[String]]("posting_date", O.Length(16,varying=true), O.Default(None))
    /** Database column path SqlType(varchar), Length(200,true) */
    val path: Rep[String] = column[String]("path", O.Length(200,varying=true))

    /** Foreign key referencing Users (database name post_user_id_fkey) */
    lazy val usersFk = foreignKey("post_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Post */
  lazy val Post = new TableQuery(tag => new Post(tag))
}
