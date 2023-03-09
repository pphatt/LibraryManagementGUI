create database LibraryManagement;
use LibraryManagement;

create table Book
(
    ID           nvarchar(36) primary key,
    Title        nvarchar(200)                      not null,
    Author       nvarchar(36)                       not null,
    Genre        nvarchar(36)                       not null,
    Status       nvarchar(30)                       not null,
    YearReleased nvarchar(30)                       not null,
    Description  nvarchar(3000)                      not null,
    Cover        nvarchar(256)                      not null,
    Chapter      integer                            not null,
    State        int check (State = 0 or State = 1) not null,
)

create table Author
(
    ID    nvarchar(36) primary key,
    Name  nvarchar(256)                      not null,
    State int check (State = 0 or State = 1) not null,
)

create table Genre
(
    ID    nvarchar(36) primary key,
    Name  nvarchar(256)                      not null,
    State int check (State = 0 or State = 1) not null,
)

alter table Book
    add constraint FK_Book_Author foreign key (Author) references Author (ID);

alter table Book
    add constraint FK_Book_Genre foreign key (Genre) references Genre (ID);

insert into Genre (ID, Name, State) values (1, 'manga', '0');

Select * from Author where Author.ID = 1