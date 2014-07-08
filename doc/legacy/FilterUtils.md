This FilterUtils and DITAVAL stuff could all be replaced with simple clojure maps

DITAVAL examples (DITA 1.2 p. 581):

<val>
   <style-conflict backcolor="red"/>
   <prop action="include" att="audience" val="everybody"/>
   <prop action="flag" att="product" val="YourProd" backcolor="purple"/>
   <prop action="flag" att="product" backcolor="blue"
         color="yellow" style="underline" val="MyProd">
      <startflag imageref="startflag.jpg">
        <alt-text>This is the start of my product info</alt-text>
      </startflag>
      <endflag imageref="endflag.jpg">
        <alt-text>This is the end of my product info</alt-text>
      </endflag>
	  </prop>
   <revprop action="flag" val="1.2"/>
</val>

val>
   <prop action="exclude"/>
   <prop action="include" att="audience" val="everybody"/>
   <prop action="include" att="audience" val="novice"/>
   <prop action="include" att="product" val="productA"/>
   <prop action="include" att="product" val="productB"/>
</val>


/*** Utility class used for flagging and filtering. */
public final class FilterUtils

NB: not really a utility class.  just implements filter/flagging logic

embed:

    /*** Filter key object. */    public static class FilterKey
	locals: attribute,  value :: Strings

locals:

// actions:  (DITA 1.2 p. 583)
    public enum Action { INCLUDE, EXCLUDE, PASSTHROUGH, FLAG }

// "conditional processing attributes":
    private static final String[] PROFILE_ATTRIBUTES =
        AUDIENCE,PLATFORM,PRODUCT,OTHERPROPS,PROPS
		PRINT  // specialized?

    public static final FilterKey DEFAULT = new FilterKey(DEFAULT_ACTION, null);

    /** Immutable default filter map. */
    private final Map<FilterKey, Action> defaultFilterMap;
    private Map<FilterKey, Action> filterMap = null;

    private final Set<FilterKey> notMappingRules = new HashSet<FilterKey>();

NOTE that key to the filtermaps is pair (attrname, attrval), and val
is "action", one of INCLUDE, EXCLUDE, PASSTHROUGH, FLAG (corresponding
to the DITA xml filtering options)



