# pedestal-extensions

A Clojure library containing useful functions for pedestal

## Usage

### pedestal-extensions.handler

#### defhandler

You can define a pedestal interceptor that create response (aka `request handler`) by this macro.

The interceptor made by this macro will add :response key to context, so the interceptor is a tailing interceptor and no other interceptors can follow after this intercepter.

Using this macro, 

- request validation is required. You `Must` supply a validation code for handler, or compile error
- If validation failed, 400 response automatically be returned.
- Only you need it to create `ring` response. You do not need handle context. Creating a response but forget adding the reponse to context is a common mistake on pedestal interceptor. You can avoid it by this macro.

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

The next of parameter vector must be a `:validate` key and validation body. This macro automatically execute validation body (in the previous example, `(validate query-params your-validator)` is the validation body).

You can write any code as the validation body, but the body must return a vector containing `[errors success]`. If the validation succeeded, `errors` must be nil and the success value will be binded to the second parameter of this macro. If the validation failed, the `errors` must be a error description. In the error case, the value of `success` is not be handled, so you can put any value on it.

When the validation failed, this macro automatically create a response with status 400, with the `errors` as a body.

After `:validation` key and the validation body, you can write any code, but must return a `ring` response (not pedestal `context`). The response automatically be added to pedestal `context`, so that pedestal will return the response to client.



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
