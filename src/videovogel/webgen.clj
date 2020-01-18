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
                          "Platzhaltertexte gibt es ja schließlich genug. "
                          "Ach, was wäre der erste Entwurf einer Website ohne "
                          "seine Platzhaltertexte? Ich sage es ihnen: "
                          "eine trostlose Angelegenheit.")
   :products-title "Angebot"
   :products [{:img "landschaft.jpg"
               :title "Perspektivwechsel"
               :text "Erstbegutachtung von Schäden, etc."}
              {:img "mavic-mini.jpg"
               :title "Neuste Technik"
               :text (str "Mit der Drohne lassen sich Fotos und Videos "
                          "aufnehmen.")}
              {:img "hochzeit.jpg"
               :title "Unvergesslich"
               :text "Halten Sie ihre schönsten Momente fest."}]
   :imprint (str "Impressum: Kacper Grubalski | "
                 "Dingelstedtwall 7 | "
                 "31737 Rinteln | "
                 "Mail kacper -ät- grubalski.de")})



;;; header / navbar

(defn header []
  [:header
   [:div.collapse.bg-dark
    {:id "navbarHeader"}
    [:div.container
     [:div.row
      [:div.col-sm-8.col-md-7.py-4
       [:h4.text-white "Über"]
       [:p.text-muted "Ich bins, Kacper."]]
      [:div.col-sm-4.offset-md-1.py-4
       [:h4.text-white "Kontakt"]
       [:ul.list-unstyled
        [:li [:a.text-wihte {:href "#"} "E-Mail"]]]]]]]
   [:div.navbar.navbar-dark.bg-dark.shadow-sm
    [:div.container.d-flex.justify-content-between
     [:a.navbar-brand.d-flex.align-items-center
      {:href "#"}
      [:strong (-> page-data :website-name)]]
     [:button.navbar-toggler.collapsed
      {:type "button"
       :data-toggle "collapse"
       :data-target "#navbarHeader"
       :aria-controls "navbarHeader"
       :aria-expanded "false"
       :aria-label "Auf/Zu"}
      [:span.navbar-toggler-icon]]]]])



;;; product

(defn product [p]
  [:div.col-md-4
   [:div.card.mb-4.shadow-sm
    [:img.card-img-top
     {:src (str "img/" (-> p :img))
      :width "100%"
      :height "225"}]
    [:div.card-body
     [:h5.card-title (-> p :title escape-html)]
     [:p.card-text (-> p :text escape-html)]
     [:div.d-flex.justify-content-between.align-items-center
      [:div.btn-group
       [:button.btn.btn-sm.btn-outline-secondary
        {:type "button"}
        "Info"]
       [:button.btn.btn-sm.btn-outline-secondary
        {:type "button"}
        "Buchen"]]
      [:small.text-muted "ab 39,- EUR"]]]]])



;;; main-gallery

(defn main-gallery []
  [:div.container
   [:div.row
    (doall
     (for [p (-> page-data :products)]
       (product p)))]])



;;; content

(defn content []
  [:main
   {:role "main"}
   [:section.jumbotron.text-center
    {:padding-top "3rem"
     :padding-bottom "3rem"
     :margin-bottom "0"
     :background-color "#fff"}
    [:div.container
     [:h1 (-> page-data :hello)]
     [:p.lead.text-muted (-> page-data :main-description)]]]
   [:div.album.py-5.bg-light
    (main-gallery)]])



;;; footer

(defn footer []
  [:footer.text-muted
   {:padding-top "3rem"
    :padding-bottom "3rem"}
   [:div.container
    [:p
     {:margin-bottom ".25rem"}
     (-> page-data :imprint)]]
   [:script {:src "https://code.jquery.com/jquery-3.4.1.slim.min.js"}]
   [:script {:src "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"}]])

;;; config

(defn index-page []
  {:modules [:bootstrap-cdn
             :page-reload]
   :scripts ["page_reload.api.watch();"]
   :content [[:body
              (header)
              (content)
              (footer)]]})


(defn website []
  {:pages {"index.html" (fn [req] (index-page))}})


(defn -main [action]
  (cond
    (= action "server") (server/run-http-server (website))
    (= action "build") (website/generate "target/web" (website))
    :else (println "unsuported action")))
