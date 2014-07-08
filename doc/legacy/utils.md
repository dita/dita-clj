
CatalogUtils.java
CheckLang.java
Configuration.java
Constants.java
ConvertLang.java
DITAOTCollator.java
DITAOTCopy.java
DelayConrefUtils.java
DitaClass.java
FilePathToURI.java
FileUtils.java
FilterUtils.java
ImgUtils.java
IsAbsolute.java
Job.java
KeyDef.java
LangParser.java
LogUtils.java
MergeUtils.java
StringUtils.java
StyleUtils.java
TopicIdParser.java
URLUtils.java
UriBasenameTask.java
UriDirnameTask.java
XMLGrammarPoolImplUtils.java
XMLSerializer.java
XMLUtils.java

****************

CatalogUtils.java: specialized to org.apache..xml.resolver

Local vars:
    /**apache catalogResolver.*/
    private static CatalogResolver catalogResolver = null;
    /** Absolute directory to find catalog-dita.xml.*/
    private static File ditaDir;

2 Funcs:

setDitaDir
getCatalogResolver

    /*** Set directory to find catalog-dita.xml. */
    public static synchronized void setDitaDir(final File ditaDir)

    /*** Get CatalogResolver. */
    public static synchronized CatalogResolver getCatalogResolver() {

****************

FileUtils.java:  static file utils

    private static final DITAOTJavaLogger logger = new DITAOTJavaLogger();

**** Set of private final static vars, all of type List<String>.
Code reads props from the Configuration object and sets the vars:

    /* File extensions contain a leading dot. */
    supportedTopicExtensions
    supportedMapExtensions
	supportedImageExtensions
	supportedHTMLExtensions
	supportedResourceExtensions
	supportedExtensions

**** Set of file type predicates; return true if file is of indicated
     type, going by file extension.  All have signature String ->
     public static boolean, e.g.

        public static boolean isHTMLFile(final String lcasefn)

isHTMLFile
isHHPFile
isHHCFile
isHHKFile
isResourceFile
isSupportedImageFile
isValidTarget

Deprecated:
isDITAFile
isDITATopicFile
isDITAMapFile

**** Path resolution ops:


    /*** Resolves a path reference against a base path. */
    public static File getRelativePath(final File basePath, final File refPath)

    /*** Resolves a path reference against a base path. */
    public static String getRelativePath(final String basePath, final String refPath, final String sep)

    /*** Get relative path to base path.
     * <p>For {@code foo/bar/baz.txt} return {@code ../../}</p> */
    private static String getRelativePathForPath(final String relativePath, final String sep)

    /*** Get relative path to base path.
     * <p>For {@code foo/bar/baz.txt} return {@code ../../}</p> */
    public static File getRelativePath(final File relativePath)

    /*** Get relative path to base path.
     * <p>For {@code foo/bar/baz.txt} return {@code ../../}</p> */
    private static String getRelativePathForPath(final String relativePath, final String sep)



Deprecated:

    /*** Resolves a path reference against a base path. */
    public static String getRelativeUnixPath(final File basePath, final String refPath) {

    /*** Resolves a path reference against a base path. */
    public static String getRelativeUnixPath(final String basePath, final String refPath) {

    /*** Get relative path to base path.
     * <p>For {@code foo/bar/baz.txt} return {@code ../../}</p> */
    public static String getRelativeUnixPath(final String relativePath)

    /*** Normalize topic path base on current directory and href value, by
     * replacing "\\" and "\" with {@link File#separator}, and removing ".", ".."
     * from the file path, with no change to substring behind "#". */
    public static String resolveTopic(final File rootPath, final String relativePath)

    /*** Normalize topic path base on current directory and href value, by
     * replacing "\\" and "\" with {@link File#separator}, and removing ".", ".."
     * from the file path, with no change to substring behind "#". */
    public static String resolveTopic(final String rootPath, final String relativePath)
    public static URI resolve(final File rootPath, final URI relativePath)

    /*** Resolve file path against a base directory path. Normalize
    * the input file path, by replacing all the '\\', '/' with
      File.seperator, and removing '..' from the directory.
     * <p>Note: the substring behind "#" will be removed.</p> */
    public static File resolve(final String basedir, final String filepath)

    public static File resolve(final File basedir, final String filepath)

    public static File resolve(final File basedir, final File filepath)

****  Normalize paths:

    /*** Remove redundant names ".." and "." from the given path. */
    public static File normalize(final File path)

    /*** Remove redundant names ".." and "." from the given path and
    * replace directory separators. */
    public static String normalize(final String path, final String separator) {

Deprecated:

    /*** Remove redundant names ".." and "." from the given path. */
    public static File normalize(final String path)

**** Conversion funcs (Win <-> Unix)

Deprecated:

    /*** Translate path separators from Windows to Unix. */
    public static String separatorsToUnix(final String path)

**** Misc:

    /*** Return if the path is absolute. */
    public static boolean isAbsolutePath (final String path)

    /*** Copy file from src to target, overwrite if needed. */
    public static void copyFile(final File src, final File target)

    /*** Copy input stream to output stream */
    public static void copy(final InputStream fis, final OutputStream fos) throws IOException

    /*** Replace the file extension. */
    public static String replaceExtension(final String attValue, final String extName)

    /*** Get file extension */
    public static String getExtension(final String file)

    /*** Check whether a file exists on the local file system. */
    public static boolean fileExists (String filename)

    /*** Get filename w/o path or hash fragment from a path. */
    public static String getName(final String aURLString)


    /*** Test if current platform is Windows */
    public static boolean isWindows()

    /*** Get base path from a path. */
    public static String getFullPathNoEndSeparator(final String aURLString)

    /*** Return true if 'child' is in 'directory'. */
    public static boolean directoryContains(final File directory, final File child)

    /*** Move file. */
    public static void moveFile(File srcFile, File destFile) throws IOException

Deprecated:

    /*** Strip fragment part from path. */
    public static String stripFragment(final String path)

    /*** Set fragment */
    public static String setFragment(final String path, final String fragment)

    /*** Get fragment part from path. */
    public static String getFragment(final String path)

    /*** Retrieve the topic ID from the path */
    public static String getTopicID(final String relativePath)

    /*** Retrieve the element ID from the path */
    public static String getElementID(final String relativePath)

    /*** Set the element ID from the path */
    public static String setElementID(final String relativePath, final String id)

    /*** Get fragment part from path or return default fragment. */
    public static String getFragment(final String path, final String defaultValue)

















****************

StringUtils.java: 1 enum, 10 funcs

join  (2 sigs)
escapeXML  (2 sigs)
replaceAll
getExtProps
restoreSet
isEmptyString
setOrAppend
getXMLReader
getLocale
normalizeAndCollapseWhitespace


    /* Assemble all elements in collection to a string.*/
    public static String join(final Collection coll, final String delim)

    /*** Assemble all elements in map to a string.*/
    public static String join(final Map value, final String delim)

    /*** Escape XML characters.*/
    public static String escapeXML(final String s)

    /*** Escape XML characters.*/
    public static String escapeXML(final char[] chars, final int offset, final int length)

    /*** Replace each substring of this string that matches the given string
     * with the given replacement. Differ from the JDK String.replaceAll function,
     * this method does not support regular expression based replacement on purpose.*/
    public static String replaceAll(
	    final String input,
		final String pattern,
		final String replacement)

    /*** Parse {@code props} attribute specializations
     * @param domains input domain
     * @return list of {@code props} attribute specializations*/
    public static String[][] getExtProps(final String domains)

    /*** Break down a string separated by <code>delim</code> into a string set.*/
    public static Set<String> restoreSet(final String s, final String delim)

    /*** Return true if the string is null or "".*/
    public static boolean isEmptyString(final String s){

    /*** If target is null, return the value; else append value to target.
     * If withSpace is true, insert a blank between them.*/
    public static String setOrAppend(
	    final String target, // target to be appended
		final String value,  // value to append
		final boolean withSpace) //  whether insert a blank

    /*** return XML parser instance.  Preferred XML readers are in order:
     *   org.dita.dost.util.Constants#SAX_DRIVER_DEFAULT_CLASS Xerces
     *   org.dita.dost.util.Constants#SAX_DRIVER_SUN_HACK_CLASS Sun's Xerces
     *   org.dita.dost.util.Constants#SAX_DRIVER_CRIMSON_CLASS Crimson */
    public static XMLReader getXMLReader() throws SAXException

    /*** Return a Java Locale object. */
    public static Locale getLocale(final String anEncoding){

    /** Whitespace normalization state. */
    private enum WhiteSpaceState { WORD, SPACE }

    /*** Normalize and collapse whitespaces from string buffer. */
    public static void normalizeAndCollapseWhitespace(final StringBuilder strBuffer)

