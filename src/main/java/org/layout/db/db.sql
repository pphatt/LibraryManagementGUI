create database LibraryManagement;
use LibraryManagement;

create table Book
(
    ID           nvarchar(36) primary key,
    Title        nvarchar(200)                      not null,
    Author       nvarchar(36)                       not null,
    Type         integer                            not null,
    Status       nvarchar(30)                       not null,
    YearReleased nvarchar(30)                       not null,
    Description  nvarchar(3000)                     not null,
    Cover        nvarchar(256)                      not null,
    Chapter      integer                            not null,
    State        int check (State = 0 or State = 1) not null,
)

create table Playlist
(
    BookID nvarchar(36),
    DateAdd nvarchar(36),
)

create table Author
(
    ID    nvarchar(36) primary key,
    Name  nvarchar(256)                      not null,
    State int check (State = 0 or State = 1) not null,
)

create table Type
(
    ID   int primary key IDENTITY (1,1),
    Name varchar(256) not null,
)

create table Genre
(
    ID    nvarchar(36) primary key,
    Name  nvarchar(256)                      not null,
    State int check (State = 0 or State = 1) not null,
)

create table BookGenre
(
    BookID  nvarchar(36)                       not null,
    GenreID nvarchar(36)                       not null,
    State   int check (State = 0 or State = 1) not null,
)

alter table Book
    add constraint FK_Book_Author foreign key (Author) references Author (ID);

alter table Book
    add constraint FK_Book_Type foreign key (Type) references Type (ID);

alter table BookGenre
    add constraint FK_BookGenre_Book foreign key (BookID) references Book (ID);

alter table BookGenre
    add constraint FK_BookGenre_Genre foreign key (GenreID) references Genre (ID);