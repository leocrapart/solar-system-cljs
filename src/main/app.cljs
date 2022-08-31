(ns app)





;; grab canvas element

(def canvas (.getElementById js/document "canvas"))
(def ctx (.getContext canvas "2d"))

(def width 1000)
(def height 800)
(def cx 500)
(def cy 400)


(defn draw-circle [ctx x y r color]
  (set! (.-fillStyle ctx) color)
  (.beginPath ctx)
  (.arc ctx x y r 0 (* 2 Math/PI))
  (.fill ctx))

(defn draw-sun [ctx]
  (draw-circle ctx cx cy 100 "#f59e0b"))

(defn deg-to-rad [deg]
  (* deg (/ (* 2 Math/PI) 360)))

(defn draw-planet [ctx delta planet-param]
  (let [seconds-elapsed (/ delta 60)
        orbital-period (planet-param :orbital-period)
        initial-angle (planet-param :initial-angle)
        orbital-radius (planet-param :orbital-radius)
        angle (+ initial-angle (* (/ 360 orbital-period) (mod seconds-elapsed orbital-period)))
        theta (deg-to-rad angle)
        x (* orbital-radius (Math/cos theta))
        y (- (* orbital-radius (Math/sin theta)))]
    (draw-circle ctx (+ cx x) (+ cy y) (planet-param :radius) (planet-param :color))))

(defn clean-canvas [ctx]
  (set! (.-fillStyle ctx) "#000")
  (.beginPath ctx)
  (.rect ctx 0 0 width height)
  (.fill ctx))

(def delta (atom 0))
(def fps 60)

;; 0 60 140 160 270

;; revolutions  20 10 

(def mercure
  {:radius 20
   :color "#fb923c"
   :orbital-period 20
   :orbital-radius 200
   :initial-angle 140})

(def venus
  {:radius 25
   :color "#7c3aed"
   :orbital-period 30
   :orbital-radius 260
   :initial-angle 0})

(def earth
  {:radius 30
   :color "#0d9488"
   :orbital-period 40
   :orbital-radius 330
   :initial-angle 60})

(def mars
  {:radius 40
   :color "#b91c1c"
   :orbital-period 50
   :orbital-radius 400
   :initial-angle 270})

(def jupiter
  {:radius 60
   :color "#d4d4d4"
   :orbital-period 60
   :orbital-radius 470
   :initial-angle 160})

(defn draw []
  (clean-canvas ctx)
  (draw-sun ctx)
  (draw-planet ctx @delta mercure)
  (draw-planet ctx @delta venus)
  (draw-planet ctx @delta earth)
  (draw-planet ctx @delta mars)
  (draw-planet ctx @delta jupiter)
  ;; (println @delta)
  (swap! delta inc))




(defn init []
  (println "hello worlds")
  (println (.now js/Date))
  (.dir js/console (clj->js ctx))
  (draw)
  (js/setInterval draw (/ 1000 fps)))

;; put draw into a loop with delta frames elapsed

