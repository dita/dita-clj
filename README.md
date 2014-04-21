# dita-clj

Clojure+leiningen build of
[DITA Open Toolkit (DITA-OT)](https://github.com/dita-ot/dita-ot) java
implemenation of the
[OASIS Darwin Information Typing Architecture (DITA) specification](http://dita.xml.org/),
and XML vocabulary for authoring and publishing.

# CURRENT STATUS (2014-04-20)

_**WARNING**_ Pre-alpha - just getting started.  Not usable.  If
you're interested in helping out you can compare the code here with
the DITA-OT code and get the idea.

_**CAVEAT**_ None of this will make a bit of sense to you if you are
not already familiar with DITA-OT and Clojure, but never fear: I've
taken more-or-less extensive notes on my excavations and put them in
[doc](https://github.com/dita/dita-clj/tree/master/doc).

That said, there's enough to establish plausiblity.  Of a sort: I've
managed to run the first preprocessing step gen-list without any of
the Ant cruft.  In about a dozen lines of Clojure code, in fact.  I'm
not saying it runs _correctly_; I'm still figuring out the gory
details of DITA-OT config.  But it runs and produces output ion the
TMP dir.  If you want to play along, fork and clone the project, then
from the project root do:

    $ lein run

**CAVEAT** Since I'm still experimenting, don't be too disappointed if
  it doesn't work when you try it.  But trust me, it's possible to
  make it go.  If you want to play along, let me know and I'll try to
  be more responsible.

The toolkit java code is in `src/java`.  It's taken straight from the
[horse's mouth](https://github.com/dita-ot/dita-ot/tree/develop/src/main/java).
Other bits, I've rearranged, putting them in `resources`, with a view
of eventually following orthodox leiningen/clojure practices.  I'm
using `src/clj/dita/core.clj` for exploration.  **Once I get it all
figured out, core.clj (etc.) will turn into a leiningen plugin for
running dita jobs.**

So far I've managed to get the first pre-processing step (gen-list) to
run.  But I'm stuck on `org/dita/dost/util/Configuration.java`, which
I want (for now) to replace by a Clojure gen-class, rather than
modifying the java class.  (See
[configuration.md](https://github.com/dita/dita-clj/blob/master/doc/configuration.md).)
Here's why: everything in Configuration.java is final static, which
means everything is fixed at compile time.  It reads config data at
compile time, stuffs it into Maps etc. and then responds to `get`
requests at runtime.  At least I think that's how it works.  In any
case, I want configuration to be specified at runtime, via a map in
project.clj or some other config file, such as an EDN file.  I could
just change the java Configuration.java implementation, but instead
mucking about with the DITA-OT kernel source I'd rather make a Clojure
implementation available through a Java interface/protocol.  The idea
is simple: the Clojure implementation reads config maps or edn files,
etc. at runtime, and responds to `get` requests just like the original
Configuration object.

The hair in the pepsi is that we want our clojure code to override the
java code by structuring the classpath search order.  If you put the
SDK java source and the clojure test/explore code in the same tree,
the clojure implementation of org.dita.dost.util.Configuration and the
java implementation of same get munged up, if you catch my drift.  I'm
still figuring out how to make something like that work in leiningen
without creating two projects (an "sdk" project with just the DITA-OT
source, reorganized, and a "test" project that uses it and implements
overrides as needed).  If that makes sense.

Unfortunately, I'm not yet sure how to expose public final static
stuff in gen-class.


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

