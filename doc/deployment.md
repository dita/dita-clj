# Deployment

### Upgrading

There are several basic components of the toolkit:

    * The Java jar files
	* The DITA schema files (DTD and XSD)
	* The DITA-OT "kernel" XSL files
	* Specializations
	  * Config files
	  * XSL files

We can expect all of these to change independently, at different
rates.  So ideally we want a convenient means to update only the DITA
schema files, or the DITA-OT kernel XSL files, etc.  This will happen
when fixes are published in between releases of the toolkit per se.

Another requirement is that upgrades should not mess up local
customizations.

### File System

We want system, local, and user-level configuration.  E.g.

* `~/.dita-clj.d/xml/org.dita/ot/xsl`  -- overrides
* `/usr/local/etc/xml/org.dita/ot/xsl`  -- overrides
* `/etc/xml/org.dita/ot/xsl`

### DITA Schema files

The DITA schema is implementation-independent, so the XSD and DTD
files should go in `/usr/local/etc/xml`, e.g.

    /usr/local/etc/xml/org.oasis-open.dita.v1_2/schema

Always put the version number in the path so multiple versions can be
installed.  This will allow the user to upgrade the schema files
without upgrading the toolkit.

### XSL Stylesheets

The stylesheets are specific to the implementation, so should be
stored there, e.g.

    $DITA-CLJ_HOME/etc/xml/org.dita/ot/xsl

**Versioning**

One problem with this strategy is that it complicates updates.  We can
expect that the stylesheets will change more frequently than the
executables.  So we would like to be able to update the XSL files
independently, e.g. to fix a bug.  We can just have the user copy some
files into the installation tree, but that doesn't seem right.  What
about a system-level installation where different users may require
different versions of various components?

* We want the option to install e.g. a bugfix version of the XSL files
  at user level, e.g. in `~/.dita-clj.d/xml/...`.

This is going to require override logic, so we can have a stack of
components and the top one gets used.  A potential problem here: we
need to use *only* what's on top.  If something on top of the stack
refers to `foo.xsl`, and that file is missing, we should _**not**_
search further in the stack; otherwise we end up mixing versions.

