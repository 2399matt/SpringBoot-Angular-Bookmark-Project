-- bookmarks.bookmark definition
CREATE DATABASE IF NOT EXISTS `bookmarks`;
USE `bookmarks`;
CREATE TABLE `bookmark` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `title` varchar(145) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `url` varchar(100) NOT NULL,
                            `created_at` date DEFAULT NULL,
                            `views` int DEFAULT NULL,
                            `favicon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `user_id` varchar(100) NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- bookmarks.tag definition

CREATE TABLE `tag` (
                       `id` bigint NOT NULL AUTO_INCREMENT,
                       `name` varchar(45) NOT NULL,
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- bookmarks.bookmark_tag definition

CREATE TABLE `bookmark_tag` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `bookmark_id` bigint NOT NULL,
                                `tag_id` bigint NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `bookmark_tag_bookmark_FK` (`bookmark_id`),
                                KEY `bookmark_tag_tag_FK` (`tag_id`),
                                CONSTRAINT `bookmark_tag_bookmark_FK` FOREIGN KEY (`bookmark_id`) REFERENCES `bookmark` (`id`),
                                CONSTRAINT `bookmark_tag_tag_FK` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO tag (name) VALUES ('entertainment');
INSERT INTO tag (name) VALUES ('gaming');
INSERT INTO tag (name) VALUES ('programming');
INSERT INTO tag (name) VALUES ('school');
INSERT INTO tag (name) VALUES ('news');
INSERT INTO tag (name) VALUES ('food');
INSERT INTO tag (name) VALUES ('movies');
INSERT INTO tag (name) VALUES ('videos');
INSERT INTO tag (name) VALUES ('shows');
INSERT INTO tag (name) VALUES ('misc');