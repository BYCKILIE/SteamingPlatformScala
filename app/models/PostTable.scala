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
   *  @param userId Database column user_id SqlType(int8), Default(0)
   *  @param title Database column title SqlType(varchar), Length(100,true)
   *  @param description Database column description SqlType(varchar), Length(1000,true), Default(None)
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true)
   *  @param path Database column path SqlType(varchar), Length(200,true)
   *  @param postType Database column post_type SqlType(varchar), Length(20,true)
   *  @param thumbnail Database column thumbnail SqlType(bytea) */
  case class PostRow(id: Long, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long = 0L, title: String, description: Option[String] = None, postingDate: String, path: String, postType: String, thumbnail: Array[Byte])
  /** GetResult implicit for fetching PostRow objects using plain SQL queries */
  implicit def GetResultPostRow(implicit e0: GR[Long], e1: GR[Option[Int]], e2: GR[String], e3: GR[Option[String]], e4: GR[Array[Byte]]): GR[PostRow] = GR{
    prs => import prs._
    PostRow.tupled((<<[Long], <<?[Int], <<?[Int], <<?[Int], <<[Long], <<[String], <<?[String], <<[String], <<[String], <<[String], <<[Array[Byte]]))
  }
  /** Table description of table post. Objects of this class serve as prototypes for rows in queries. */
  class Post(_tableTag: Tag) extends profile.api.Table[PostRow](_tableTag, "post") {
    def * = (id, views, likes, dislikes, userId, title, description, postingDate, path, postType, thumbnail).<>(PostRow.tupled, PostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), views, likes, dislikes, Rep.Some(userId), Rep.Some(title), description, Rep.Some(postingDate), Rep.Some(path), Rep.Some(postType), Rep.Some(thumbnail))).shaped.<>({r=>import r._; _1.map(_=> PostRow.tupled((_1.get, _2, _3, _4, _5.get, _6.get, _7, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column views SqlType(int4), Default(Some(0)) */
    val views: Rep[Option[Int]] = column[Option[Int]]("views", O.Default(Some(0)))
    /** Database column likes SqlType(int4), Default(Some(0)) */
    val likes: Rep[Option[Int]] = column[Option[Int]]("likes", O.Default(Some(0)))
    /** Database column dislikes SqlType(int4), Default(Some(0)) */
    val dislikes: Rep[Option[Int]] = column[Option[Int]]("dislikes", O.Default(Some(0)))
    /** Database column user_id SqlType(int8), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
    /** Database column title SqlType(varchar), Length(100,true) */
    val title: Rep[String] = column[String]("title", O.Length(100,varying=true))
    /** Database column description SqlType(varchar), Length(1000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1000,varying=true), O.Default(None))
    /** Database column posting_date SqlType(varchar), Length(16,true) */
    val postingDate: Rep[String] = column[String]("posting_date", O.Length(16,varying=true))
    /** Database column path SqlType(varchar), Length(200,true) */
    val path: Rep[String] = column[String]("path", O.Length(200,varying=true))
    /** Database column post_type SqlType(varchar), Length(20,true) */
    val postType: Rep[String] = column[String]("post_type", O.Length(20,varying=true))
    /** Database column thumbnail SqlType(bytea) */
    val thumbnail: Rep[Array[Byte]] = column[Array[Byte]]("thumbnail")

    /** Foreign key referencing Users (database name post_user_id_fkey) */
    lazy val usersFk = foreignKey("post_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Post */
  lazy val Post = new TableQuery(tag => new Post(tag))
}
