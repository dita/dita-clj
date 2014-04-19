(defproject org.mobileink/dita-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot dita.core
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :target-path "target/%s/lib" ;; jar goes here
  :clean-targets [:compile-path "target"]
  :resource-paths ["resources"] ;; ["src/main/resource"]
  :java-source-exclude ["org/dita/dost/invoker/JavaInvoker.java"
                        "org/dita/dost/invoker/CommandLineInvoker.java"]
  :javac-options ["-target" "1.7" "-source" "1.7"] ;; "-Xlint:deprecation"]
  :compile-path "target/classes"
  :omit-source true  ;; don't put source in jar
  ;; :jar-name "dost.jar"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [xerces/xercesImpl "2.11.0"]
                 [xml-apis/xml-apis "1.4.01"]
                 ;; [xml-apis/xml-apis "2.0.2"]
                 [xml-resolver/xml-resolver "1.2"]
                 [commons-codec/commons-codec "1.8"]
                 [com.ibm.icu/icu4j "52.1"]
                 [org.apache.ant/ant "1.8.4"] ;; dita-ot 1.8.4; latest 1.9.2
                 [net.sourceforge.saxon/saxon "9.1.0.8"] ;; see README.md
                 ;; [net.sourceforge.saxon/saxon-dom "9.1.0.8"] ;; see README.md
                 [nu.validator.htmlparser/htmlparser "1.4"]
                 ;; [usr.local/saxon "9.5.1.2J"] ;; see README.md
                 ;; [dom4j/dom4j "1.6.1"]
                 [junit/junit "4.11"]
                 [xmlunit/xmlunit "1.5"]
                 [org.clojure/tools.logging "0.2.3"]])
