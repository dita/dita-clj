# Extensions

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
