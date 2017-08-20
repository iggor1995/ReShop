package com.epam.igor.electronicsshop.action;

/**
 * Created by User on 03.08.2017.
 */
public class ActionResult {
        private final String view;
        private final boolean redirect;

        public ActionResult(String pageName, boolean redirect) {
            this.view = pageName;
            this.redirect = redirect;
        }

        public ActionResult(String pageName) {
            this.view = pageName;
            this.redirect = false;
        }

        public String getView() {
            return view;
        }

        public boolean isRedirect() {
            return redirect;
        }
    }
