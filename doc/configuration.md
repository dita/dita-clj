# DITA-OT Configuration

`org/dita/dost/util/Configuration.java` maintains the following config data:

* configuration :: public final static Map<String,String>.  Data from
  * GEN_CONF_PROPERTIES file = `lib/plugin.properties` (see below)
  * CONF_PROPERTIES file = `lib/configuration.properties` (see below)
* processingMode :: public enum Mode {STRICT, SKIP, LAX}
  * Contents: set by "processing-mode" config property
* printTranstype :: public static final List<String>
  * Contents: items in CONF_PRINT_TRANSTYPES config property (see below)
* pluginResourceDirs :: public static final Map<String, File>
  * Contents: config properties starting with "plugin." and ending in ".dir"

## "configuration" Map

### plugin.properties

Configuration.java reads GEN_CONF_PROPERTIES file = `lib/plugin.properties`, which looks like:

	#DITA-OT runtime configuration, do not edit manually
	#Thu Sep 05 19:39:48 CDT 2013
	supported_topic_extensions=.xml;.dita
	supported_resource_extensions=.pdf;.swf
	print_transtypes=pdf2;pdf;legacypdf;odt;lein-pdf
	supported_image_extensions=.bmp;.svg;.png;.tif;.jpg;.gif;.jpeg;.tiff;.eps
	supported_map_extensions=.ditamap
	supported_html_extensions=.htm;.html

Configuration.java then loads props into one of the static collection objects it maintains

### configuration.properties

Reads CONF_PROPERTIES file = `lib/configuration.properties`, which looks like

	#DITA-OT configuration properties
	otversion = 1.8
	default.language = en
	generate-debug-attributes = true
	processing-mode = lax

	# Integration
	plugindirs = plugins;demo
	plugin.ignores =

	# PDF2 defaults
	org.dita.pdf2.index.frame-markup = false
	org.dita.pdf2.i18n.enabled = true
	org.dita.pdf2.use-out-temp = false

### Client Access

org.dita.dost.util.Configuration is all static; no objects of this
class can be created.  Class methods load of the config props and
that's it.

Clients use:

* Configuration.configuration.get(key) // configuration is key-val Map
* Configuration.printTranstype.contains(key)  // printTranstype is a string List
* Configuration.pluginResourceDirs.get(key)  // pluginResourceDirs is key-val Map

E.g.

    Constants.java :  public static final String CONF_PRINT_TRANSTYPES = "print_transtypes";
    final String printTranstypes = Configuration.configuration.get(CONF_PRINT_TRANSTYPES);


# DITA-CLJ

We would prefer to get rid of this stuff and configure extensions at
runtime via a project map or config files.  After all, if I want PDF
output, then the fact that various other output types are available is
totally irrelevant.  The process need only confirm that the transform
I want is available; taking a census of all possible available
transforms is wasteful overhead.

We can hack this in Clojure using java interop, e.g. gen-class, proxy,
deftype, or the like.  We support the public interface of
org.dita.dost.util.Configuration but instead of reading
`plugin.properties` and `configuration.properties` files, we provide
the setter methods that are missing in Configuration.java.  Then our
job control logic can read props from e.g. the project map, initialize
our config object, and pass it to the dita kernel.

 See `config.clj`.

