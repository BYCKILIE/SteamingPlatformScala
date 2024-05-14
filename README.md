Branch:
  - main:
      L   documentation
  - backend:
      L    server
      L    security
      L    video stream functionality
  - frontend:
      L    website design
      L    video player
  - pyclient:
      L    live streaming UI
  - video_breaker:
      L    app to bring the uploaded videos to the format use in the app


Backend:
  - Framework: This project is built upon the Scala Play 2 framework.

  - Database Integration: PostgreSQL, is seamlessly integrated into the project for efficient data storage and retrieval.

  - Testing: The project incorporates "org.scalatestplus.play" for comprehensive testing, ensuring the reliability and robustness of the codebase.

  - Database Operations: "com.typesafe.play" and "com.typesafe.slick" are utilized for database operations, offering a smooth and intuitive interface for interacting with the PostgreSQL database.

  - JSON Serialization: "circe" for JSON serialization, enabling efficient data handling and exchange within the application, check app/utilities/JsonOp.scala and the DTO package.

  - Security: Prioritizing security, "org.mindrot" is employed for password hashing using bcrypt, ensuring robust protection of sensitive user data such as password management.

  - Scalability: With the use of "com.typesafe.slick" for database connection pooling via "slick-hikaricp," the project is designed to scale efficiently, handling increasing loads with ease.

  - Flexibility: The project architecture allows for easy customization and adaptation to evolving requirements, ensuring long-term viability and maintainability.

  - Data Pipeline: Tables -> (Services with Repositories) + DTO -> Controllers -> "URL"

  - Security: BouncyCastle asymemtric encryption library

  - Authentication: Token based auth generated with pdi.jwt
