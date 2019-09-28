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
[JDK](https://openjdk.java.net/projects/jdk8/).

Clone this project.

You'll first want to start a REPL. How you do it depends on how you
prefer to work with Clojure. I'm using Emacs and execute `lein
repl` in some Linux shell.  Then I connect to it.

After connecting to your REPL use `(user/system-*)` commands to
start/stop/restart the backend.

After starting the system you can access the in-process H2 DB
via http://localhost:8082 with user `sa` and empty password.

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


Use `(user/start-figwheel!)` to start Figwheel (an interactive,
incremental Cljs compiler). Figwheel will also hotload your CSS into
the browser.

Use `(user/cljs-repl)` to connect your REPL to the browser.

To work interactively on your `.sass` stylesheet open a shell, cd into
your project folder and start

`sass --watch src/sass/stylesheet.sass resources/public/css`

Everytime you save a `.sass` file, SASS will


To build a shippable uberjar type `lein uberjar`.


(After an uberjar build it's advisable to clean target and
resources/public/js folders before starting the REPL. In fact, I use
a bash alias for `remove -rf target resources/public/js && lein repl` to
make sure I'm always starting clean.)


## License

Copyright © 2019 Falko Riemenschneider

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
