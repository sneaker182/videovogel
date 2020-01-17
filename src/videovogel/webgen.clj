(ns videovogel.webgen
  (:require
    [html-tools.website :as website]
    [html-tools.server :as server]
    [hiccup.util :refer [escape-html]]))

(def page-data
  {:products [{:img "mavic-mini.jpg"
               :title "produkt 1"
               :text "asdfasdfasdfasd<hr>"}
              {:title "produkt 2"}
              {:title "vxcvy 3"}]})

(defn header []
  [:div
   [:h1 "abc"]])

;;; main-gallery

(defn product [p]
  [:div.card
   [:img.card-img-top
    {:src (str "img/" (-> p :img))}]
   [:div.card-body
    [:h5.card-title (-> p :title escape-html)]
    [:p.card-text (-> p :text escape-html)]]])

(defn main-gallery []
  [:div
   [:h2 "Main Gallery"]
   [:div
    {:style "display:grid; grid-template-columns:1fr 1fr 1fr;"}
    (doall
     (for [p (-> page-data :products)]
       (product p)))]])

;;; config - in diesem block funzt die live

(defn index-page []
  {:modules [:bootstrap-cdn
             :page-reload]
   :scripts ["page_reload.api.watch();"]
   :content [[:div.container
              (header)
              (main-gallery)
              "hello asdf" "asdf"]]})


(defn website []
  {:pages {"index.html" (fn [req] (index-page))}})


(defn -main [action]
  (cond
    (= action "server") (server/run-http-server (website))
    (= action "build") (website/generate "target/web" (website))
    :else (println "unsuported action")))
