// @flow
import React from "react";
import { translate } from "react-i18next";
import {
  apiClient,
  ErrorNotification,
  InputField,
  SubmitButton
} from "@scm-manager/ui-components";
import type { NamespaceConfiguration } from "./types";

type Props = {
  url: string,
  config: NamespaceConfiguration,
  onConfigSubmitted?: NamespaceConfiguration => void,

  // context props
  t: string => string
};

type State = {
  namespace?: string,

  loading: boolean,
  error?: Error
};

class NamespaceConfigurationForm extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      loading: false,
      namespace: props.config.namespace
    };
  }

  submit = (e: Event) => {
    e.preventDefault();
    const { namespace } = this.state;
    if (namespace) {
      this.save({ namespace });
    }
  };

  save = (config: NamespaceConfiguration) => {
    this.setState({
      loading: true
    });

    const { url, onConfigSubmitted } = this.props;
    apiClient
      .put(url, config, "application/vnd.scmm-configurablenamespace+json;v=2")
      .then(() => {
        this.setState({
          loading: false,
          error: undefined
        });
        if (onConfigSubmitted) {
          onConfigSubmitted(config);
        }
      })
      .catch(error =>
        this.setState({
          error,
          loading: false
        })
      );
  };

  onChange = (value: string, name: string) => {
    this.setState({
      [name]: value
    });
  };

  isValid = () => {
    const { namespace } = this.state;
    return !!namespace;
  };

  render() {
    const { t } = this.props;
    const { namespace, loading, error } = this.state;
    return (
      <form onSubmit={this.submit}>
        <ErrorNotification error={error} />
        <InputField
          name="namespace"
          label={t("scm-nssample-plugin.namespace")}
          value={namespace}
          onChange={this.onChange}
        />
        <SubmitButton
          label={t("scm-nssample-plugin.save")}
          loading={loading}
          disabled={!this.isValid()}
        />
      </form>
    );
  }
}

export default translate("plugins")(NamespaceConfigurationForm);
