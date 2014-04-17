
Two steps: preprocessing yields an intermediate representation -- call
it dixml (dita intermediate xml).  Postprocessing converts dixml to an
output format.

Instead of "plugins" that alter the toolkit we specify post-processing
tasks at runtime.

Job control is handled by a leiningen plugin, under the control of
project.clj.

Kinds of extension:

* preprocessing - use hooks
* schema extension
* postprocessing - this is not really an extension

Postprocessing tasks (e.g. dixml to html5) will usually be just xslt
files.  But in principle java code is allowable.  The job control
(leiningen task) is responsible for running the postprocessing job
according to config specs encoded in project.clj.  That will usually
mean running an xslt process, but may also involve running java or
clojure code.

In any case, there will be no "integrate" task.
