import React from "react";
import { apiClient, ErrorNotification, Loading, Notification, Subtitle } from "@scm-manager/ui-components";
import { withTranslation, WithTranslation } from "react-i18next";
import { NamespaceConfiguration } from "./types";
import NamespaceConfigurationForm from "./NamespaceConfigurationForm";

type Props = WithTranslation & {
  link: string;
};

type State = {
  configuration?: NamespaceConfiguration;
  loading: boolean;
  error?: error;

  submitted: boolean;
};

class NamespaceConfigurationPage extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      loading: false,
      submitted: false
    };
  }
  componentDidMount() {
    this.setState({
      loading: true
    });
    apiClient
      .get(this.props.link)
      .then(resp => resp.json())
      .then(configuration => {
        this.setState({
          loading: false,
          configuration,
          error: undefined
        });
      })
      .catch(error => {
        this.setState({
          loading: false,
          error
        });
      });
  }
  configSubmitted = () => {
    this.setState({
      submitted: true
    });
  };
  render() {
    const { link, t } = this.props;
    const { configuration, loading, submitted, error } = this.state;
    let children;
    if (loading) {
      children = <Loading />;
    } else if (error) {
      children = <ErrorNotification error={error} />;
    } else if (configuration) {
      children = (
        <NamespaceConfigurationForm url={link} config={configuration} onConfigSubmitted={this.configSubmitted} />
      );
    }
    let submittedNotification = null;
    if (submitted) {
      submittedNotification = <Notification type="success">{t("scm-nssample-plugin.submitted")}</Notification>;
    }
    return (
      <>
        <Subtitle subtitle={t("scm-nssample-plugin.title")} />
        {submittedNotification}
        {children}
      </>
    );
  }
}
export default withTranslation("plugins")(NamespaceConfigurationPage);
