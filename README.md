# todoapp

An architectural prototype demonstrating a full-stack single-page
application (SPA) built on top of React with a SQL-DB based backend.

Aspects shown here include:

* Usage of re-frame, Reagent

* Client-side routing

* A Transit based UI service layer

* Transactional DB access with a connection pool

* Application configuration, incl. logging

* Interactive programming with Figwheel


## Yet to be added

* A details page to better demonstrate routing

* Login page and authentication based on friend

* A JSON API service layer


## Usage

You'll need [Leiningen](https://leiningen.org) and a Java >=8
[JDK](https://openjdk.java.net/projects/jdk8/). If you want to work
with Sass instead of plain CSS then [Dart
Sass](https://sass-lang.com/dart-sass) would be a good option.

Clone this project.

You'll first want to start a REPL. How you do it depends on how you
prefer to work with Clojure. I'm using [a personal setup of
Emacs](https://github.com/friemen/emacsd) and execute `lein repl` in
some Linux shell.  Then I connect to it.

After connecting to your REPL use `(user/system-*)` commands to
start/stop/restart the backend.

After starting the system you can access the in-process [H2
DB](https://www.h2database.com)  via http://localhost:8082 with user
`sa` and empty password.

The schema is

```
create table todo (id int primary key auto_increment,
                   position int,
                   label varchar(250),
                   done bool);
```


The `user` namespace contains a `reset-db!` function which drops all
tables and re-creates them. Execute it to initialise the DB.

To interact freely with the database from within the REPL you can use
expressions like

`(jdbc/query (:db user/system) ["select * from todo"])`

or

`(jdbc/execute! (:db user/system) ["insert into todo (position, label) values (?,?)" 0 "Repair my bike"])`.


Use `(user/start-figwheel!)` to start
[Figwheel](https://github.com/bhauman/lein-figwheel) (an interactive,
incremental Cljs compiler). Figwheel will also hotload your CSS into
the browser. It is advisable to use Chrome or Chromium for development
purposes.

Use `(user/cljs-repl)` to connect your REPL to the browser.

To work interactively on your `.sass` stylesheet open a shell, cd into
your project folder and start

`sass --watch src/sass/stylesheet.sass resources/public/css`

Everytime you save a `.sass` file, Sass will recompile it to CSS and
Figwheel will update the browser.


To build a shippable Jar type `lein uberjar`.

(After an uberjar build it's advisable to clean target and
resources/public/js folders before starting the REPL. In fact, I use
a bash alias for `remove -rf target resources/public/js && lein repl` to
make sure I'm always starting clean.)


To start the Jar from a shell a command like

`java -Dde.sample.configfile=path/to/your/config.edn -jar todoapp.jar`.

should do. The default `config.edn` is in `resources/etc`.


## License

Copyright © 2019 Falko Riemenschneider

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
