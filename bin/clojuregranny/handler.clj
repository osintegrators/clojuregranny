(ns clojuregranny.handler
  (:use compojure.core)  
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp])
  (:use clojure.data.json)
  (:require [clojure.data.json :as json])
  (:use clojure.java.jdbc)
  (:require [clojure.java.jdbc :as sql])
  )

 (defn attribute-map [res] 
  (:html [:granny (map (fn [x] [:granny x]) res)]) 
  )

;;Connection to Database
(defn db-run
  [crudop sql resfn args]
  (let [db-host "localhost"
        db-port 5432
        db-name "grannyclojure"]
    (def db {:classname "org.postgresql.Driver"
           :subprotocol "postgresql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "clojureuser"
           :password "clojureuser"})
    (with-connection db
      ;; perform CRUD
      (crudop sql resfn args))))

;;Add functionality
 (defn crud-add
  [sql resfn args]
   (resfn
    (insert-values
     ;; table name and fields
     (first sql) (last sql)
     ;; values to insert
     args)))

;;Update functionality 
 (defn crud-update [sql resfn args]
   (try
     (resfn
   (update-values 
    ;; table name, condition
    (first sql) (last sql) 
    ;; values to update with
    args))
   (catch Exception ex
    (.getMessage (.getNextException ex))))
   )

;;Read functionality 
 (defn crud-read [sql resfn args]
     (println sql)
  (with-query-results rs [sql]
    ;;pass result to resfn
   (def result (json-str rs))    
    ;; (dorun (map #(println %) rs))) 
    ;;result   
    result))

;;Retrieve by Id 
 (defn crud-read-id [sql resfn args]
  (resfn
     (with-query-results rs [sql]         
     )))

;;Delete functionality
 (defn crud-delete
  [sql resfn args]  
    (try 
    (resfn
   (delete-rows 
    ;; table name and condition
     (first sql) (last sql)))(catch Exception ex
    (.getMessage (.getNextException ex))))
   )
 
;;Add user
(defn add-user [name address phone email]
  (db-run crud-add [:granny [:name :address :phone :email]] attribute-map [name address phone email]))

;;Update user
(defn update-user [id name address phone email]  
  (db-run crud-update [:granny ["id=?" id]] attribute-map {:name name :address address 
	       :phone phone :email email}))

;;Retrieve all users
(defn get-all-users []
 (db-run crud-read "SELECT * FROM granny" attribute-map nil)  
 )

;;Retrieve user by id
 (defn get-user [id]
 (db-run (crud-read-id ["SELECT * FROM granny where id=?"id] attribute-map nil)))

;;Delete user
(defn delete-user [id]
  (db-run crud-delete [:granny ["id=?" id]] attribute-map nil))


;;Routes for the application 
(defroutes app  
   (GET "/add/:name/:address/:phone/:email" {params :params} 
   (add-user (params :name)(params :address)(params :phone)(params :email))) 
  
   (GET "/delete/:id"  {params :params} 
   (delete-user (params :id)))      
  
   (GET "/update/:id/:name/:address/:phone/:email" {params :params} 
   (update-user (params :id)(params :name)(params :address)(params :phone)(params :email)))    

   (GET "/get" []
     (get-all-users))   
   
   (GET "/get/:id" {params :params} 
     (get-user(params :id)))   
      
   (GET "/" [] (resp/file-response "index.html" {:root "public"}))
   (route/resources "/"))   

;;Request calls
 (defn handler [request]
   {:status 200
    :headers {"Content-Type" "application/json"}}
    app request
  )
 

