# scm-nssample-plugin

Sample SCM-Manager Plugin which implements a custom namespace strategy.

## Usage

Login as Administrator.
The Strategy can be activated by choosing `Configurable` at `Administration -> Settings -> Namespace Strategy`.
To change the namespace of a user go to `Users`, 
select a user, go to `Settings -> Namespace` and enter the desired namespace.  

## Requirements

* Java 8
* Maven >= 3.5

## Development

To start an SCM-Manager instance with the plugin pre installed, just run the following command:

```bash
mvn clean run
``` 

## Installation

Create the SMP file with the following command:

```bash
mvn clean package
``` 

The final SMP file is located in target directory.
To install the plugin to an existing SCM-Manager instance, 
put the smp file into the `plugins` folder of the scm home directory
and restart the instance.

## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
