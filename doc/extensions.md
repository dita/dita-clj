# Extensions

**CAVEAT**  Nothing implemented yet.  These are just design notes.

DITA-OT uses static "extension points".  This means plugins modify the
toolkit.

dita-clj pursues a different strategy, using "hooks" to support
runtime-only extensions.  Each project can hang bits of code on the
hooks, which effectively extend the functionality of the toolkit at
runtime.

Under this strategy the toolkit itself is never modified.  A dita-clj
plugin is code, resources, etc. invoked via a hook at runtime.
Plugins are not "installed" in the toolkit, although they may be
lodged in a well-known location so that they become available to
projects.

Search path for plugins: ideally, follow the standard pattern: put
system level stuff somewhere like `/etc`, local stuff somewher in
`/usr/local`, and user stuff somewhere in `~/`, like `~/.dita`.
(Presumably there is some kind of analogous best-practice for
Windows.)

Using plugins: the hook mechanism will be enabled by leiningen
keywords in the project.clj file for each project.  See the leiningen
template dita-template.
