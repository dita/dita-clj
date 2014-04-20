(ns dita.core
  (:require [clojure.java.io :as jio]
            [org.dita.dost.util.Configuration :as cfg])
  (:import (org.dita.dost.invoker ExtensibleAntInvoker
                                  ExtensibleAntInvoker$Module
                                  ExtensibleAntInvoker$Param)
           (org.dita.dost.log DITAOTAntLogger)
           (org.dita.dost.module GenMapAndTopicListModule)
           (org.dita.dost.pipeline AbstractPipelineInput
                                   AbstractPipelineOutput
                                   PipelineHashIO
                                   PipelineFacade)
           ;; (org.dita.dost.platform InsertDependsAction)
           ;; (org.dita.dost.reader DitaValReader
           ;;                       GenListModuleReader
           ;;                       GenListModuleReader$Reference
           ;;                       GrammarPoolManager
           ;;                       KeydefFilter)
           (org.dita.dost.util
            ;; CatalogUtils
            ;; DelayConrefUtils
            ;; FileUtils
            ;; FilterUtils
            Job
            ;; KeyDef
            ;; StringUtils
            ;; Job$FileInfo
            )
           ;; (org.dita.dost.writer ExportAnchorsFilter
           ;;                       ExportAnchorsFilter$ExportAnchor
           ;;                       ProfilingFilter)
           (org.apache.tools.ant Project
                                 Task)
           ))

(def dita-parms
  {
   ;; <clj keyword> <key> <default value>
   ;; TODO: add these for MessageUtils.java and CatalogUtils.java
   :xmlcatalog ["xmlcatalog" "catalog-dita.xml"]
   :resourcesdir ["resourcesdir" "resources"]
   :messages	["messages" "messages/messages.xml"]
   :base-dir	["basedir" "/Users/gar/dev/dita-clj"]
   ;; (jio/file "./src/dita/doc")] ; base directory of the project as a file object.
   :input-map	["inputmap" "quickstart.ditamap"]
   :dita-dir	["ditadir" "/Users/gar/dev/dita-clj"] ;; TODO: get $DITAOT_HOME from env?
   ;; <param name="ditadir" location="${dita.dir}"/>
   ;; CATALOG:  dita-ot looks for catalog-dita.xml in DITADIR
   :dita-val	["ditaval" ""] ;; location="${dita.input.valfile}" if="dita.input.valfile"
   :encoding	["encoding" "UTF-8"]
   :generate-copy-outer ["generatecopyouter" "1"] ;;  generate.copy.outer
   ;; <param name="generatecopyouter" value="${generate.copy.outer}"/>
   :cache-grammar ["gramcache" "no"]  ;; yes | no
   ;; <param name="gramcache" value="${args.grammar.cache}"/>
   :index-class ["indexclass" ""]  ;; depends on output format
   :index-type ["indextype" "htmlhelp"] ;; depends on output format
   :input-file	["input" "/Users/gar/dev/dita-clj/src/dita/doc/quickstart.ditamap"] ;; ${dita.temp.dir}${file.separator}${user.input.file}
   ;; args.input  :  master file for your documentation project.
   :input-dir	["inputdir" "/Users/gar/dev/dita-clj/src/dita/doc"] ;; base directory for your documentation project.
   ;; <param name="inputdir" location="${args.input.dir}" if="args.input.dir"/>
   ;; The default value is the parent directory of the file specified by args.input.
   :map-links	["maplinks" ""]
   :msg		["message" "hello DITA"]
   :outer-control ["outercontrol" "warn"] ;; outer.control : fail | warn | quiet
   ;; <param name="outercontrol" value="${outer.control}"/>
   :only-topic-in-map ["onlytopicinmap" "false"] ; bool
   ;; <param name="onlytopicinmap" value="${onlytopic.in.map}"/>
   :output-dir ["outputdir" "/Users/gar/dev/dita-clj/target"] ;  output.dir : default:  DITADIR/out.
   ;; <param name="outputdir" location="${output.dir}"/>
   :output-file	["output" ""] ;;  ${output.dir}/${user.input.file}"
   :print-trans-types ["print_transtypes" "xhtml "]
    ;; public static final String CONF_PRINT_TRANSTYPES = "print_transtypes";
   :set-system-id ["setsystemid" "no"] ;; ??
   ;; <param name="setsystemid" value="${args.xml.systemid.set}"/>
   :stylesheet	["style" ""]; ${dita.plugin.org.dita.pdf2.dir}/xsl/common/topicmerge.xsl"/>
   :target-ext	["targetext" ".html"] ;; depends on output format
   :temp-dir	["tempDir" "/var/tmp/dita"] ;; :ANT_INVOKER_PARAM_TEMPDIR "tempDir"
   :trans-type ["transtype" "xhtml"]
   ;; :DITADIR	["ditadir" "/usr/local/ditaot"] ;; TODO: get $DITAOT_HOME from env?
   ;; <param name="transtype" value="${transtype}"/>
   :validate	["validate" "true"] ; bool
   ;; <param name="validate" value="${validate}"/>
   }
  )

(defonce conf
  (do
    (.put cfg/configuration "foo" "bar")
    (.put cfg/configuration "baz" "buz")
    (.put cfg/configuration
          (first (:print-trans-types dita-parms))
          (second (:print-trans-types dita-parms)))
    ))


(defonce job (Job. (jio/file "/var/tmp/dita"))) ; see ExtensibleAntInvoker.getJob

(defonce ^:dynamic *prj*
  (let [p (Project.)]
    (do
      (.addReference p "job" job)
      p)))

;;(defonce task (.createTask prj "DITA-CLJ"))                      ; see ExtensibleAntInvoker.execute

(defonce ^:dynamic *logger*
  (let [l (DITAOTAntLogger. *prj*)]     ; see ExtensibleAntInvoker.execute
    (do
      ;; (.setTask l task)
      ;; (.setTarget l target)
      l)))

(def ant-parms
        ;; build_demo.xsl:
	;; <!-- defaults for DITA configuration required by callable targets -->
	;; <property name="dita.dir" value="${basedir}"/>
	;; <property name="dita.dtd.dir"
	;; 	value="${dita.dir}${file.separator}dtd"/>
	;; <property name="dita.css.dir"
	;; 	value="${dita.dir}${file.separator}css"/>
	;; <property name="dita.resource.dir"
	;; 	value="${dita.dir}${file.separator}resource"/>
	;; <!-- defaults for DITA output configuration -->
	;; <property name="dita.doc.dir"
	;; 	value="${dita.dir}${file.separator}doc"/>
	;; <property name="dita.doc.langref.dir"
	;; 	value="${dita.doc.dir}${file.separator}langref-dita1.1"/>
	;; <property name="dita.doc.articles.dir"
	;; 	value="${dita.doc.dir}${file.separator}articles"/>

	;; <property name="dita.samples.dir"
	;; 	value="${dita.dir}${file.separator}samples"/>

	;; <property name="dita.output.dir"
	;; 	value="${dita.dir}${file.separator}out"/>

	;; <property name="dita.output.docbook.dir"
	;; 	value="${dita.output.dir}${file.separator}docbook"/>


  )

(defn get-in-parms
  []
  ;; see GenMapAndTopicListModule
  ;; we don't do ant "if" and "unless" props (we have real logic!)
  ;;     if ((ifProperty == null || getProject().getProperties().containsKey(ifProperty))
  ;;             && (unlessProperty == null || !getProject().getProperties().containsKey(unlessProperty))) {
  ;;         attrs.put(p.getName(), p.getValue());
  ;;     }
  ;;     final PipelineHashIO pipelineInput = new PipelineHashIO();
  (let [in-parms (PipelineHashIO.)]
    ;; dita-ot installs "task pipeline" parms in the input pipeline
    ;; ExtensibleAntInvoker:
    ;; for (final Map.Entry<String, String> e: attrs.entrySet()) {
    ;;     pipelineInput.setAttribute(e.getKey(), e.getValue());
    ;; }
    (doseq [[key pair] dita-parms]
      ;; (printf "key: %s    val: %s  :  first %s\n" key pair (first pair))
      (.setAttribute in-parms (str (first pair)) (str (second pair))))
    in-parms))

(defn get-dita-modules
  []
  ;; in principle, each <pipeline ...> element could contain multiple module elements.
  ;; in base, they only ever contain one.

  ;; original DITA-OT logic:
  ;; for each Ant Module m:
  ;;    Ant automatically creates embedded params
  ;;    Ant automatically creates the org.dita.dost.invoker.Module object m
  ;;    Ant invokes method addConfiguredModule(m) on ExtensibleAntInvoker object
  ;;    ExtensibleAntInvoker then takes over:
  ;;        create PipelineFacade object at construction time
  ;;        create ArrayList of modules, to be populated by Ant's calling addConfiguredModule
  ;;        create ArrayList of Params (for <param ...> nested directly
  ;;        on execute() (called by Ant?)
  ;;            create an input-hash (PipelineHashIO
  ;;            move attrs to the input-hash
  ;;            move "pipeline" params to input-hash
  ;;            then execute the Module

  ;; pipeline facade logic:
  ;; final AbstractPipelineModule module = factory.createModule(pipelineModuleName);
  ;; if (module != null) {
  ;;     module.setLogger(logger);
  ;;     module.setJob(job);
  ;;     return module.execute(input);
  ;; }

  ;; we do not need any of these things.  Ant Modules just manage dita
  ;; modules.  PipelineFacade just creates a dita module and invokes
  ;; its exec method.  ModuleFactory just creates and runs dita
  ;; modules.  ExtensibleAntInvoker just manages this process.  We can
  ;; do all of that directly a dozen lines of Clojure code.

  ;; this is what PipelineFacade does:
  (let [glm (GenMapAndTopicListModule.) ;; instead of factory.createModule
        in-parms (get-in-parms)]
    (do
      ;; (doseq [[key pair] dita-parms]
      ;;   (let [p (.getAttribute in-parms (str (first pair)))]
      ;;   (printf "%s : %s\n" (str (first pair)) p)))
      (.setLogger glm *logger*)
      (.setJob glm job)
      (.execute glm in-parms)))
  )


(def dita-logger
  ;; we use plain ol' clojure logging.
  ;; dita-ot, in ExtensibleAntInvoker:
  ;; final DITAOTAntLogger logger = new DITAOTAntLogger(getProject());
  ;; logger.setTask(this);
  ;; pipeline.setLogger(logger);
  ;; pipeline.setJob(getJob(tempDir, getProject()));
)

;; EXPLORATION: create and configure the Ant stuff
;; (ExtensibleAntInvoker, ExtensibleAntInvoker$Module, etc.) using
;; Clojure code
(defn get-gen-list
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
;; (def build-init-tasks
;;   {:pipeline "org.dita.dost.invoker.ExtensibleAntInvoker"
;;    :config-logger "org.dita.dost.log.LogConfigTask"
;;    :dita-ot-echo "org.dita.dost.log.DITAOTEchoTask"
;;    :dita-ot-fail "org.dita.dost.log.DITAOTFailTask"
;;    :dita-ot-copy "org.dita.dost.util.DITAOTCopy"
;;    :job-property "org.dita.dost.platform.JobPropertyTask"
;;    ;; xslt tasks
;;    :job-helper ""
;;    ;; targets
;;    :init-logger nil
;;    :check-arg nil
;;    :log-arg nil
;;    }
;;   )

;; EXPLORATION
;; (defn gen-list
;;   [& args]
;;   ;; <pipeline message="Generate list." taskname="gen-list"
;;   ;;   inputmap="${args.input}"
;;   ;;   tempdir="${dita.temp.dir}">
;;   ;;   <module class="org.dita.dost.module.GenMapAndTopicListModule">
;;   ;;     <param name="inputdir" location="${args.input.dir}" if="args.input.dir"/>
;;   ;;     <param name="ditadir" location="${dita.dir}"/>
;;   ;;     <param name="ditaval" location="${dita.input.valfile}" if="dita.input.valfile"/>
;;   ;;     <param name="validate" value="${validate}"/>
;;   ;;     <param name="generatecopyouter" value="${generate.copy.outer}"/>
;;   ;;     <param name="outercontrol" value="${outer.control}"/>
;;   ;;     <param name="onlytopicinmap" value="${onlytopic.in.map}"/>
;;   ;;     <param name="outputdir" location="${output.dir}"/>
;;   ;;     <param name="transtype" value="${transtype}"/>
;;   ;;     <param name="gramcache" value="${args.grammar.cache}"/>
;;   ;;     <param name="setsystemid" value="${args.xml.systemid.set}"/>
;;   ;;   </module>
;;   ;; </pipeline>
;;   ;; <!-- generate list files -->
;;   ;; <job-helper file="outditafiles.list" property="outditafileslist"/>
;;   ;; etc
;;   (println "GenMapAndTopicListModule")
;;   ;; (println (dita-ant "foo.ditamap")))
;;   ;; (.. GenMapAndTopicListModule (execute)))
;; )

;; EXPLORATION
;; (def build-preprocess-tasks
;;   {;; from src/main/plugins/org.dita.base/build_preprocess_template.xml
;;    :clean-temp nil
;;    ;;{depend.preprocess.pre},
;;    :preprocess-init nil,
;;    :gen-list nil,
;;     ;; dita:depends="{depend.preprocess.gen-list.pre}"
;;     ;; dita:extension="depends org.dita.dost.platform.InsertDependsAction"

;;    :debug-filter nil,
;;    :copy-files nil,
;;    :keyref nil,
;;    :conrefpush nil,
;;    :conref nil,
;;    :topic-fragment nil,
;;    :coderef nil,
;;    :mapref nil,
;;    :move-meta-entries nil,
;;    :mappull nil,
;;    :chunk nil,
;;    :maplink nil,
;;    :move-links nil,
;;    :topicpull nil,
;;    :flag-module nil,
;;    :clean-map nil
;;    ;;{depend.preprocess.post}"
;;    }
;;   )

(defn -main
  []
  (let [parms (get-in-parms)]
    (do
      (doseq [[key pair] dita-parms]
        (printf "%s : %s\n" (str (first pair)) (.getAttribute parms (str (first pair)))))
      (printf "CONF key %s : %s\n"
              (first (:print-trans-types dita-parms))
              (.get cfg/configuration (first (:print-trans-types dita-parms)))))
 (get-dita-modules)))
  ;; (gen-list))
