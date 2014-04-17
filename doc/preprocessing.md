# Preprocessing

the extension points are themselves specified in

src/main/plugins/org.dita.base/plugin.xml

e.g.  <extension-point id="depend.preprocess.pre" name="Preprocessing pre-target"/>

;; dita-ot preprocessing extension point names look like:
;;   depend.preprocess.foo.pre
;; dita-clj uses:   foo-pre-hook

;; entire preprocessing pipeline:
:preproc-pre-hook  ;; dita-ot:   depend.preprocess.pre
:preproc-post-hook ;; dita-ot:   depend.preprocess.post

;; individual preprocessing steps:
;; Clean temp pre-target:  depend.preprocess.clean-temp.pre
:preproc-clean-temp-pre-hook

;; Generate list pre-target  depend.preprocess.gen-list.pre
:preproc-gen-list-pre-hook
in main/plugins/org.dita.base/build_preprocess_template.xml:
  <target name="preprocess"
    dita:depends="{depend.preprocess.pre},
                  preprocess.init,
                  gen-list,

  <target name="gen-list"
    dita:depends="{depend.preprocess.gen-list.pre}"


;; gen-list : org.dita.dost.module.GenMapAndTopicListModule

;; Debug and filter pre-target
:depend.preprocess.debug-filter.pre
:depend.preprocess.conrefpush.pre
;; Content reference push pre-target
:depend.preprocess.move-meta-entries.pre
;; Move meta entries pre-target
:depend.preprocess.conref.pre
;; Content reference pre-target
:depend.preprocess.coderef.pre
;; Code reference pre-target
:depend.preprocess.mapref.pre
;; Map reference pre-target
:depend.preprocess.keyref.pre
;; Resolve key reference pre-target
:depend.preprocess.mappull.pre
;; Map pull pre-target
:depend.preprocess.chunk.pre
;; Chunking pre-target
:depend.preprocess.maplink.pre
;; Map link pre-target
:depend.preprocess.move-links.pre
;; Move links pre-target
:depend.preprocess.topicpull.pre
;; Topic pull pre-target
:depend.preprocess.copy-files.pre
;; Copy files pre-target
:depend.preprocess.copy-image.pre
;; Copy images pre-target
:depend.preprocess.copy-html.pre
;; Copy HTML pre-target
:depend.preprocess.copy-flag.pre
;; Copy flag pre-target
:depend.preprocess.copy-subsidiary.pre
:Copy subsidiary pre-target
:depend.preprocess.copy-generated-files.pre
;; Copy generated files pre-target

