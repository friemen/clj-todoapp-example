# todoapp

An architectural prototype demonstrating a full-stack single-page
application (SPA) built on top of React with a SQL-DB based backend.

Aspects shown here include:

* Usage of re-frame, Reagent

* Client-side routing

* A JSON API service layer

* A Transit based UI service layer

* Transactional DB access with a connection pool

* Usage of a central configuration file


## TODOs

* Add frontend routing and another page as example.

* Add a main function

* Add standard routes and middlewares (tx, exception, context, friend).

* Add API Service infrastructure.


## Usage

After starting the system you can access the in-process H2 DB
via http://localhost:8082 with user `sa` and empty password.

The schema is

```
create table todo (id int primary key auto_increment,
                   position int,
				   label varchar(250),
				   done bool);
```

To work interactively on your .sass stylesheet open a shell, cd into
your project folder and start

`sass --watch src/sass/stylesheet.sass resources/public/css`

To start a REPL use `lein repl` in some other shell.

After connecting to your REPL use `(user/system-*)` commands to
start/stop/restart the backend.

Use `(user/start-figwheel!)` to start Figwheel (an interactive,
incremental Cljs compiler). Figwheel will also hotload your CSS into
the browser.

Use `(user/cljs-repl)` to connect your REPL to the browser.


To interact with the database you can use expressions like

`(jdbc/query (:db user/system) ["select * from todo"])`.


## License

Copyright © 2019 Falko Riemenschneider

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
