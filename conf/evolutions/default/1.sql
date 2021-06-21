# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigint auto_increment not null,
  value                         varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_category primary key (id)
);

create table difficult (
  id                            bigint auto_increment not null,
  value                         varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_difficult primary key (id)
);

create table ingredient (
  id                            bigint auto_increment not null,
  recipe_id                     bigint not null,
  name                          varchar(255),
  type_id                       bigint,
  calories                      double not null,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint uq_ingredient_type_id unique (type_id),
  constraint pk_ingredient primary key (id)
);

create table ingredient_type (
  id                            bigint auto_increment not null,
  value                         varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_ingredient_type primary key (id)
);

create table recipe (
  id                            bigint auto_increment not null,
  user_id                       bigint not null,
  name                          varchar(255),
  description                   varchar(100000),
  duration_in_hours             double not null,
  difficult_id                  bigint,
  category_id                   bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint uq_recipe_difficult_id unique (difficult_id),
  constraint uq_recipe_category_id unique (category_id),
  constraint pk_recipe primary key (id)
);

create table subcategory (
  id                            bigint auto_increment not null,
  recipe_id                     bigint not null,
  value                         varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_subcategory primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_user primary key (id)
);

create index ix_ingredient_recipe_id on ingredient (recipe_id);
alter table ingredient add constraint fk_ingredient_recipe_id foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;

alter table ingredient add constraint fk_ingredient_type_id foreign key (type_id) references ingredient_type (id) on delete restrict on update restrict;

create index ix_recipe_user_id on recipe (user_id);
alter table recipe add constraint fk_recipe_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table recipe add constraint fk_recipe_difficult_id foreign key (difficult_id) references difficult (id) on delete restrict on update restrict;

alter table recipe add constraint fk_recipe_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;

create index ix_subcategory_recipe_id on subcategory (recipe_id);
alter table subcategory add constraint fk_subcategory_recipe_id foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;


# --- !Downs

alter table ingredient drop constraint if exists fk_ingredient_recipe_id;
drop index if exists ix_ingredient_recipe_id;

alter table ingredient drop constraint if exists fk_ingredient_type_id;

alter table recipe drop constraint if exists fk_recipe_user_id;
drop index if exists ix_recipe_user_id;

alter table recipe drop constraint if exists fk_recipe_difficult_id;

alter table recipe drop constraint if exists fk_recipe_category_id;

alter table subcategory drop constraint if exists fk_subcategory_recipe_id;
drop index if exists ix_subcategory_recipe_id;

drop table if exists category;

drop table if exists difficult;

drop table if exists ingredient;

drop table if exists ingredient_type;

drop table if exists recipe;

drop table if exists subcategory;

drop table if exists user;

