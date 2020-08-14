drop table IF EXISTS `person`;

create table `person` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `score` int(11) NOT NULL,
    `create_time` timestamp  NOT NULL,
    PRIMARY KEY (`id`),
    key `name_score` (`name`, `score`) using BTREE,
    key `create_time` (`create_time`) using BTREE
) ENGINE = InnoDB DEFAULT CHARACTER  = utf8mb4;