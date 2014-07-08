#  GenMapAndTopicListModule

public final class GenMapAndTopicListModule extends AbstractPipelineModuleImpl

Related:

    org.dita.dost.reader.GenListModuleReader extends AbstractXMLFilter

        public abstract class org.dita.dost.writer.AbstractXMLFilter
		    extends org.xml.sax.helpers.XMLFilterImpl
			implements org.dita.dost.writer.AbstractWriter

	org.dita.dost.reader.GenListModuleReader$Reference
        public final String filename;
        public final String format;

 NB: GenListModuleReader obj is called "listFilter" !?


GenMapAndTopicListModule Local vars:  lots of file lists:

    private final Set<File> ditaSet;       /** all dita files */
    private final Set<File> fullTopicSet;  /** all topic files */
    private final Set<File> fullMapSet;    /** all map files */
    private final Set<File> hrefTopicSet;  /** topic files containing href */
    private final Set<File> hrefWithIDSet; /** href topic files with anchor ID */
    private final Set<File> chunkTopicSet; /** chunk topic with anchor ID */
    private final Set<File> hrefMapSet;    /** map files containing href */
    private final Set<File> conrefSet;     /** dita files containing conref */
    private final Set<File> coderefSet;    /** topic files containing coderef */
    private final Set<File> imageSet;      /** all images */
    private final Set<File> flagImageSet;  /** all images used for flagging */
    private final Set<File> htmlSet;       /** all html files */
    private final Set<File> hrefTargetSet; /** all the href targets */
    private Set<File> conrefTargetSet;     /** all the conref targets */
    private Set<File> copytoSourceSet;     /** all the copy-to sources */
    private final Set<File> nonConrefCopytoTargetSet; /** all non-conref targets */
    private final Set<File> ignoredCopytoSourceSet;/** sources of copy-to ignored */
    private final Set<File> subsidiarySet;    /** subsidiary files */
    private final Set<File> relFlagImagesSet; /** relative flag image files */
    private Map<File, File> copytoMap;    /** all copy-to (target,source) */
    private final List<File> waitList; /** waiting for parsing (relative paths) */
    private final List<File> doneList;    /** parsed files */
    private final Set<File> outDitaFilesSet;  /** outer dita files */
    private final Set<File> conrefpushSet;    /** sources of conacion */
    private final Set<File> keyrefSet;        /** files containing keyref */
    private final Set<File> resourceOnlySet;  /** "@processing-role=resource-only" */
    private final Map<String, KeyDef> keysDefMap;    /** Map of all key definitions */

    private File baseInputDir;    /** Absolute basedir for processing */
    private File ditaDir;         /** Absolute ditadir for processing */
    private File inputFile;       /** Input file name. */
    private File ditavalFile;     /** Absolute path for filter file. */

    private GenListModuleReader listFilter;
    private KeydefFilter keydefFilter;
    private ExportAnchorsFilter exportAnchorsFilter;

FILTER STUFF:

filters indirectly extend sax's XMLFilterImpl and implment a write interface

    org.dita.dost.writer.AbstractXMLFilter
	    extends org.xml.sax.helpers.XMLFilterImpl
		implements  org.dita.dost.writer.AbstractWriter

NB: so-called AbstractWriter implements write() by calling XMLUtils.transform()

    org.dita.dost.writer.ProfilingFilter extends AbstractXMLFilter
	org.dita.dost.writer.ExportAnchorsFilter extends AbstractXMLFilter (Eclipse-specific)
	org.dita.dost.reader.KeydefFilter extends AbstractXMLFilter
	org.dita.dost.reader.GenListModuleReader extends AbstractXMLFilter (named "listFilter")



METHODS:

    public AbstractPipelineOutput execute(final AbstractPipelineInput input)

    private void initFilters()

    /*** Init xml reader used for pipeline parsing.
     * @param ditaDir absolute path to DITA-OT directory
     * @param rootFile absolute path to input file */
    private void initXMLReader(final File ditaDir, final boolean validate, final File rootFile)

    private void parseInputParameters(final AbstractPipelineInput input)

    private void processWaitList()

    /*** Get pipe line filters
     * fileToParse absolute path to current file being processed
     * file relative path to current file being processed, relative to start document parent directory */
    private List<XMLFilter> getProcessingPipe(final File fileToParse, final File file)

    /*** Read a file and process it for list information. */
    private void processFile(final File currentFile)

    private void processParseResult(final File currentFile)

    private void categorizeCurrentFile(final File currentFile)
	    => adds file to one of the file sets

    /*** Categorize file. Only adds file to waitlist, htmlSet, or imageSet
     * If {@code file} parameter contains a pipe character, the pipe character is followed by the format of the file. */
    private void categorizeResultFile(final Reference file)

    /*** Update uplevels if needed. If the parameter contains a {@link org.dita.dost.util.Constants#STICK STICK}, it and anything following it is removed.     */
    private void updateUplevels(final File file) {

    /*** Add file to wait list if it has not been parsed. */
    private void addToWaitList(final File file)

    /*** Update base directory based on uplevels. */
    private void updateBaseDirectory()

    /*** Get up-levels relative path.
     * @return path to up-level, e.g. {@code ../../} */
    private String getUpdateLevels()

    /*** Escape regular expression special characters.
     * @return input with regular expression special characters escaped */
    private String escapeRegExp(final String value)

    /*** Parse filter file: return configured filter utility */
    private FilterUtils parseFilterFile()
	    sets default filterMap; parses ditaval file if given

    private void refactoringResult() {
        resourceOnlySet.addAll(
		    listFilter.getResourceOnlySet()); // listFilter = GenListModuleReader
        handleConref(); handleCopyto(); }

    private void handleCopyto()

    /*** Handle topic which are only conref sources from normal processing. */
    private void handleConref()

    /*** Write result files. */
    private void outputResult()
	    works with "job"
	    for-loops over each file set/list updating job
		then calls job.write()
		contains code specific to eclipse help

    /*** Write map of sets to a file.
     * <p>The serialization format is XML properties format where values are comma
     * separated lists.</p> */
    private void writeMapToXML(final Map<File, Set<File>> m, final String filename)

    /*** Add file prefix. For absolute paths the prefix is not added. */
    private Set<File> addFilePrefix(final Set<File> set)

    /*** Add file prefix. For absolute paths the prefix is not added. */
    private Map<File, File> addFilePrefix(final Map<File, File> set)

    /*** Add key definition to job configuration
     * @param prop job configuration */
    private void addKeyDefSetToProperties(final Job prop, final Map<String, KeyDef> keydefs)

    /*** add FlagImagesSet to Properties, which needn't to change the dir level,
     * just ouput to the ouput dir.
     * @param prop job configuration
     * @param set relative flag image files */
    private void addFlagImagesSetToProperties(final Job prop, final Set<File> set)


****************

The heavy lifting goes on in processFile, which processes files on the wait list

the wait list contains "dita" files (ext .dita or .ditamap)

This checks that the file exists and is a "valid target" - meaning,
has one of the "supported extentions" defined in FileUtils.java, from
Configuration, as the merge of (set in plugin.properties):

    supportedTopicExtensions  ;; .xml, .dita
    supportedMapExtensions    ;; .ditamap
	supportedImageExtensions ;; .bmp, .svg, .png, .tif, .jpg, .gif, .jpeg, .tiff, .eps
	supportedHTMLExtensions  ;; .htm, .html
	supportedResourceExtensions ;; .pdf, .swf

Then it gets an XMLReader (called xmlSource), and for each filter in ????

The GenListModuleReader does the XML parsing, acting as client to the
sax processor.  For some reason, this is called

    private GenListModuleReader listFilter;

Maybe that's because we have

    public final class GenListModuleReader extends AbstractXMLFilter

So a reader, which is actually a parser, extends a filter.  Go figure.

Key func: getProcessingPipe

Then it does

            xmlSource.setContentHandler(nullHandler);
            xmlSource.parse(fileToParse.toURI().toString());
            // don't put it into dita.list if it is invalid
            if (listFilter.isValidInput()) {
                processParseResult(file);
                categorizeCurrentFile(file);
            } else if (!file.equals(inputFile)) {
                logger.warn(MessageUtils.getInstance().getMessage("DOTJ021W", params).toString());
            }

If no exception, then:

        doneList.add(file);
        listFilter.reset();
        keydefFilter.reset();

