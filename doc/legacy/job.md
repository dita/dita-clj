# Job

org.dita.dost.util.Job - Specific to dita-ot

Embedded interfaces and classes:

    private final static class JobHandler extends  org.xml.sax.helpers.DefaultHandler

    public static final class Job$FileInfo
	        public locals: uri, file, fileformat, bools for e.g. isChunked

        embedded in FileInfo:
            public static interface Job$FileInfo$Filter
            public static class Job$FileInfo$Builder
		        private locals: same as FileInfo's public locals!

element, attribute, and property names implemented as private static final String locals:

     JOB_FILE = ".job.xml";
the rest are a waste of vars:
    private static final String ELEMENT_JOB = "job";
	private static final String ATTRIBUTE_KEY = "key";
	etc.


/** File name for key definition file */
public static final String KEYDEF_LIST_FILE = "keydef.xml";
/** File name for key definition file */
public static final String SUBJECT_SCHEME_KEYDEF_LIST_FILE = "schemekeydef.xml";
/** File name for temporary input file list file */
public static final String USER_INPUT_FILE_LIST_FILE = "usr.input.file.list";

/** Map of serialization attributes to file info boolean fields. */
private static final Map<String, Field> attrToFieldMap= new HashMap<String, Field>();
    static {
        try {
            attrToFieldMap.put(ATTRIBUTE_CHUNKED, FileInfo.class.getField("isChunked"));
			etc.

