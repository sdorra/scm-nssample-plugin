import React from "react";
import { Route } from "react-router-dom";
import { User } from "@scm-manager/ui-types";
import NamespaceConfiguration from "./NamespaceConfigurationPage";

type Props = {
  url: string;
  user: User;
};

class NavigationRoute extends React.Component<Props> {
  render() {
    const { user, url } = this.props;
    return (
      <Route
        path={`${url}/settings/namespace`}
        render={() => <NamespaceConfiguration link={user._links.namespace.href} />}
      />
    );
  }
}

export default NavigationRoute;
