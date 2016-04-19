(ns ws-bra-1.core
  (:require
   [ws-bra-1.utils :as utils]
   [thi.ng.math.core :as m]
   [thi.ng.math.noise :as n]
   [thi.ng.geom.core :as g]
   [thi.ng.geom.vector :as v :refer [vec2 vec3]]
   [thi.ng.geom.matrix :as mat]
   [thi.ng.geom.circle :refer [circle]]
   [thi.ng.geom.polygon :refer [polygon2]]
   [thi.ng.geom.basicmesh :refer [basic-mesh]]
   [thi.ng.geom.ptf :as ptf]
   [thi.ng.geom.line :as l]
   [thi.ng.geom.bezier :as b]
   [thi.ng.geom.path :as path]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.dstruct.core :as d]
   [clojure.java.io :as io]
   [thi.ng.geom.mesh.io :as mio]))

(defn save-as-svg
  [path {:keys [width height body]}]
  (->> body
       (svgadapt/all-as-svg)
       (svg/svg {:width (or width 600) :height (or height 400)})
       (svg/serialize)
       (spit path)))

#_(save-as-svg
 "foo.svg"
 {:body (-> (circle [300 200] 100)
            (g/as-polygon 5)
            (utils/poly-as-linestrip)
            (g/sample-uniform 20000 false)
            (polygon2)
            (utils/smooth-polygon 0.125 0.25 8)
            (utils/shape-vertices-as-circles 2))})


(def curve-points
  [[50 10] [40 20] [20 25] [35 35]
   [60 30] [65 20] [80 15] [55 23]
   [45 40] [50 35]])

(defn make-rings-along-curve
  "Takes sequence of numbers and converts it into
  sequence of 3d ring meshes."
  []
  (map-indexed
   (fn [z [x radius]]
     (let [c (circle [x 0] radius)
           e (g/extrude c {:res 80 :depth 1 :mesh (basic-mesh)})
           e (g/translate e [0 0 (* z 10)])]
       e))
   curve-points))

;; meshlab.sf.net

#_(with-open [out (io/output-stream "rings.stl")]
  (mio/write-stl
   (mio/wrapped-output-stream out)
   (g/tessellate (reduce g/into (make-rings-along-curve)))))


(defn make-agent
  []
  {:pos    [0 0]
   :dir    (g/as-cartesian
            (v/vec2 1 (m/radians (m/random -45 45))))
   :speed  (m/random 1 5)        
   :smooth (m/random 0.05 0.25) ;; percentage
   })
