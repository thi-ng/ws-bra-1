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
   [thi.ng.geom.line :as l]
   [thi.ng.geom.bezier :as b]
   [thi.ng.geom.path :as path]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.dstruct.core :as d]))

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
            (g/as-polygon 3)
            (utils/poly-as-linestrip)
            (g/sample-uniform 10 false)
            (polygon2)
            (utils/smooth-polygon 0.125 0.25 8)
            #_(utils/shape-vertices-as-circles 2))})
