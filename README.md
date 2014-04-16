# dita-clj

Clojure+leiningen build of
[DITA Open Toolkit (DITA-OT)](https://github.com/dita-ot/dita-ot) java
implemenation of the
[OASIS Darwin Information Typing Architecture (DITA) specification](http://dita.xml.org/),
and XML vocabulary for authoring and publishing.

_**WARNING**_ Pre-alpha - just getting started.  Not usable.  If
you're interested in helping out you can compare the code here with
the DITA-OT code and get the idea.

## Prelims

DITA v 1.2 is [here](http://docs.oasis-open.org/dita/v1.2/spec/DITA1.2-spec.html)

This project is based on Dita-OT, but is not just a wrapper.  It
reorganizes the code to make it more conformant to standard practices
and work with leiningen.  In particular, it replaces the `ant`-based
job control system of DITA-OT with [leiningen](http://leiningen.org/)
tasks implemented in [Clojure](http://clojure.org/).

## Differences from DITA-OT

### Task Management

DITA-OT uses `ant` to manage the DITA processing pipeline.  `ant` is
basically a partial XML encoding of Java syntax.  Broadly speaking,
DITA-OT tasks are encoded as ant tasks, which run Java or XSLT
processes.

dita-clj replaces ant tasks with leiningen tasks, which are
implemented in Clojure.

### Codebase Organization

The primary goal of this project is to clojurize the dita-ot.  But
while we're at it, the current DITA-OT source tree is a little unusual
in some respects, so this project does some reorganizing.  For
example, the `src` tree contains lots of stuff that is not source
code.  It also puts things in strange places; for example,
configuration files in `lib/`, and essential DTD and XSD files in a
plugin subdirectory.

dita-clj tries to normalize, modularize, and simplify things.  The
`src/java` tree contains java source code; `src/dita` contains DITA
document sources.  Documentation goes in the doc directory.  Config
files go in `etc/`.  And so forth.

### User Interface

DITA-OT requires requires that the user run a `startcmd` from within
the installation root directory in order to do any processing.

dita-clj adopts a more orthodox approach which allows the user to easily  run
the tools from any directory.  To process dita source docs, you create
a simple `project.clj` file in the source root directory and then run
`$ lein dita pdf` or `$ lein dita html`, etc.

### XML Catalog

dita-clj follows DITA-OT in using [XML Catalogs](https://www.oasis-open.org/committees/download.php/14809/xml-catalogs.html); see
e.g. [XML file associations with DTDs and XML schemas](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.wst.xmleditor.doc.user%2Ftopics%2Fcxmlcat.html)

The library is Apache Commons'
[xml-resolver](http://xerces.apache.org/xml-commons/components/resolver/index.html).
The catalog files go in `/etc/xml`.  This makes them available to any
application that needs them.  The `CatalogManager.properties` file,
which controls the resolver, goes in `/etc/dita-clj.d` or `~/.dita-clj.d`.

We try to follow standard practice, insofar as there is such a thing.
Examples:
[Debian Catalog Hierarchy](http://debian-xml-sgml.alioth.debian.org/xml-policy/xml-catalog-hierarchy.html)

See also [libxml2 catalog support](http://xmlsoft.org/catalog.html)

### Schema and DTD files

DITA-OT keeps them in a `plugins` subdir.  I don't know why, but since
the DITA schema files are central to the whole enterprise, and are
specific neither to DITA-OT nor to a DITA-OT plugin, we put them in
`/etc/xml`.

### Plugins

The plugin architecture is central to DITA-OT.  But installing a
plugin alters the state of the toolkit itself.  That seems like a bad
idea.  E.g. plugin config files use "pre-defined extension points to
locate changes, and integrates those changes into the core code."  I'm
not sure yet what "integrates .. into core code" means exactly but I
don't much like the sound of it; I should think the better idea is to
extend the core functionality by adding stuff outside, not injecting
outside stuff into it.

To be clear: it's not that adding a plugin should not change some kind
of state, it's just that such mutable data should be isolated from the
immutable "kernel" of the toolkit, and it should be easy to examine
and fix.

**dita-clj support for plugins**: to be determined; see
[doc/extensions.md](doc/extensions.md).

## Usage

When we're done, the process should look something like the following:

    $ lein new dita mydoc
	$ cd mydoc    ## edit stuff
	$ lein dita pdf   ## or html, etc.

## License

The DITA Open Toolkit is licensed for use, at the user's election,
under the
[Common Public License](http://www.opensource.org/licenses/cpl1.0.php)
1.0 (CPL) or
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

The Clojure and leiningen portions of dita-clj are Copyright © 2013
Gregg Reynolds and distributed under the Eclipse Public License either
version 1.0 or (at your option) any later version.

