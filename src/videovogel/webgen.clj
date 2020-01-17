(ns videovogel.webgen
  (:require
    [html-tools.website :as website]
    [html-tools.server :as server]
    [hiccup.util :refer [escape-html]]))

;;; inhalte

(def page-data
  {:website-name "VideoVogel"
   :hello "Willkommen"
   :main-description (str "Suchen Sie sich was aus. "
                          "Platzhaltertexte gibt es ja schließlich genug.")
   :products-title "Angebot"
   :products [{:img "landschaft.jpg"
               :title "Perspektivwechsel"
               :text "Erstbegutachtung von Schäden, etc."}
              {:img "mavic-mini.jpg"
               :title "Neuste Technik"
               :text (str "Mit der Drohne lassen sich Fotos und Videos "
                          "aufnehmen. Schon ab 49,- EUR / Std.")}
              {:img "hochzeit.jpg"
               :title "Unvergesslich"
               :text "Halten Sie ihre schönsten Momente fest."}]
   :imprint "Impressum: Kacper Grubalski | Dingelstedtwall 7 | 31737 Rinteln | Mail kacper@grubalski.de"})

;;; navigationsleiste

(defn nav-bar []
  [:header
   [:div.navbar.navbar-dark.bg-dark.shadow-sm
    [:div.container.d-flex.justify-content-between
     [:a.navbar-brand.d-flex.align-items-center
      {:href "#"}
      [:strong (-> page-data :website-name)]]]]])

;;; website beschreibung

(defn main-description []
  [:section.jumbotron.text-center
   [:div.container
    [:h1 (-> page-data :hello)]
    [:p.text-muted (-> page-data :main-description)]]])


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
;;   [:h2 (-> page-data :products-title)]
   [:div
    {:style "display:grid; grid-template-columns:1fr 1fr 1fr;"}
    (doall
     (for [p (-> page-data :products)]
       (product p)))]])


(defn footer []
  [:footer.text-muted
   [:div.container
    [:p (-> page-data :imprint)]]])


;;; config

(defn index-page []
  {:modules [:bootstrap-cdn
             :page-reload]
   :scripts ["page_reload.api.watch();"]
   :content [[:div.container
              (nav-bar)
              (main-description)
              (main-gallery)
              (footer)]]})


(defn website []
  {:pages {"index.html" (fn [req] (index-page))}})


(defn -main [action]
  (cond
    (= action "server") (server/run-http-server (website))
    (= action "build") (website/generate "target/web" (website))
    :else (println "unsuported action")))
