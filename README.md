# pedestal-extensions

A Clojure library containing useful functions for pedestal

## Usage

### pedestal-extensions.handler

#### defhandler

You can define a pedestal interceptor that creates a response (aka request handler) using this macro.

The interceptor created by this macro will add the :response key to the context, making it a terminating interceptor so that no other interceptors can follow it.

When using this macro,

- Request validation is required. You must supply validation code for the handler, otherwise a compile error will occur.
- If validation fails, a 400 response will be automatically returned.
- You only need to create a ring response. You do not need to handle the context. Forgetting to add the response to the context is a common mistake when creating a pedestal interceptor, which this macro helps you avoid.

This macro creates an interceptor with the name supplied to `defhandler`, and creates a function with the supplied name + `-handler`.

For example, if you write code like the following:

```clojure
(defhandler get-user-info
  [context params]
  :validate ...your validation code...
  
  ...your code...)
```

it will generate code like:

```
(defn get-user-info-handler
  [context]
  ...generated code...)

(def get-user-info
  {:name ::get-user-info
   :enter get-user-info-handler})
```

So, the name you supplied to `defhandler` will become the name of the generated interceptor.

If you want to use the handler function itself, you can get it by retrieving the `:enter` value of the interceptor like `(:enter get-user-info)`, or you can directly use `get-user-info-handler`.

Usage:

```clojure
(defn- validate
  [{:keys [name]}]
  (if
    (nil? name) [{:name "is required"} nil]
    [nil {:name name}]))

(defhandler hello-world
  [{{:keys [query-params]} :request} valid-params]
  :validate (validate query-params your-validator)

  (-> (response (str "Hello, " (:name valid-params) "!"))
      (content-type "text/plain")))
```

The first parameter of `defhandler` is a pedestal context and the second is the success result of validation.

The next item of the parameter vector must be a `:validate` key followed by the validation body. This macro automatically executes the validation body (in the previous example, `(validate query-params your-validator)` is the validation body).

You can write any code as the validation body, but the body must return a vector containing `[errors success]`. If the validation succeeds, errors must be nil, and the success value will be bound to the second parameter of this macro. If the validation fails, `errors` must contain an error description. In case of an error, the value of `success` will not be handled, so you can put any value in it.

When the validation fails, this macro automatically creates a response with status 400, using the `errors` as the body.

After the `:validate` key and the validation body, you can write any code, but it must return a `ring` response (not a pedestal `context`). The response will automatically be added to the pedestal `context`, so that pedestal will return the response to the client.



## License

Copyright © 2024 Tsutomu YANO

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
