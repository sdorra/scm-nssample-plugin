import React from "react";
import { WithTranslation, withTranslation } from "react-i18next";
import { NavLink } from "@scm-manager/ui-components";

type Props = WithTranslation & {
  url: string;
};
class NavigationLink extends React.Component<Props> {
  render() {
    const { url, t } = this.props;
    return <NavLink to={`${url}/settings/namespace`} label={t("scm-nssample-plugin.navigation")} />;
  }
}
export default withTranslation("plugins")(NavigationLink);
