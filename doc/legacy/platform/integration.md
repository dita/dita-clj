# Integration logic

org.dita.dost.platform.IntegratorTask holds ref to:

org.dita.dost.platform.Integrator which is "the main class to control
and excute the integration of the toolkit and different plug-ins."

"descFile" = plugin configuration

## Data

org.dita.dost.platform.ExtensionPoint
    final String id;    /** Extension point ID. */
    final String name;    /** Extension point name. */
    final String plugin;    /** Plug-in defining the extension point. */

 org.dita.dost.platform.Features
    private String id;
    private final File location;
    private final File ditaDir;
    private final Map<String, ExtensionPoint> extensionPoints;
    private final Hashtable<String,List<String>> featureTable;
    private final List<PluginRequirement> requireList;
    private final Hashtable<String,String> metaTable;
    private final List<String> templateList;

org.dita.dost.platform.PluginRequirement
    private static final String REQUIREMENT_SEPARATOR = "|";
    private final ArrayList<String> plugins;
    private boolean required;


## org.dita.dost.platform.Integrator

public final class Integrator {

private static final Strings:

CONF_PLUGIN_IGNORES = "plugin.ignores";
CONF_PLUGIN_DIRS = "plugindirs";
FEAT_TOPIC_EXTENSIONS = "dita.topic.extensions";
FEAT_MAP_EXTENSIONS = "dita.map.extensions";
FEAT_IMAGE_EXTENSIONS = "dita.image.extensions";
FEAT_HTML_EXTENSIONS = "dita.html.extensions";
FEAT_RESOURCE_EXTENSIONS = "dita.resource.extensions";
FEAT_PRINT_TRANSTYPES = "dita.transtype.print";
FEAT_VALUE_SEPARATOR = ",";
PARAM_VALUE_SEPARATOR = ";";
PARAM_NAME_SEPARATOR = "=";

    /** Plugin table which contains detected plugins. */
    private final Map<String, Features> pluginTable;

    private File ditaDir; // DITA_HOME??

    /** Plugin configuration file. */
    private final Set<File> descSet;  // NB:  "desc" for "plugin"?

    private final Set<String> loadedPlugin;
    private final Hashtable<String, List<String>> featureTable;

    private final Set<String> extensionPoints;

METHODS:

    /*** Default Constructor */
    public Integrator()

    /*** Set the ditaDir */
    public void setDitaDir(final File ditadir)

    /*** Set the properties file */
    public void setProperties(final File propertiesfile)

    /*** Setter for strict/lax mode */
    public void setStrict(final boolean strict)

    /*** Set logger */
    public void setLogger(final DITAOTLogger logger)

    /*** Get all and combine extension values
     * @return combined extension value, {@code null} if no value available */
    static String getValue(
	    final Map<String, Features> featureTable,
		final String extension)

    /*** Execute point of Integrator. */
    public void execute()

    /*** Generate and process plugin files. */
    private void integrate()

    /*** Read plug-in feature:: featureName -> combined list of values */
    private String readExtensions(final String featureName)

   /*** Load the plug-in, aggregate by feature, fill into feature table */
    private boolean loadPlugin(final String plugin)

    /*** Check whether the plugin can be loaded */
    private boolean checkPlugin(final String currentPlugin)

    /*** Parse plugin configuration files */
    private void parsePlugin()

    /*** Parse plugin configuration file */
    private void parseDesc(final File descFile)

    /*** Validate plug-in configuration.
     * Follow OSGi symbolic name syntax rules:
     * <pre>
     * digit         ::= [0..9]
     * alpha         ::= [a..zA..Z]
     * alphanum      ::= alpha | digit
     * token         ::= ( alphanum | '_' | '-' )+
     * symbolic-name ::= token('.'token)*
     * </pre>
     * Follow OSGi bundle version syntax rules:
     * <pre>
     * version   ::= major( '.' minor ( '.' micro ( '.' qualifier )? )? )?
     * major     ::= number
     * minor     ::=number
     * micro     ::=number
     * qualifier ::= ( alphanum | '_' | '-' )+
     * </pre>
     * @param f Features to validate
     */
    private void validatePlugin(final Features f)

    /*** Set default values */
    private void setDefaultValues(final Features f)

    /*** Command line interface for testing */
    public static void main(final String[] args)
