package services

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import models.Tables._
import play.api.db.slick.DatabaseConfigProvider
import repositories.PostRepository
import slick.jdbc.JdbcProfile
import DTO.PostDTO
import java.util.Base64

@Singleton
class PostService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext)
 extends PostRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def listPosts: Future[Seq[PostDTO]] = {
    val query = Post.result
    db.run(query).map { posts =>
      posts.map { post =>
        PostDTO(
          id = post.id,
          userId = post.userId,
          title = post.title,
          views = post.views,
          likes = post.likes,
          dislikes = post.dislikes,
          description = post.description,
          postingDate = post.postingDate,
          path = post.path,
          thumbnail = java.util.Base64.getEncoder.encodeToString(post.thumbnail),
          postType = post.postType
        )
      }
    }
  }

  def createPost(post: PostDTO): Future[Long] = {
    db.run(
      (Post.returning(Post.map(_.id))) += PostRow(
        id = 0,
        userId = post.userId,
        title = post.title,
        description = post.description,
        postingDate = post.postingDate,
        path = post.path,
        thumbnail = Base64.getDecoder.decode(post.thumbnail),
        postType = post.postType
      )
    )
  }

  def readPost(id: Long): Future[PostDTO] = {
    db.run(Post.filter(_.id === id).result.headOption).flatMap {
      case Some(post) =>
        Future.successful(PostDTO(
          id = post.id,
          userId = post.userId,
          title = post.title,
          views = post.views,
          likes = post.likes,
          dislikes = post.dislikes,
          description = post.description,
          postingDate = post.postingDate,
          path = post.path,
          thumbnail = Base64.getEncoder.encodeToString(post.thumbnail),
          postType = post.postType
        ))
      case None =>
        Future.failed(new NoSuchElementException(s"Post with id $id not found"))
    }
  }

  def updatePost(id: Long, updatedPost: PostDTO): Future[Boolean] = {
    db.run(Post.filter(_.id === id).result.headOption).flatMap {
      case Some(existingUser) =>
        val updated = existingUser.copy(
          title = updatedPost.title,
          description = updatedPost.description,
          thumbnail = Base64.getDecoder.decode(updatedPost.thumbnail),
        )
        db.run(Post.filter(_.id === id).update(updated)).map(_ > 0)
      case None =>
        Future.successful(false)
    }
  }

  def deletePost(id: Long): Future[Boolean] = {
    db.run((for {
      _       <- Comments.filter(_.postId === id).delete
      deleted <- Post.filter(_.id === id).delete
    } yield deleted > 0).transactionally)
  }

}
