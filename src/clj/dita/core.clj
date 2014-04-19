(ns dita.core
  (:require [clojure.java.io :as jio])
  (:import (org.dita.dost.invoker ExtensibleAntInvoker
                                  ExtensibleAntInvoker$Module
                                  ExtensibleAntInvoker$Param)
           (org.dita.dost.pipeline AbstractPipelineInput
                                   AbstractPipelineOutput
                                   PipelineHashIO
                                   PipelineFacade)
           (org.dita.dost.platform InsertDependsAction)
           (org.dita.dost.module GenMapAndTopicListModule)
;; import org.dita.dost.pipeline.AbstractPipelineOutput;
;; import org.dita.dost.reader.DitaValReader;
;; import org.dita.dost.reader.GenListModuleReader;
;; import org.dita.dost.reader.GenListModuleReader.Reference;
;; import org.dita.dost.reader.GrammarPoolManager;
;; import org.dita.dost.reader.KeydefFilter;
;; import org.dita.dost.util.CatalogUtils;
;; import org.dita.dost.util.DelayConrefUtils;
;; import org.dita.dost.util.FileUtils;
;; import org.dita.dost.util.FilterUtils;
;; import org.dita.dost.util.Job;
;; import org.dita.dost.util.KeyDef;
;; import org.dita.dost.util.StringUtils;
;; import org.dita.dost.util.Job.FileInfo;
;; import org.dita.dost.writer.ExportAnchorsFilter;
;; import org.dita.dost.writer.ExportAnchorsFilter.ExportAnchor;
;; import org.dita.dost.writer.ProfilingFilter;
           ))

  ;; (import '(org.dita.dost.invoker.ExtensibleAntInvoker)
  ;;         '(org.dita.dost.platform InsertDependsAction)))


(def ant-parms
  {
   ;; ant invoker(foo)
   ;; :ANT_INVOKER_PARAM_TEMPDIR "tempDir"
   :MSG		"message"
   :TEMPDIR	"tempDir"
   :BASEDIR	"basedir"
   :INPUTMAP	"inputmap"
   :DITAVAL	"ditaval"
   :MAPLINKS	"maplinks"
   :X_TARGETEXT	"targetext";
   :X_INDEXTYPE "indextype";
   :X_INDEXCLASS "indexclass";
   :X_ENCODING	"encoding";
   :X_OUTPUT	"output";
   :X_INPUT	"input";
   :X_DITADIR	"ditadir";
   :X_INPUTDIR	"inputdir";
   :X_STYLE	"style";
   :X_TRANSTYPE "transtype";
   :X_OUTTERCONTROL "outercontrol";
   :X_GENERATECOPYOUTTER "generatecopyouter";
   :X_ONLYTOPICINMAP "onlytopicinmap";
   :X_VALIDATE	"validate";
   :X_OUTPUTDIR "outputdir";
   :X_GRAMCACHE "gramcache";
   :X_SETSYSTEMID "setsystemid";
   }
  )

(defn get-modules

;; for (final Module m: modules) {
;;     final PipelineHashIO pipelineInput = new PipelineHashIO();
;;     for (final Map.Entry<String, String> e: attrs.entrySet()) {
;;         pipelineInput.setAttribute(e.getKey(), e.getValue());
;;     }
;;     if (m instanceof Xslt) {
;;        /* process xslt task */
;;     } else {
;;         for (final Param p : m.params) {
;;             if (!p.isValid()) {
;;                 throw new BuildException("Incomplete parameter");
;;             }
;;             final String ifProperty = p.getIf();
;;             final String unlessProperty = p.getUnless();
;;             if ((ifProperty == null || getProject().getProperties().containsKey(ifProperty))
;;                 && (unlessProperty == null || !getProject().getProperties().containsKey(unlessProperty))) {
;;                 pipelineInput.setAttribute(p.getName(), p.getValue());
;;             }
;;         }
;;         start = System.currentTimeMillis();
;;         pipeline.execute(m.getImplementation(), pipelineInput);
;;         end = System.currentTimeMillis();
;;     }
;; )

;; interface:  org.dita.dost.pipeline.AbstractPipelineInput
    ;; public void setAttribute(String name, String value);
    ;; public String getAttribute(String name);

;; public final class PipelineHashIO implements AbstractPipelineInput, AbstractPipelineOutput
;;     ;; public void setAttribute(String name, String value);
;;     ;; public String getAttribute(String name);

(defn get-in-pipe
  []
  ;; in org/dita/dost/invoker/ExtensibleAntInvoker.java:
  ;; private final Map<String, String> attrs = new HashMap<String, String>();
  ;; attrs entries are only set explicitly in ExtensibleAntInvoker
  ;; so I don't see any real purpose to it
  ;;     attrs.put("message", m);
  ;;     attrs.put(ANT_INVOKER_PARAM_INPUTMAP, inputmap);
  ;;     attrs.put(ANT_INVOKER_PARAM_TEMPDIR, tempdir.getAbsolutePath());
  ;;     if (attrs.get(ANT_INVOKER_PARAM_BASEDIR) == null) {
  ;;         attrs.put(ANT_INVOKER_PARAM_BASEDIR, getProject().getBaseDir().getAbsolutePath());
  ;;     }

  ;; we don't do ant "if" and "unless" props (we have real logic!)
  ;;     if ((ifProperty == null || getProject().getProperties().containsKey(ifProperty))
  ;;             && (unlessProperty == null || !getProject().getProperties().containsKey(unlessProperty))) {
  ;;         attrs.put(p.getName(), p.getValue());
  ;;     }
  ;;     final PipelineHashIO pipelineInput = new PipelineHashIO();
  (let [in-pipe (PipelineHashIO.)
        attrs {(:MSG ant-parms) "hi dita"
               (:INPUTMAP ant-parms) "test.ditamap"
               (:TEMPDIR ant-parms) "/tmp"
               (:BASEDIR ant-parms) (jio/file "./") ; base directory of the project as a file object.
               ;; TODO: use leiningen to get proj dir
               }]
    ;; dita-ot installs "task pipeline" parms in the input pipeline
    ;; ExtensibleAntInvoker:
    ;; for (final Map.Entry<String, String> e: attrs.entrySet()) {
    ;;     pipelineInput.setAttribute(e.getKey(), e.getValue());
    ;; }
    (doseq [[key val] attrs]
      (.setAttribute in-pipe (str key) (str val)))
    in-pipe))

(def dita-logger
  ;; we use plain ol' clojure logging.
  ;; dita-ot, in ExtensibleAntInvoker:
  ;; final DITAOTAntLogger logger = new DITAOTAntLogger(getProject());
  ;; logger.setTask(this);
  ;; pipeline.setLogger(logger);
  ;; pipeline.setJob(getJob(tempDir, getProject()));
)


(defn dita-ant
  [ditamap ;; filename
   & args]
  ;; <taskdef name="pipeline"
  ;;   classname="org.dita.dost.invoker.ExtensibleAntInvoker">
  ;;   <classpath refid="dost.jar.path" />
  ;; </taskdef>
  (let [
        p (let [p (ExtensibleAntInvoker$Param.)]
            (.setName p "inputdir")
            (.setLocation p (jio/file "./")) ;;"${args.input.dir}"
            (.setIf p "args.input.dir")
            p)
        tf (jio/file "tmp")
        glm (GenMapAndTopicListModule.)
        mod (ExtensibleAntInvoker$Module.)
        eai (ExtensibleAntInvoker.)]
    (do
      (.setMessage eai "Generate List")
      (.setInputmap eai ditamap)
      (.setTempdir eai tf)
      (.setClass mod (type glm))
      (.addConfiguredParam mod p)
      (.addConfiguredModule eai mod)
      ;;                       ;; (setClass "org.dita.dost.module.GenMapAndTopicListModule")
      ;;                       (m addConfiguredParam p))))))
      )
    mod))
      ;; (.. Param
                               ;;     ))
  ;;     <param name="ditadir" location="${dita.dir}"/>
  ;;     <param name="ditaval" location="${dita.input.valfile}" if="dita.input.valfile"/>
  ;;     <param name="validate" value="${validate}"/>
  ;;     <param name="generatecopyouter" value="${generate.copy.outer}"/>
  ;;     <param name="outercontrol" value="${outer.control}"/>
  ;;     <param name="onlytopicinmap" value="${onlytopic.in.map}"/>
  ;;     <param name="outputdir" location="${output.dir}"/>
  ;;     <param name="transtype" value="${transtype}"/>
  ;;     <param name="gramcache" value="${args.grammar.cache}"/>
  ;;     <param name="setsystemid" value="${args.xml.systemid.set}"/>

(def build-init-tasks
  {:pipeline "org.dita.dost.invoker.ExtensibleAntInvoker"
   :config-logger "org.dita.dost.log.LogConfigTask"
   :dita-ot-echo "org.dita.dost.log.DITAOTEchoTask"
   :dita-ot-fail "org.dita.dost.log.DITAOTFailTask"
   :dita-ot-copy "org.dita.dost.util.DITAOTCopy"
   :job-property "org.dita.dost.platform.JobPropertyTask"
   ;; xslt tasks
   :job-helper ""
   ;; targets
   :init-logger nil
   :check-arg nil
   :log-arg nil
   })

(defn gen-list
  [& args]
  ;; <pipeline message="Generate list." taskname="gen-list"
  ;;   inputmap="${args.input}"
  ;;   tempdir="${dita.temp.dir}">
  ;;   <module class="org.dita.dost.module.GenMapAndTopicListModule">
  ;;     <param name="inputdir" location="${args.input.dir}" if="args.input.dir"/>
  ;;     <param name="ditadir" location="${dita.dir}"/>
  ;;     <param name="ditaval" location="${dita.input.valfile}" if="dita.input.valfile"/>
  ;;     <param name="validate" value="${validate}"/>
  ;;     <param name="generatecopyouter" value="${generate.copy.outer}"/>
  ;;     <param name="outercontrol" value="${outer.control}"/>
  ;;     <param name="onlytopicinmap" value="${onlytopic.in.map}"/>
  ;;     <param name="outputdir" location="${output.dir}"/>
  ;;     <param name="transtype" value="${transtype}"/>
  ;;     <param name="gramcache" value="${args.grammar.cache}"/>
  ;;     <param name="setsystemid" value="${args.xml.systemid.set}"/>
  ;;   </module>
  ;; </pipeline>
  ;; <!-- generate list files -->
  ;; <job-helper file="outditafiles.list" property="outditafileslist"/>
  ;; etc
  (println "GenMapAndTopicListModule")
  (println (dita-ant "foo.ditamap")))
  ;; (.. GenMapAndTopicListModule (execute)))

(def build-preprocess-tasks
  {
   :clean-temp nil
   ;;{depend.preprocess.pre},
   :preprocess-init nil,
   :gen-list nil,
    ;; dita:depends="{depend.preprocess.gen-list.pre}"
    ;; dita:extension="depends org.dita.dost.platform.InsertDependsAction"

   :debug-filter nil,
   :copy-files nil,
   :keyref nil,
   :conrefpush nil,
   :conref nil,
   :topic-fragment nil,
   :coderef nil,
   :mapref nil,
   :move-meta-entries nil,
   :mappull nil,
   :chunk nil,
   :maplink nil,
   :move-links nil,
   :topicpull nil,
   :flag-module nil,
   :clean-map nil
   ;;{depend.preprocess.post}"
   }
  )

(defn -main
  "I don't do a whole lot."
  [& x]
  (println x "Hello, DITA!")
  (let [p (get-in-pipe)]
    (printf "%s : %s\n" (:MSG ant-parms) (.getAttribute p (:MSG ant-parms)))
    (printf "%s : %s\n" (:INPUTMAP ant-parms) (.getAttribute p (:INPUTMAP ant-parms)))
    (printf "%s : %s\n" (:TEMPDIR ant-parms) (.getAttribute p (:TEMPDIR ant-parms)))
    (printf "%s : %s\n" (:BASEDIR ant-parms) (.getAttribute p (:BASEDIR ant-parms)))
    p))
  ;; (gen-list))
