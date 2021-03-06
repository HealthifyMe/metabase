(ns metabase.events-test
  (:require [clojure.core.async :as async]
            [expectations :refer :all]
            [metabase.events :as events]
            [metabase.test.util :as tu]))

(def ^:private testing-topic :event-test-topic)

(def ^:private testing-sub-channel (async/chan))


;; ## Basic Pub/Sub TESTS

(tu/resolve-private-vars metabase.events subscribe-to-topic!)

(subscribe-to-topic! testing-topic testing-sub-channel)

;; we should get back our originally posted object no matter what happens
(expect
  {:some :object}
  (events/publish-event! testing-topic {:some :object}))

;; when we receive a message it should be wrapped with {:topic `topic` :item `message body`}
(expect
  {:topic testing-topic
   :item  {:some :object}}
  (async/<!! testing-sub-channel))
