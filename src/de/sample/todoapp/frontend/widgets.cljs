(ns de.sample.todoapp.frontend.widgets
  (:require
   [re-frame.core :as rf]))


(defn textfield
  [{:keys [id path label]} value]
  (fn [_ value]
    [:div.formfield.textfield
     (if label
       [:label {:for (str id "-textfield")} label])
     [:input {:id (str id "-textfield")
              :value value
              :on-change (fn [e]
                           (rf/dispatch-sync [:app/set path (-> e .-target .-value)]))}]]))

(defn checkbox
  [{:keys [id path label]} value]
  (fn [_ value]
    [:div.formfield.checkbox
     [:input {:id        (str id "-checkbox")
              :type      "checkbox"
              :checked   value
              :on-change (fn [e]
                           (rf/dispatch-sync [:app/set path (-> e .-target .-checked)]))}]]))


(defn button
  [{:keys [id label event]}]
  [:div.formfield.button
   [:button {:id id
             :on-click (fn [e]
                         (rf/dispatch event))}
    label]])
