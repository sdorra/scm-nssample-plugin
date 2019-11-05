import { binder } from "@scm-manager/ui-extensions";
import NavigationLink from "./NavigationLink";
import NavigationRoute from "./NavigationRoute";

const userPredicate = props => {
  return props.user && props.user._links && props.user._links.namespace;
};

binder.bind("user.setting", NavigationLink, userPredicate);
binder.bind("user.route", NavigationRoute, userPredicate);
