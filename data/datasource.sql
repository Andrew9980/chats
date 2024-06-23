
create database chats;

use chats;

create table user_info
(
    id                 bigint auto_increment,
    user_id            varchar(12)  not null comment '',
    password           varchar(50)  not null comment '',
    salt               varchar(8)   not null comment '',
    email varchar(50) not null comment '',
    nick_name          varchar(50)  not null comment '',
    personal_signature varchar(100) not null comment '',
    sex                tinyint(1) comment '',
    birthday           date comment '',
    area_id            long comment '',
    area_name          varchar(50) comment '',
    status             tinyint(1) comment '',
    last_login_time    datetime comment '',
    create_time        datetime,
    update_time        datetime,
    yn tinyint(1) comment '',
    primary key (id),
    unique key (email),
    unique key (user_id)
) comment '用户信息' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table user_contact
(
    id bigint auto_increment,
    user_id varchar(12) not null comment 'user_info表user_id',
    contact_id varchar(12) not null comment '联系人id type=1 user_info表user_id type=2 group表group_id',
    notes varchar(50) comment '联系人/群备注',
    type tinyint(1) not null comment '联系类型，1：联系用户 2：联系群',
    status tinyint not null default 0 comment '联系状态，0：申请中，1：正常 2：拉黑 3：退出群聊',
    unread_count int default 0 comment '消息未读数',
    last_contract_time datetime comment '最后联系时间',
    create_time datetime comment '',
    update_time datetime comment '',
    primary key (id),
    unique key (user_id, contact_id)
) comment '用户联系' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table message
(
    id bigint auto_increment,
    sender_id varchar(12) not null comment '发送者',
    receive_id varchar(12) not null comment '接收者',
    status tinyint(1) not null comment '1：未读 2：已读',
    type tinyint not null comment '消息类型，0：好友申请，1：群申请 2：文字 3：图片 4：文件',
    content text comment '消息内容',
    create_time datetime,
    update_time datetime,
    primary key (id),
    index s_idx(sender_id),
    index r_idx(receive_id)
) comment '消息表' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table chat_group
(
    id bigint auto_increment,
    group_id varchar(12) not null comment '群id',
    group_name varchar(50) not null comment '群名称',
    group_info varchar(200) comment '群信息',
    type tinyint(1) default 0 comment '群类型',
    search tinyint(1) default 1 comment '是否可被搜索 0：不可搜索 1：可搜索',
    cur_count smallint default 1 comment '当前人数',
    max_count smallint default 500 comment '最大群人数',
    status tinyint(1) default 1 comment '1：正常，2：封禁',
    create_time datetime,
    update_time datetime,
    primary key (id),
    unique key (group_id)
) comment '群' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table group_member
(
    id bigint auto_increment,
    group_id varchar(12) not null comment '群id',
    user_id varchar(12) not null comment '用户id',
    user_role tinyint(1) not null comment '群成员权限，1：群主 2：管理员 3：群员',
    join_time datetime not null comment '加入时间',
    status tinyint(1) not null comment '状态，1：正常 2：禁言 3：被踢',
    muted_time datetime comment '禁言生效时间',
    create_time datetime,
    update_time datetime,
    primary key id(id),
    unique key (group_id, user_id)
) comment '群成员' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
